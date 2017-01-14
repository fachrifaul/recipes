package com.suleiman.pagination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("recipes")
    @Expose
    private List<ResultRecipes> recipes = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ResultRecipes> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ResultRecipes> recipes) {
        this.recipes = recipes;
    }

}