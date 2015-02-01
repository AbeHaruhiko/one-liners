package jp.caliconography.one_liners.viewaction;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.CoreMatchers.allOf;

public final class InputTextAction implements ViewAction {
    private final String mText;

    public InputTextAction(String text) {
        checkNotNull(text);
        mText = text;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(EditText.class));
    }

    @Override
    public void perform(UiController uiController, View view) {
        ((EditText) view).setText(mText);
    }

    @Override
    public String getDescription() {
        return "set text";
    }
}