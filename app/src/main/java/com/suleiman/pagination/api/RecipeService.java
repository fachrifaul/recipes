package com.suleiman.pagination.api;


import com.suleiman.pagination.models.Recipe;
import com.suleiman.pagination.models.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public interface RecipeService {

    @GET("search?q=pancake")
    Call<Recipes> getRecipes(
            @Query("key") String key,
            @Query("page") int page
    );

    @GET("get")
    Call<Recipe> getRecipe(
            @Query("key") String key,
            @Query("rId") String rId
    );

}
