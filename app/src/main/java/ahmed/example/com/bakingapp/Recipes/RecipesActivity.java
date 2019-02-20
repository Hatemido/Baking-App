package ahmed.example.com.bakingapp.Recipes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ahmed.example.com.bakingapp.Widget.IngredientsProvider;
import ahmed.example.com.bakingapp.Models.Ingredient;
import ahmed.example.com.bakingapp.Models.Recipe;
import ahmed.example.com.bakingapp.R;
import ahmed.example.com.bakingapp.Steps.StepsActivity;
import ahmed.example.com.bakingapp.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipesActivity extends AppCompatActivity implements
                                                       RecipesAdapter.onItemClickListener {

    public static final String RECIPES = "recipes";
    public static final String SHARED_NAME = "shared";
    public static final String RECIPE_NAME = "name";
    @BindView(R.id.recipes_list)
    RecyclerView recipesRecyclerView;
    List<Recipe> recipes = new ArrayList<>();
    @Nullable
    private RecipesIdlingResource idlingResource = new RecipesIdlingResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPES);

            setRecyclerView(recipes);
        } else {
            getRecipes();
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES, (ArrayList<? extends Parcelable>) recipes);

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipesIdlingResource();
        }
        return idlingResource;
    }

    /**
     * get Recipes from Url and Display it in RecyclerView
     */
    public void getRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (idlingResource != null) idlingResource.setIdleState(false);


        retrofit.create(RecipesClient.class).getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response
            ) {
                if (response.code() == 200) {
                    recipes = response.body();

                    setRecyclerView(recipes);
                    if (idlingResource != null) idlingResource.setIdleState(true);

                }


            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipesActivity.this,
                               "Check internet Connection ",
                               Toast.LENGTH_SHORT
                ).show();

            }
        });
    }

    private void setRecyclerView(List<Recipe> recipes) {
        RecipesAdapter adapter = new RecipesAdapter(RecipesActivity.this,
                                                    recipes,
                                                    RecipesActivity.this
        );
        recipesRecyclerView.setAdapter(adapter);
        if (!Utility.isAbove7inch(RecipesActivity.this)) {
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(RecipesActivity.this,
                                                                         LinearLayoutManager.VERTICAL,
                                                                         false
            ));
        } else {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(RecipesActivity.this,
                                                                       Utility.calculateNoOfColumns(
                                                                               RecipesActivity.this,
                                                                               250
                                                                       )
            ));

        }
    }

    @Override
    public void onItemClickActionListener(int position) {
        Intent intent = new Intent(this, StepsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(StepsActivity.STEPS, recipes.get(position));
        intent.putExtras(bundle);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putString(RECIPE_NAME, recipes.get(position).getName()).apply();
        int deleted = getContentResolver().delete(IngredientsProvider.BASE_URI, null, null);

        for (Ingredient ingredient : recipes.get(position).getIngredients()) {
            ContentValues values = new ContentValues();
            values.put(IngredientsProvider.INGREDIENTS_QUANTITY, ingredient.getQuantity());
            values.put(IngredientsProvider.INGREDIENT, ingredient.getIngredient());
            values.put(IngredientsProvider.INGREDIENTS_MEASURE, ingredient.getMeasure());
            Uri uri = getContentResolver().insert(IngredientsProvider.BASE_URI, values);
            Log.e("----", uri.toString());

        }
        startActivity(intent);


    }

    interface RecipesClient {

        @GET("baking.json")
        Call<List<Recipe>> getRecipes();
    }
}

