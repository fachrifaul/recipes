package com.suleiman.pagination;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.suleiman.pagination.api.RecipeApi;
import com.suleiman.pagination.api.RecipeService;
import com.suleiman.pagination.models.Recipe;
import com.suleiman.pagination.models.ResultRecipe;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_IMAGE = "image";

    private String id;
    private String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        id = intent.getStringExtra(EXTRA_ID);
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);
        imageUrl = intent.getStringExtra(EXTRA_IMAGE);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        //adview
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadBackdrop();
        loadDataRecipe();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progress);

        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // TODO: 08/11/16 handle failure
                        mProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        // imageUrl ready, hide progress now
                        mProgress.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized imageUrl
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    private void loadDataRecipe() {
        final TextView ingredientsTextView = (TextView) findViewById(R.id.ingredientsTextView);

        RecipeService recipeService = RecipeApi.getClient().create(RecipeService.class);
        recipeService.getRecipe(getString(R.string.recipe_api_key), id)
                .enqueue(new Callback<Recipe>() {
                    @Override public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        Recipe recipe = response.body();
                        ResultRecipe resultRecipe = recipe.getRecipe();
                        List<String> list = resultRecipe.getIngredients();
                        String ingredients = "";

                        for (String string : list) {
                            ingredients += string + "\n";
                        }

                        ingredientsTextView.setText(ingredients);
                    }

                    @Override public void onFailure(Call<Recipe> call, Throwable t) {
                        t.printStackTrace();
                        ingredientsTextView.setText(fetchErrorMessage(t));
                    }
                });
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishActivity();
        return true;
    }

    void finishActivity() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}