package jp.caliconography.one_liners.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.fragments.BookSearchResultListFragment;


/**
 * An activity representing a list of books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BookSearchResultListActivity} and the item details
 * (if present) is a {@link jp.caliconography.one_liners.fragments.BookDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BookSearchResultListActivity.Callbacks} interface
 * to listen for item selections.
 */
public class BookSearchResultListActivity extends Activity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_search_result_list);

//        if (findViewById(R.id.book_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-large and
//            // res/values-sw600dp). If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//
//            // In two-pane mode, list items should be given the
//            // 'activated' state when touched.
//            ((BookListFragment) getFragmentManager()
//                    .findFragmentById(R.id.book_list))
//                    .setActivateOnItemClick(true);
//        }

        // TODO: If exposing deep links into your app, handle intents here.

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("SGV");

        final FragmentManager fm = getFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
//            final BookListFragment fragment = new BookListFragment();
            final BookSearchResultListFragment fragment = new BookSearchResultListFragment();
            fm.beginTransaction().add(android.R.id.content, fragment).commit();
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
            NavUtils.navigateUpTo(this, new Intent(this, BookDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 2014/09/20 searchViewを追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_search, menu);

        // SearchViewを取得する
        MenuItem searchItem = menu.findItem(R.id.search_view);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.book_search_query_hint));
        searchView.setIconified(false);
//        searchView.setOnQueryTextListener(this);


        return true;
    }
}
