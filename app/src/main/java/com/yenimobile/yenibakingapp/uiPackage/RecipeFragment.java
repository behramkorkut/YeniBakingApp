package com.yenimobile.yenibakingapp.uiPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yenimobile.yenibakingapp.IdlingResource.SimpleIdlingResource;
import com.yenimobile.yenibakingapp.MainActivity;
import com.yenimobile.yenibakingapp.R;
import com.yenimobile.yenibakingapp.adaptersPackage.RecipeAdapter;
import com.yenimobile.yenibakingapp.dataPAckage.Recipe;
import com.yenimobile.yenibakingapp.retrofitPackage.IRecipe;
import com.yenimobile.yenibakingapp.retrofitPackage.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yenimobile.yenibakingapp.uiPackage.RecipeDetailActivity.ALL_RECIPES;

public class RecipeFragment extends Fragment {

    public RecipeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;

        View rootView = inflater.inflate(R.layout.recipe_fragment_body_part, container, false);

        recyclerView=(RecyclerView)  rootView.findViewById(R.id.recipe_recycler);
        RecipeAdapter recipesAdapter =new RecipeAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(recipesAdapter);



        if (rootView.getTag()!=null && rootView.getTag().equals("phone-land")){
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),4);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        IRecipe iRecipe = RetrofitBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipeData(recipes,getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
            }
        });

        return rootView;
    }


}


