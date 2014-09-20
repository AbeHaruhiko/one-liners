package jp.caliconography.one_liners;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import it.gmariotti.cardslib.demo.extras.fragment.StaggeredGridFragment;


/**
 * An activity representing a list of books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BookListFragment} and the item details
 * (if present) is a {@link BookDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link BookListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class BookListActivity extends Activity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_list);

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


        setTitle("SGV");

        final FragmentManager fm = getFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
//            final BookListFragment fragment = new BookListFragment();
            final StaggeredGridFragment fragment = new StaggeredGridFragment();
            fm.beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

}
