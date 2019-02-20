package ahmed.example.com.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ahmed.example.com.bakingapp.Recipes.RecipesActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Created by root on 23/09/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipesDetailsTest {
    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class, false, true);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }
    @Test
    public void clickOnRecyclerViewItem_opensRecipeStepActivity() {

        onView(withId(R.id.recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onData(anything()).inAdapterView(withId(R.id.steps_list)).atPosition(0).perform(click());

    }


}
