package ahmed.example.com.bakingapp.Steps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ahmed.example.com.bakingapp.Models.Ingredient;
import ahmed.example.com.bakingapp.Models.Recipe;
import ahmed.example.com.bakingapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StepsListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    public static final String RECIPE = "recipeList";
    Recipe recipe;

    public StepsListFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.steps_list)
    ListView stepsList;
    @BindView(R.id.ingredients)
    TextView ingredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState!=null){
            recipe=savedInstanceState.getParcelable(RECIPE);

        }
        setListView();
        setIngredients();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE,recipe);
    }

    private void setIngredients() {
        String ingredientString ="" ;
        for(Ingredient ingredient:recipe.getIngredients()){
            ingredientString+="-"+ingredient.getIngredient()+"("+ingredient.getQuantity()+ingredient.getMeasure()+")\n";
        }
        ingredients.setText(ingredientString);
    }

    private void setListView() {
        stepsList.setAdapter(new StepsAdapter(getActivity(),recipe.getSteps()));
        stepsList.setOnItemClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                               + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRecipeList(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListener.onFragmentInteraction(recipe,i);

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Recipe recipe,int position);
    }
}