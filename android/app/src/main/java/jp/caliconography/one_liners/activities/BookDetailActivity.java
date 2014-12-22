package jp.caliconography.one_liners.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.fragments.BookDetailFragment;
import jp.caliconography.one_liners.model.parseobject.Review;


/**
 * An activity representing a single book detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link jp.caliconography.one_liners.fragments.BookDetailFragment}.
 */
public class BookDetailActivity extends ActionBarActivity {

    private Review mReview = new Review();

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.inject(this);

        ((ActionBarActivity) this).setSupportActionBar(mToolBar);
        mToolBar.setTitle(getResources().getString(R.string.title_book_detail));

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            arguments.putString(BookDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(BookDetailFragment.ARG_ITEM_ID));
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }

        if (getIntent().getStringExtra(Review.KEY_OBJECT_ID) != null) {
            mReview = ParseObject.createWithoutData(Review.class, getIntent().getStringExtra(Review.KEY_OBJECT_ID));
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.book_detail, menu);
//        return true;
//    }

    public Review getCurrentReview() {
        return mReview;
    }

    public void setCurrentReview(Review review) {
        this.mReview = review;
    }
}
