package com.suleiman.pagination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("recipe")
    @Expose
    private ResultRecipe recipe;

    public ResultRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ResultRecipe recipe) {
        this.recipe = recipe;
    }

}