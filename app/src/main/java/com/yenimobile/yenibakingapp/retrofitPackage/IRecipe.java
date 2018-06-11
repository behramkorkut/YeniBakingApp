package com.yenimobile.yenibakingapp.retrofitPackage;

import com.yenimobile.yenibakingapp.dataPAckage.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRecipe {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}