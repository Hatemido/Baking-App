package ahmed.example.com.bakingapp.Steps;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import ahmed.example.com.bakingapp.Models.Recipe;
import ahmed.example.com.bakingapp.R;

public class StepsActivity extends AppCompatActivity implements
                                                     StepsListFragment.OnFragmentInteractionListener {

    public static final String STEPS = "STEPS";

    boolean isTablet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (findViewById(R.id.details_fragment) != null) {
            isTablet = true;
        }
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            Log.e("in", "----");
        }
        Log.e("in2", "----");
        if (savedInstanceState == null) {
            //set List Fragment in two cases
            StepsListFragment stepsListFragment = new StepsListFragment();
            Recipe recipe = getIntent().getExtras().getParcelable(STEPS);
            stepsListFragment.setRecipeList(recipe);
            changeListFragment(stepsListFragment);
            if (isTablet) {
                //set details fragment too if table
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setData(recipe, 0, isTablet);
                changeDetailsFragment(detailsFragment);
            }
        }


    }


    void changeListFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                                   .addToBackStack("MYSTACK")
                                   .replace(R.id.list_fragment, fragment)
                                   .commit();
    }

    void changeDetailsFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.details_fragment, fragment)
                                   .commit();
    }


    @Override
    public void onFragmentInteraction(Recipe recipe, int position) {
        if (isTablet) {
            // if tablet inflate details fragment in second fragment
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setData(recipe, position, isTablet);
            changeDetailsFragment(detailsFragment);

        } else {
            //if not inflate details fragment  in first fragment
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setData(recipe, position, isTablet);
            changeListFragment(detailsFragment);
        }

    }


    @Override
    public void onBackPressed() {
        // empty the stack to the first fragment and if it first fragment  call super
        if (getSupportFragmentManager().getBackStackEntryCount() > 1 && !isTablet) {
            getSupportFragmentManager().popBackStack();

        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
