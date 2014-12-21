/*
 * Copyright (c) Oct 2, 2013 ZYXW. All rights reserved.
 */
package jp.caliconography.one_liners.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import jp.caliconography.one_liners.R;

/**
 * Created based on http://qiita.com/GeneralD/items/89278fc0f5513abe0bb3
 * Changed packege name.
 * Customize to use android.app.DialogFragment
 * by abe on 2014/11/17.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment implements OnClickListener {

    // flags
    public static final int NO_POSITIVE_BUTTON = 1 << 0;
    public static final int NO_NEGATIVE_BUTTON = 1 << 1;
    public static final int NO_CLOSE_BUTTON = 1 << 2;
    public static final int ADD_NEUTRAL_BUTTON = 1 << 3;

    private static final int LAYOUT = R.layout.common_dialog;
    private static final int ID_TITLE = R.id.dialog_title_textview;
    private static final int ID_MESSAGE = R.id.dialog_message_textview;
    private static final int ID_POSITIVE_BUTTON = R.id.dialog_positive_button;
    private static final int ID_NEGATIVE_BUTTON = R.id.dialog_negative_button;
    private static final int ID_CLOSE_BUTTON = R.id.dialog_close_button;
    private static final int ID_NEUTRAL_BUTTON = R.id.dialog_neutral_button;

    private static final String TAG = "exclusive_dialog";

    private IDialogFragmentListener listener;
    private int listenerId;

    private boolean clickGuard = false;
    private boolean dismissFlag = false;

    /**
     * Use this method as a constructor!!
     *
     * @return new instance
     */
    public static final DialogFragment newInstance() {

        return newInstance(true);
    }

    /**
     * Use this method as a constructor!!
     *
     * @param cancelable whether the shown Dialog is cancelable.
     * @return new instance
     */
    public static final DialogFragment newInstance(boolean cancelable) {

        return newInstance(0, cancelable);
    }

    /**
     * Use this method as a constructor!!
     *
     * @param flags 0 ok, unless especially
     * @return new instance
     * @see #NO_POSITIVE_BUTTON
     * @see #NO_NEGATIVE_BUTTON
     * @see #NO_CLOSE_BUTTON
     */
    public static final DialogFragment newInstance(int flags) {

        return newInstance(flags, true);
    }

    /**
     * Use this method as a constructor!!
     *
     * @param flags      0 ok, unless especially
     * @param cancelable whether the shown Dialog is cancelable.
     * @return new instance
     * @see #NO_POSITIVE_BUTTON
     * @see #NO_NEGATIVE_BUTTON
     * @see #NO_CLOSE_BUTTON
     */
    public static final DialogFragment newInstance(int flags, boolean cancelable) {

        Bundle args = new Bundle();
        args.putInt("flags", flags);

        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);
        fragment.setCancelable(cancelable);
        return fragment;
    }

    /**
     * New instance with force ok button. (cancelable = false)
     *
     * @param flags the flags (0 ok)
     * @return new instance
     * @see #NO_POSITIVE_BUTTON
     * @see #NO_NEGATIVE_BUTTON
     * @see #NO_CLOSE_BUTTON
     */
    public static final DialogFragment newInstanceForceOkDialog(int flags) {

        return newInstance(NO_NEGATIVE_BUTTON | NO_CLOSE_BUTTON | flags, false);
    }

    /**
     * New instance with no button. (cancelable = false)
     *
     * @param flags the flags (0 ok)
     * @return new instance
     * @see #NO_POSITIVE_BUTTON
     * @see #NO_NEGATIVE_BUTTON
     * @see #NO_CLOSE_BUTTON
     */
    public static final DialogFragment newInstanceNoButton(int flags) {

        return newInstance(NO_POSITIVE_BUTTON | NO_NEGATIVE_BUTTON
                | NO_CLOSE_BUTTON | flags, false);
    }

    /**
     * Keep this constructor!!
     * (Use {@link #newInstance()} as a substitute!!)
     */
    public DialogFragment() {

        // Don't do anything here!!
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        int flags = args.getInt("flags");
        boolean noPosBtn = (flags & NO_POSITIVE_BUTTON) != 0;
        boolean noNegBtn = (flags & NO_NEGATIVE_BUTTON) != 0;
        boolean noClsBtn = (flags & NO_CLOSE_BUTTON) != 0;
        boolean addNtrlBtn = (flags & ADD_NEUTRAL_BUTTON) != 0;

        convertResIdToStringInArgs("titleResId", "title");
        convertResIdToStringInArgs("messageResId", "message");
        convertResIdToStringInArgs("posbtnTextResId", "posbtnText");
        convertResIdToStringInArgs("negbtnTextResId", "negbtnText");
        convertResIdToStringInArgs("ntrlbtnTextResId", "ntrlbtnText");

        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(LAYOUT);
        dialog.setCanceledOnTouchOutside(false);

//        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_back));

        View view;

        // title
        view = dialog.findViewById(ID_TITLE);
        if (view instanceof TextView) {
            if (!args.containsKey("title")) view.setVisibility(View.GONE);
            else ((TextView) view).setText(args.getString("title"));
        }
        dialog.setTitle(args.getString("title"));

        // message
        view = dialog.findViewById(ID_MESSAGE);
        if (view instanceof TextView) {
            if (!args.containsKey("message")) view.setVisibility(View.GONE);
            else ((TextView) view).setText(args.getString("message"));
        }

        // positive button
        view = dialog.findViewById(ID_POSITIVE_BUTTON);
        if (view != null) {
            if (noPosBtn) view.setVisibility(View.GONE);
            else {
                view.setOnClickListener(this);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (args.containsKey("posbtnText"))
                        textView.setText(args.getString("posbtnText"));
                }
            }
        }

        // negative button
        view = dialog.findViewById(ID_NEGATIVE_BUTTON);
        if (view != null) {
            if (noNegBtn) view.setVisibility(View.GONE);
            else {
                view.setOnClickListener(this);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (args.containsKey("negbtnText"))
                        textView.setText(args.getString("negbtnText"));
                }
            }
        }

        // neutral button
        view = dialog.findViewById(ID_NEUTRAL_BUTTON);
        if (view != null) {
            if (addNtrlBtn) {
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(this);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (args.containsKey("ntrlbtnText"))
                        textView.setText(args.getString("ntrlbtnText"));
                }
            }
        }

        // close button
        view = dialog.findViewById(ID_CLOSE_BUTTON);
        if (view != null) {
            if (noClsBtn) view.setVisibility(View.GONE);
            else view.setOnClickListener(this);
        }

        return dialog;
    }

    @Override
    public void onResume() {

        if (dismissFlag) { // dismiss a dialog
            Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
                getFragmentManager().beginTransaction().remove(fragment).commit();
                dismissFlag = false;
            }
        }
        super.onResume();
    }

    /**
     * @deprecated Use {@link #show(FragmentManager)}!!
     */
    @Override
    @Deprecated
    public final void show(FragmentManager manager, String tag) {

        show(manager);
    }

    /**
     * Display the dialog, adding the fragment to the given FragmentManager.
     * This is a convenience for explicitly creating a transaction, adding the fragment to it with the given tag,
     * and committing it. This does <em>not</em> add the transaction to the back stack.
     * When the fragment is dismissed, a new transaction will be executed to remove it from the activity.
     *
     * @param manager The FragmentManager this fragment will be added to.
     */
    public final void show(FragmentManager manager) {

        deleteDialogFragment(manager);
        clickGuard = false;

        // super.show(manager, TAG);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, TAG);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void dismiss() {

        if (isResumed()) super.dismiss(); // dismiss now
        else dismissFlag = true; // dismiss on onResume
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        if (listener != null) listener.onEvent(listenerId, IDialogFragmentListener.ON_DISMISS);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        super.onCancel(dialog);
        if (listener != null) listener.onEvent(listenerId, IDialogFragmentListener.ON_CANCEL);
    }

    @Override
    public void onClick(View v) {

        if (clickGuard) return;
        clickGuard = true;

        int event = -1;
        switch (v.getId()) {
            case ID_POSITIVE_BUTTON:
                event = IDialogFragmentListener.ON_POSITIVE_BUTTON_CLICKED;
                break;
            case ID_NEGATIVE_BUTTON:
                event = IDialogFragmentListener.ON_NEGATIVE_BUTTON_CLICKED;
                break;
            case ID_NEUTRAL_BUTTON:
                event = IDialogFragmentListener.ON_NEUTRAL_BUTTON_CLICKED;
                break;
            case ID_CLOSE_BUTTON:
                event = IDialogFragmentListener.ON_CLOSE_BUTTON_CLICKED;
                break;
        }
        if (listener != null) listener.onEvent(listenerId, event);
        dismiss();
    }

    /**
     * Sets dialog title.
     *
     * @param title dialog title
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setTitle(String title) {

        getArguments().putString("title", title);
        getArguments().remove("titleResId");
        Dialog dialog = getDialog();
        if (dialog == null) return this;
        View view = dialog.findViewById(ID_TITLE);
        if (view instanceof TextView) ((TextView) view).setText(title);
        return this;
    }

    /**
     * Sets dialog title.
     *
     * @param resId resource id
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setTitle(int resId) {

        if (isAdded()) setTitle(getString(resId));
        else {
            getArguments().putInt("titleResId", resId);
            getArguments().remove("title");
        }
        return this;
    }

    /**
     * Sets dialog message.
     *
     * @param message dialog message
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setMessage(String message) {

        getArguments().putString("message", message);
        getArguments().remove("messageResId");
        Dialog dialog = getDialog();
        if (dialog == null) return this;
        View view = dialog.findViewById(ID_MESSAGE);
        if (view instanceof TextView) ((TextView) view).setText(message);
        return this;
    }

    /**
     * Sets dialog message.
     *
     * @param resId resource id
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setMessage(int resId) {

        if (isAdded()) setMessage(getString(resId));
        else {
            getArguments().putInt("messageResId", resId);
            getArguments().remove("message");
        }
        return this;
    }

    /**
     * Sets text on positive button.
     *
     * @param text the text
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setPositiveButtonText(String text) {

        getArguments().putString("posbtnText", text);
        getArguments().remove("posbtnTextResId");
        Dialog dialog = getDialog();
        if (dialog == null) return this;
        View view = dialog.findViewById(ID_POSITIVE_BUTTON);
        if (view instanceof TextView) ((TextView) view).setText(text);
        return this;
    }

    /**
     * Sets text on positive button.
     *
     * @param resId resource id
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setPositiveButtonText(int resId) {

        if (isAdded()) setPositiveButtonText(getString(resId));
        else {
            getArguments().putInt("posbtnTextResId", resId);
            getArguments().remove("posbtnText");
        }
        return this;
    }

    /**
     * Sets text on negative button.
     *
     * @param text the text
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setNegativeButtonText(String text) {

        getArguments().putString("negbtnText", text);
        getArguments().remove("negbtnTextResId");
        Dialog dialog = getDialog();
        if (dialog == null) return this;
        View view = dialog.findViewById(ID_NEGATIVE_BUTTON);
        if (view instanceof TextView) ((TextView) view).setText(text);
        return this;
    }

    /**
     * Sets text on negative button.
     *
     * @param resId resource id
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setNegativeButtonText(int resId) {

        if (isAdded()) setNegativeButtonText(getString(resId));
        else {
            getArguments().putInt("negbtnTextResId", resId);
            getArguments().remove("negbtnText");
        }
        return this;
    }

    /**
     * Sets text on neutral button.
     *
     * @param text the text
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setNeutralButtonText(String text) {

        getArguments().putString("ntrlbtnText", text);
        getArguments().remove("ntrlbtnTextResId");
        Dialog dialog = getDialog();
        if (dialog == null) return this;
        View view = dialog.findViewById(ID_NEUTRAL_BUTTON);
        if (view instanceof TextView) ((TextView) view).setText(text);
        return this;
    }

    /**
     * Sets text on neutral button.
     *
     * @param resId resource id
     * @return this {@link DialogFragment} instance
     */
    public DialogFragment setNeutralButtonText(int resId) {

        if (isAdded()) setNeutralButtonText(getString(resId));
        else {
            getArguments().putInt("ntrlbtnTextResId", resId);
            getArguments().remove("ntrlbtnText");
        }
        return this;
    }

    private void convertResIdToStringInArgs(String resIdkey, String destKey) {

        Bundle args = getArguments();
        if (isAdded()) {
            if (args.containsKey(resIdkey)) {
                String string = getString(args.getInt(resIdkey));
                args.putString(destKey, string);
                args.remove(resIdkey);
            }
        }
    }

    /**
     * @param manager {@link FragmentManager}
     */
    private void deleteDialogFragment(final FragmentManager manager) {

        DialogFragment previous = (DialogFragment) manager.findFragmentByTag(TAG);
        if (previous == null) return;

        Dialog dialog = previous.getDialog();
        if (dialog == null) return;

        if (!dialog.isShowing()) return;

        previous.onDismissExclusiveDialog();
        previous.dismiss();
    }

    protected void onDismissExclusiveDialog() {

    }

    public DialogFragment setListener(int id, IDialogFragmentListener listener) {

        this.listenerId = id;
        this.listener = listener;
        return this;
    }

/*
 * Copyright (c) Oct 6, 2013 ZYXW. All rights reserved.
 */

/**
 * The listener interface for receiving dialogFragment events.
 * The class that is interested in processing a dialogFragment
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>setListener</code> method. When
 * the dialogFragment event occurs, that object's appropriate
 * method is invoked.
 *
 * @author yumenosuke-k
 * @category Listener
 * @since Oct 6, 2013
 * @see #onEvent(int, int)
 */

    /**
     * Created based on http://qiita.com/GeneralD/items/89278fc0f5513abe0bb3
     * Set in DialogFragment class
     * by abe on 2014/11/17.
     */
    public interface IDialogFragmentListener {

        public static final int ON_DISMISS = 0;
        public static final int ON_CANCEL = 1;
        public static final int ON_POSITIVE_BUTTON_CLICKED = 2;
        public static final int ON_NEGATIVE_BUTTON_CLICKED = 3;
        public static final int ON_NEUTRAL_BUTTON_CLICKED = 4;
        public static final int ON_CLOSE_BUTTON_CLICKED = 5;

        /**
         * On event.
         *
         * @param id    listener id
         * @param event event is either of the following. {@link #ON_DISMISS} {@link #ON_CANCEL},
         *              {@link #ON_POSITIVE_BUTTON_CLICKED}, {@link #ON_NEGATIVE_BUTTON_CLICKED},
         *              {@link #ON_CLOSE_BUTTON_CLICKED}.
         */
        void onEvent(int id, int event);
    }
}

