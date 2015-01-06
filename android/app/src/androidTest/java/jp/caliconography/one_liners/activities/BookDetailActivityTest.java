package jp.caliconography.one_liners.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.viewaction.InputTextAction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class BookDetailActivityTest extends ActivityInstrumentationTestCase2<BookDetailActivity> {

    public static final String STRING_999_TO_BE_TYPED = "999";
    public static final String STRING_OVER_140_CHARS = "１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n１２３４５６７８９０\n";
    private BookDetailActivity mActivity;

    public BookDetailActivityTest() {
        super(BookDetailActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        Intent detailIntent = new Intent(getActivity(), BookDetailActivity.class);
//        detailIntent.putExtra(Review.KEY_OBJECT_ID, "xjTbn1JchZ");
//        setActivityIntent(detailIntent);
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
    public void ページに数字を入力するとActivityのReviewオブジェクトに保存される() throws Exception {
        onView(withId(R.id.txt_page))
                .perform(typeText(STRING_999_TO_BE_TYPED), closeSoftKeyboard());
        assertThat(mActivity.getCurrentReview().getPage(), is(STRING_999_TO_BE_TYPED));
    }

    @Test
    public void レビューに入力するとActivityのReviewオブジェクトに保存される() throws Exception {
        onView(withId(R.id.txt_review))
                .perform(typeText(STRING_999_TO_BE_TYPED), closeSoftKeyboard());
        assertThat(mActivity.getCurrentReview().getReviewText(), is(STRING_999_TO_BE_TYPED));
    }

    @Test
    public void レビューに140文字以上入力すると140文字分ActivityのReviewオブジェクトに保存される() throws Exception {
        onView(withId(R.id.txt_review))
                .perform(new InputTextAction(STRING_OVER_140_CHARS), closeSoftKeyboard());
        assertThat(mActivity.getCurrentReview().getReviewText(), is(STRING_OVER_140_CHARS.substring(0, 140)));
    }
}