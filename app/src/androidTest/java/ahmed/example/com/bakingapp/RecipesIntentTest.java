package ahmed.example.com.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

import ahmed.example.com.bakingapp.Recipes.RecipesActivity;
import ahmed.example.com.bakingapp.Steps.StepsActivity;

import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;

/**
 * Created by root on 23/09/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipesIntentTest {
    @Rule
    public IntentsTestRule<RecipesActivity> mIntentsTestRule =
            new IntentsTestRule<>(RecipesActivity.class);

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {

//        // Stub all external intents
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }

    @Test
    public void clickRecipeListItem_viewItemHasIntent() {
        onView(withId(R.id.recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(
                hasExtraWithKey(StepsActivity.STEPS)
        );
    }
}
