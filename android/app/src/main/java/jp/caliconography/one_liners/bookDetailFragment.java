package jp.caliconography.one_liners;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private WebView mWebView;
    private Button mBtnLoadImage;
    private Uri mPictureUri;
    private int inSampleSize;
    private Handler mHandler = new Handler();
    private String[] imagedata;
    private int maxDataLength = 100;

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

        ImageButton searchButton = (ImageButton) rootView.findViewById(R.id.btn_search_book);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView searchView = (SearchView) getActivity().findViewById(R.id.searchView);
                searchView.setIconified(false);
            }
        });

        return rootView;
    }
}