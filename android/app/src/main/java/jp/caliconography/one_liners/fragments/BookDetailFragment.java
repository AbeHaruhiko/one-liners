package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.activities.BookListActivity;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.util.Utils;
import jp.caliconography.one_liners.widget.DynamicHeightPicassoImageView;

/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link jp.caliconography.one_liners.activities.BookListActivity}
 * in two-pane mode (on tablets) or a {@link jp.caliconography.one_liners.activities.BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {

    public static final String INTENT_KEY_PAINTED_PHOTO = "paintedPhoto";

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final int IMAGE_CHOOSER_RESULTCODE = 0;
    public static final int IMG_MAX_LENGTH = 320;
    static final String TAG = BookDetailFragment.class.getSimpleName();
    private static final int REQ_CODE_BOOK_SEARCH = 0;
    private static final int REQ_CODE_PHOTO_DETAIL = 1;
    public static final int DELETE_DIALOG_LISTENER_ID = 0;
    public static final int SHARE_DIALOG_LISTENER_ID = 0;
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    @InjectView(R.id.progress_container)
    View mProgressContainer;
    @InjectView(R.id.book_photo)
    ParseImageView mBookPhoto;
    @InjectView(R.id.txt_title)
    TextView mTxtTitle;
    @InjectView(R.id.txt_author)
    TextView mTxtAuthor;
    @InjectView(R.id.txt_review)
    TextView mTextReview;
    @InjectView(R.id.book_thumbnail)
    DynamicHeightPicassoImageView mThumbnail;
    @InjectView(R.id.quote)
    ImageView mQuoteMark;
    @InjectView(R.id.swc_share_scope)
    Switch mShareScope;
    @InjectView(R.id.btn_search_book)
    ImageButton mSearchBookButton;

    private Bitmap mPhotoBitmap;
    private MenuItem mMenuDelete;
    private MenuItem mMenuSave;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FragmentでMenuを表示する為に必要
        this.setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);

        ButterKnife.inject(this, rootView);
        mBookPhoto.setPlaceholder(getActivity().getResources().getDrawable(R.drawable.photo_placeholder));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.book_detail, menu);

        mMenuDelete = menu.findItem(R.id.delete_book);
        mMenuSave = menu.findItem(R.id.save_book);
        setMenuItemVisibility();
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
//
//        mMenuDelete = menu.findItem(R.id.delete_book);
//        setMenuItemVisibility(review);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            final Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
            if (review.isDirty()) {

                for (String key : review.keySet()) {
                    if (review.isDirty(key)) {
                        Log.d(TAG, "dirty: " + key);
                    }
                }
                // 変更されている場合

                DialogFragment
                        .newInstance(true)
                        .setTitle(R.string.dialog_title_confirm)
                        .setMessage(String.format(getResources().getString(R.string.dialog_confirm_message_review_is_dirty), getResources().getString(R.string.dialog_negative_button_text)))
                        .setPositiveButtonText(R.string.dialog_posigive_button_text_save)
                        .setNegativeButtonText(R.string.dialog_negative_button_text_dont_save)
                        .setListener(DELETE_DIALOG_LISTENER_ID, new DialogFragment.IDialogFragmentListener() {

                            @Override
                            public void onEvent(int id, int event) {
                                switch (event) {

                                    case DialogFragment.IDialogFragmentListener.ON_POSITIVE_BUTTON_CLICKED:
                                        saveReviewAndReturnToBookList(review);
                                        break;

                                    case DialogFragment.IDialogFragmentListener.ON_NEGATIVE_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_NEUTRAL_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_CLOSE_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_CANCEL:
                                        NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), BookListActivity.class));
                                        break;
                                }
                            }
                        })
                        .show(getActivity().getSupportFragmentManager());
            } else {
                NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), BookListActivity.class));
            }
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            NavUtils.navigateUpTo(this.getActivity(), new Intent(this.getActivity(), BookListActivity.class));
            return true;
        } else if (id == R.id.save_book) {

            if (!Utils.isOnline(getActivity())) {
                // TODO: エラーメッセージ表示が仮
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Error saving: ネットワークに接続できません。",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            showProgressBar();

            // TODO: 入力チェック

            Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
            saveReviewAndReturnToBookList(review);

            return true;
        } else if (id == R.id.delete_book) {

            DialogFragment
                    .newInstance(true)
                    .setTitle(R.string.dialog_title_confirm)
                    .setMessage(R.string.dialog_confirm_message_delete)
                    .setPositiveButtonText(R.string.dialog_posigive_button_text)
                    .setNegativeButtonText(R.string.dialog_negative_button_text)
                    .setListener(DELETE_DIALOG_LISTENER_ID, new DialogFragment.IDialogFragmentListener() {

                        @Override
                        public void onEvent(int id, int event) {
                            switch (event) {

                                case DialogFragment.IDialogFragmentListener.ON_POSITIVE_BUTTON_CLICKED:
                                    deleteReview();
                                    break;

                                case DialogFragment.IDialogFragmentListener.ON_NEGATIVE_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_NEUTRAL_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_CLOSE_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_CANCEL:
                                    break;
                            }
                        }
                    })
                    .show(getFragmentManager());

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void saveReviewAndReturnToBookList(Review review) {

        // 入力したreviewを保存
        review.setReviewText(mTextReview.getText().toString());
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Activity bookDetailActivity = getActivity();
                    bookDetailActivity.setResult(Activity.RESULT_OK);
                    bookDetailActivity.finish();
                } else {
                    // TODO: エラーメッセージ表示が仮
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteReview() {
        showProgressBar();
        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        review.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    ((BookDetailActivity) getActivity()).setCurrentReview(new Review());
                    resetReview();
                    hideProgressBar();
                } else {
                    // TODO: エラーメッセージ表示が仮
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Error deleting: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideProgressBar() {
        mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        mProgressContainer.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        resetReview();

    }

    private void resetReview() {
        final Review review = ((BookDetailActivity) getActivity()).getCurrentReview();

        showProgressBar();

        review.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                hideProgressBar();

                if (!review.getACL().getWriteAccess(ParseUser.getCurrentUser())) {
                    // 書き込み権限がない

                    mSearchBookButton.setEnabled(false);
                    mShareScope.setEnabled(false);
                    mTextReview.setEnabled(false);
                }

                mTxtTitle.setText(review.getTitle());
                mTxtAuthor.setText(review.getAuthor());
                mTextReview.setText(review.getReviewText());
                mShareScope.setChecked(review.getShareScope().isPublic());
                mThumbnail.loadImage(review.getThumbnailUrl());

                ParseFile photoFile = review.getPhotoFile();
                mBookPhoto.setParseFile(photoFile);
                if (photoFile != null) {
                    mBookPhoto.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            mBookPhoto.setVisibility(View.VISIBLE);
                            mMenuDelete.setVisible(true);
                            setQuoteMarkPosition(review);
                        }
                    });
                }

                setMenuItemVisibility();
            }
        });

    }

    private void setQuoteMarkPosition(Review review) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mQuoteMark.getLayoutParams();
        if (review.getQuoteMarkPosition() == Review.QuoteMarkPosition.LEFT) {
            putQuoteMarkLeft(review, layoutParams);
        } else if (review.getQuoteMarkPosition() == Review.QuoteMarkPosition.RIGHT) {
            putQuoteMarkRight(review, layoutParams);
        }
        mQuoteMark.setLayoutParams(layoutParams);
        mQuoteMark.setVisibility(View.VISIBLE);
    }

    private void putQuoteMarkLeft(Review review, RelativeLayout.LayoutParams layoutParams) {
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.book_photo);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, 0);
    }

    private void putQuoteMarkRight(Review review, RelativeLayout.LayoutParams layoutParams) {
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.book_photo);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, 0);
    }

    private void setMenuItemVisibility() {
        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        if (!review.isDataAvailable()) {
            return;
        }
        if (mMenuDelete == null) {
            return;
        }
        if (review.isEmpty()) {
            mMenuDelete.setVisible(false);
            mMenuSave.setVisible(false);
        }
        if (review.getACL().getWriteAccess(ParseUser.getCurrentUser())) {
            mMenuDelete.setVisible(true);
            mMenuSave.setVisible(true);
        } else {
            mMenuDelete.setVisible(false);
            mMenuSave.setVisible(false);
        }
    }

    @OnClick(R.id.book_photo)
    void onClickBookPhoto() {

        Fragment photoDetailFragment = new PhotoDetailFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.book_detail_container, photoDetailFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @OnClick(R.id.btn_search_book)
    void onClickSearchBook() {

        Fragment bookSearchResultListFragment = new BookSearchResultListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.book_detail_container, bookSearchResultListFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @OnClick(R.id.move_to_rakuten)
    void onClickRakutenBtn() {
        if (((BookDetailActivity) getActivity()).getCurrentReview().getAffiliateUrl() != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(((BookDetailActivity) getActivity()).getCurrentReview().getAffiliateUrl())));
        }
    }

    @OnClick(R.id.swc_share_scope)
    void onClickShareScope(final Switch view) {

        final Review review = ((BookDetailActivity) getActivity()).getCurrentReview();

        if (view.isChecked()) {

            DialogFragment
                    .newInstance(true)
                    .setTitle(R.string.dialog_title_confirm)
                    .setMessage(R.string.dialog_confirm_message_share_public)
                    .setPositiveButtonText(R.string.dialog_posigive_button_text)
                    .setNegativeButtonText(R.string.dialog_negative_button_text)
                    .setListener(SHARE_DIALOG_LISTENER_ID, new DialogFragment.IDialogFragmentListener() {

                        @Override
                        public void onEvent(int id, int event) {
                            switch (event) {

                                case DialogFragment.IDialogFragmentListener.ON_POSITIVE_BUTTON_CLICKED:
//                                    review.setShareScope(Review.ShareScope.PUBLIC);
                                    ParseACL acl = new ParseACL();
                                    acl.setPublicReadAccess(view.isChecked());
                                    review.setACL(acl);

                                    break;

                                case DialogFragment.IDialogFragmentListener.ON_NEGATIVE_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_NEUTRAL_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_CLOSE_BUTTON_CLICKED:
                                case DialogFragment.IDialogFragmentListener.ON_CANCEL:
                                    view.setChecked(false);
                                    break;
                            }
                        }
                    })
                    .show(getFragmentManager());

        } else {
            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(view.isChecked());
            review.setACL(acl);
        }
    }

    @OnClick(R.id.quote)
    void onClickQuoteMark(final View view) {
        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (review.getQuoteMarkPosition() == Review.QuoteMarkPosition.LEFT) {
            // いま左にある
            putQuoteMarkRight(review, layoutParams);
            review.setQuoteMarkPosition(Review.QuoteMarkPosition.RIGHT);
        } else if (review.getQuoteMarkPosition() == Review.QuoteMarkPosition.RIGHT) {
            // いま右にある
            putQuoteMarkLeft(review, layoutParams);
            review.setQuoteMarkPosition(Review.QuoteMarkPosition.LEFT);
        }
        view.setLayoutParams(layoutParams);
    }

    @OnTextChanged(R.id.txt_review)
    void OnReviewTextChanged(CharSequence text) {

        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        review.setReviewText(text.toString());
    }
}