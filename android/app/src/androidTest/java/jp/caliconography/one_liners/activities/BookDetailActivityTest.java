package jp.caliconography.one_liners.activities;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.caliconography.one_liners.model.parseobject.Review;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class BookDetailActivityTest extends ActivityInstrumentationTestCase2<BookDetailActivity> {

    private BookDetailActivity mActivity;

    public BookDetailActivityTest() {
        super(BookDetailActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Intent detailIntent = new Intent(getActivity(), BookDetailActivity.class);
        detailIntent.putExtra(Review.KEY_OBJECT_ID, "xjTbn1JchZ");
        setActivityIntent(detailIntent);
        mActivity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void checkPreconditions() {
        assertThat(mActivity, notNullValue());
        // Check that Instrumentation was correctly injected in setUp()
        assertThat(getInstrumentation(), notNullValue());
    }

    @Test
    public void testOnPageTextChanged() throws Exception {

    }
}