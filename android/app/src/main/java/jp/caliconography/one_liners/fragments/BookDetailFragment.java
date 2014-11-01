package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookListActivity;
import jp.caliconography.one_liners.activities.BookSearchResultListActivity;
import jp.caliconography.one_liners.activities.PhotoDetailActivity;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.event.PhotoBitmapGottenEvent;
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

    @InjectView(R.id.progressContainer)
    View mProgressContainer;
    @InjectView(R.id.book_photo)
    ImageView mBookPhoto;
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

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQ_CODE_BOOK_SEARCH && resultCode == Activity.RESULT_OK) {

            mTxtTitle.setText(intent.getCharSequenceExtra("title"));
            mTxtAuthor.setText(intent.getCharSequenceExtra("author"));

        } else if (requestCode == REQ_CODE_PHOTO_DETAIL && resultCode == Activity.RESULT_OK) {

//            Bitmap photoBitmap = (Bitmap) intent.getParcelableExtra(INTENT_KEY_PAINTED_PHOTO);
//            mBookPhoto.setBackground(new BitmapDrawable(getActivity().getResources(), photoBitmap));
//            Review review = ParseObject.createWithoutData(Review.class, intent.getSerializableExtra("reviewId").toString());
            File file = null;
            mPhotoBitmap = null;
            try {
                file = new File(intent.getStringExtra("photoFilePath"));
                mPhotoBitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
//            mBookPhoto.setImageBitmap(bitmap);
//            mBookPhoto.setText("");
//            mBookPhoto.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            setBackgroundDrawable(mBookPhoto, new BitmapDrawable(getActivity().getResources(), bitmap));
            mBookPhoto.setImageBitmap(mPhotoBitmap);
        }
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

            showProgressBar();

            // TODO: 入力チェック

            final ParseFile file = new ParseFile("photo.png", Utils.bitmapToByte(mPhotoBitmap));
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        BusHolder.get().post(new PhotoSavedEvent(file));
                    } else {
                        hideProgressBar();
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

    public void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        BusHolder.get().register(this);
    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);

        super.onPause();
    }

    @OnClick(R.id.book_photo)
    void onClickBookPhoto() {
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        startActivityForResult(intent, REQ_CODE_PHOTO_DETAIL);
    }

    @OnClick(R.id.btn_search_book)
    void onClickSearchBook() {
        Intent intent = new Intent(getActivity(), BookSearchResultListActivity.class);
        startActivityForResult(intent, REQ_CODE_BOOK_SEARCH);
    }

    static Intent createSearchResultIntent(String title, String author) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        return intent;
    }

    static Intent createPaintedPhotoIntent(Bitmap paintedPhoto) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_PAINTED_PHOTO, paintedPhoto);
        return intent;
    }

    static void putPaintedPhotoIntent(Intent intent, Bitmap paintedPhoto) {
        intent.putExtra(INTENT_KEY_PAINTED_PHOTO, paintedPhoto);
    }

    @Subscribe
    public void onPhotoBitmapGotten(PhotoBitmapGottenEvent event) {
        // TODO API levelごとの対応
        mBookPhoto.setBackground(new BitmapDrawable(getActivity().getResources(), event.getBitmap()));
    }

    @Subscribe
    public void onPhotoSaved(PhotoSavedEvent event) {

        final Review review = new Review();
        review.setTitle(mTxtTitle.getText().toString());
        review.setAuthor(mTxtAuthor.getText().toString());
        review.setPhotoFile(event.getFile());
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                Activity bookDetailActivity = getActivity();
                bookDetailActivity.setResult(Activity.RESULT_OK);
                bookDetailActivity.finish();
            }
        });

    }
}