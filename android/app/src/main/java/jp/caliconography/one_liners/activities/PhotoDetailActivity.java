package jp.caliconography.one_liners.activities;

import android.app.Activity;
import android.os.Bundle;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.fragments.PhotoDetailFragment;


/**
 * An activity representing a single book detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link jp.caliconography.one_liners.fragments.BookDetailFragment}.
 */
public class PhotoDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PhotoDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PhotoDetailFragment.ARG_ITEM_ID));
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.photo_detail_container, fragment)
                    .commit();
        }
    }
}
