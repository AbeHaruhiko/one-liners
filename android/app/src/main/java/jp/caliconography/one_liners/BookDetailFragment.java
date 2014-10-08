package jp.caliconography.one_liners;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.caliconography.one_liners.dummy.DummyContent;

/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {

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

    @InjectView(R.id.txt_title)
    TextView mTxtTitle;
    @InjectView(R.id.txt_author)
    TextView mTxtAuthor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == REQ_CODE_BOOK_SEARCH && resultCode == Activity.RESULT_OK){
            mTxtTitle.setText(intent.getCharSequenceExtra("title"));
            mTxtAuthor.setText(intent.getCharSequenceExtra("author"));
        }
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

}