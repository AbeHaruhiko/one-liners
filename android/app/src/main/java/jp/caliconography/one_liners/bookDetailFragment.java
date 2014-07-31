package jp.caliconography.one_liners;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import jp.caliconography.one_liners.dummy.DummyContent;
import us.costan.chrome.ChromeView;

/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link bookListActivity}
 * in two-pane mode (on tablets) or a {@link bookDetailActivity}
 * on handsets.
 */
public class bookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private WebView mWebView;
    private Button mBtnLoadImage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public bookDetailFragment() {
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

        mBtnLoadImage = (Button) rootView.findViewById(R.id.loadImage);
        mBtnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCanvasBackground();
            }
        });

        mWebView = (WebView) rootView.findViewById(R.id.webView);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");
        mWebView.loadUrl("http://one-liners.parseapp.com");

        return rootView;
    }

    private void setCanvasBackground() {
        String script = "javascript:setCanvasBackground('%s');";
        String image = "/9j/4AAQSkZJRgABAQEASABIAAD/4ge4SUNDX1BST0ZJTEUAAQEAAAeoYXBwbAIgAABtbnRyUkdCIFhZWiAH2QACABkACwAaAAthY3NwQVBQTAAAAABhcHBsAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWFwcGwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAtkZXNjAAABCAAAAG9kc2NtAAABeAAABWxjcHJ0AAAG5AAAADh3dHB0AAAHHAAAABRyWFlaAAAHMAAAABRnWFlaAAAHRAAAABRiWFlaAAAHWAAAABRyVFJDAAAHbAAAAA5jaGFkAAAHfAAAACxiVFJDAAAHbAAAAA5nVFJDAAAHbAAAAA5kZXNjAAAAAAAAABRHZW5lcmljIFJHQiBQcm9maWxlAAAAAAAAAAAAAAAUR2VuZXJpYyBSR0IgUHJvZmlsZQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAbWx1YwAAAAAAAAAeAAAADHNrU0sAAAAoAAABeGhySFIAAAAoAAABoGNhRVMAAAAkAAAByHB0QlIAAAAmAAAB7HVrVUEAAAAqAAACEmZyRlUAAAAoAAACPHpoVFcAAAAWAAACZGl0SVQAAAAoAAACem5iTk8AAAAmAAAComtvS1IAAAAWAAACyGNzQ1oAAAAiAAAC3mhlSUwAAAAeAAADAGRlREUAAAAsAAADHmh1SFUAAAAoAAADSnN2U0UAAAAmAAAConpoQ04AAAAWAAADcmphSlAAAAAaAAADiHJvUk8AAAAkAAADomVsR1IAAAAiAAADxnB0UE8AAAAmAAAD6G5sTkwAAAAoAAAEDmVzRVMAAAAmAAAD6HRoVEgAAAAkAAAENnRyVFIAAAAiAAAEWmZpRkkAAAAoAAAEfHBsUEwAAAAsAAAEpHJ1UlUAAAAiAAAE0GFyRUcAAAAmAAAE8mVuVVMAAAAmAAAFGGRhREsAAAAuAAAFPgBWAWEAZQBvAGIAZQBjAG4A/QAgAFIARwBCACAAcAByAG8AZgBpAGwARwBlAG4AZQByAGkBDQBrAGkAIABSAEcAQgAgAHAAcgBvAGYAaQBsAFAAZQByAGYAaQBsACAAUgBHAEIAIABnAGUAbgDoAHIAaQBjAFAAZQByAGYAaQBsACAAUgBHAEIAIABHAGUAbgDpAHIAaQBjAG8EFwQwBDMEMAQ7BEwEPQQ4BDkAIAQ/BEAEPgREBDAEOQQ7ACAAUgBHAEIAUAByAG8AZgBpAGwAIABnAOkAbgDpAHIAaQBxAHUAZQAgAFIAVgBCkBp1KAAgAFIARwBCACCCcl9pY8+P8ABQAHIAbwBmAGkAbABvACAAUgBHAEIAIABnAGUAbgBlAHIAaQBjAG8ARwBlAG4AZQByAGkAcwBrACAAUgBHAEIALQBwAHIAbwBmAGkAbMd8vBgAIABSAEcAQgAg1QS4XNMMx3wATwBiAGUAYwBuAP0AIABSAEcAQgAgAHAAcgBvAGYAaQBsBeQF6AXVBeQF2QXcACAAUgBHAEIAIAXbBdwF3AXZAEEAbABsAGcAZQBtAGUAaQBuAGUAcwAgAFIARwBCAC0AUAByAG8AZgBpAGwAwQBsAHQAYQBsAOEAbgBvAHMAIABSAEcAQgAgAHAAcgBvAGYAaQBsZm6QGgAgAFIARwBCACBjz4/wZYdO9k4AgiwAIABSAEcAQgAgMNcw7TDVMKEwpDDrAFAAcgBvAGYAaQBsACAAUgBHAEIAIABnAGUAbgBlAHIAaQBjA5MDtQO9A7kDugPMACADwAPBA78DxgOvA7sAIABSAEcAQgBQAGUAcgBmAGkAbAAgAFIARwBCACAAZwBlAG4A6QByAGkAYwBvAEEAbABnAGUAbQBlAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGUAbA5CDhsOIw5EDh8OJQ5MACAAUgBHAEIAIA4XDjEOSA4nDkQOGwBHAGUAbgBlAGwAIABSAEcAQgAgAFAAcgBvAGYAaQBsAGkAWQBsAGUAaQBuAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGkAbABpAFUAbgBpAHcAZQByAHMAYQBsAG4AeQAgAHAAcgBvAGYAaQBsACAAUgBHAEIEHgQxBEkEOAQ5ACAEPwRABD4ERAQ4BDsETAAgAFIARwBCBkUGRAZBACAGKgY5BjEGSgZBACAAUgBHAEIAIAYnBkQGOQYnBkUARwBlAG4AZQByAGkAYwAgAFIARwBCACAAUAByAG8AZgBpAGwAZQBHAGUAbgBlAHIAZQBsACAAUgBHAEIALQBiAGUAcwBrAHIAaQB2AGUAbABz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";
        mWebView.loadUrl(String.format(script, image));
    }
}
