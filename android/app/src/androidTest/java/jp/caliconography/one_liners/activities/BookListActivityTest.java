package jp.caliconography.one_liners.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.caliconography.one_liners.TestUtils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BookListActivityTest extends ActivityInstrumentationTestCase2<BookListActivity> {

    private static final String TAG = BookListActivityTest.class.getSimpleName();

    private BookListActivity mActivity;

    public BookListActivityTest() {
        super(BookListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        TestUtils.toggleAnimationEnable(mActivity, TAG, false);
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

}