package com.example.lenovo.recipes;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.example.lenovo.recipes.adapterUtile.RecipesNameAdapter;
import com.example.lenovo.recipes.fragmentsUtile.IngredientFragment;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;

import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.AllOf.allOf;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Instrumentation.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    // test to demonstrate Espresso intent test
    @Rule
    public IntentsTestRule<MainRecipesActivity> mActivityRule =
            new IntentsTestRule<>(MainRecipesActivity.class);


IngredientFragment fragment = new IngredientFragment();
private IdlingResource mIdlingResource;
    @Before
    public void stubAllInternalIntents() {
        mIdlingResource = mActivityRule.getActivity().getIdleResource();
        IdlingRegistry.getInstance().register(mIdlingResource);}
        @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void onRecipesListItemClick() {

        onView(ViewMatchers.withId(R.id.recipes_name_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.recipes_detail_container,fragment).commit();
        intended(allOf(
                hasComponent(DetailRecipesActivity.class.getName()),
                hasExtra("recipesName", "Nutella Pie"),
                hasExtra("recipesId",0)));
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.recipes_detail_container,fragment).commit();

            onView(withId(R.id.show_ingredient_list)).perform(click());
            onView(withId(R.id.ingredient_btn_on)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));



            onView(withId(R.id.show_setps)).perform(click());
            onView(withId(R.id.direction_btn_on)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }



}


