package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.activities.BookListActivity;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.event.PhotoSavedEvent;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.util.BusHolder;
import jp.caliconography.one_liners.util.Utils;

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
    private Bitmap mPhotoBitmap;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this.getActivity(), new Intent(this.getActivity(), BookListActivity.class));
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

            return true;
        }
        return super.onOptionsItemSelected(item);
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

        BusHolder.get().register(this);
        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        mTxtTitle.setText(review.getTitle());
        mTxtAuthor.setText(review.getAuthor());
        ParseFile photoFile = review.getPhotoFile();
        if (photoFile != null) {
            mBookPhoto.setParseFile(photoFile);
            mBookPhoto.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    mBookPhoto.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);

        super.onPause();
    }

    @OnClick(R.id.book_photo)
    void onClickBookPhoto() {

        Fragment photoDetailFragment = new PhotoDetailFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.book_detail_container, photoDetailFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @OnClick(R.id.btn_search_book)
    void onClickSearchBook() {

        Fragment bookSearchResultListFragment = new BookSearchResultListFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.book_detail_container, bookSearchResultListFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @Subscribe
    public void onPhotoSaved(PhotoSavedEvent event) {

        final Review review = new Review();
        review.setTitle(mTxtTitle.getText().toString());
        review.setAuthor(mTxtAuthor.getText().toString());
        review.setPhotoFile(event.getFile());
        review.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
        Activity bookDetailActivity = getActivity();
        bookDetailActivity.setResult(Activity.RESULT_OK);
        bookDetailActivity.finish();

    }
}