package jp.caliconography.one_liners.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.fragments.BookListFragment;


/**
 * An activity representing a list of books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BookListFragment} and the item details
 * (if present) is a {@link jp.caliconography.one_liners.fragments.BookDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BookListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class BookListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // support libraryでandroid.R.id.contentを使うときのクラッシュに対応
        setContentView(R.layout.dummy_framelayout);

        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentById(android.R.id.content) == null) {
            final BookListFragment fragment = new BookListFragment();
            fm.beginTransaction().add(R.id.dummy, fragment).commit();
//            fm.beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
