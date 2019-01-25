package com.example.lenovo.recipes;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainRecipesActivityTest1 {

    @Rule
    public ActivityTestRule<MainRecipesActivity> mActivityTestRule = new ActivityTestRule<>(MainRecipesActivity.class);
    private IdlingResource mIdlingResource;
    @Before
    public void stubAllInternalIntents() {
        mIdlingResource = mActivityTestRule.getActivity().getIdleResource();
        IdlingRegistry.getInstance().register(mIdlingResource);}
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
    @Test
    public void mainRecipesActivityTest1() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.recipes_name), withText("Brownies"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipes_name_list),
                                        1),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredient), withText("Bittersweet chocolate (60-70% cacao)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredient_list),
                                        0),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("Bittersweet chocolate (60-70% cacao)")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
