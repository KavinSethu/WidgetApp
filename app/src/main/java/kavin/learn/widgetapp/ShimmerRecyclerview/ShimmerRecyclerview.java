package kavin.learn.widgetapp.ShimmerRecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import kavin.learn.widgetapp.MyApplication;
import kavin.learn.widgetapp.R;

public class ShimmerRecyclerview extends AppCompatActivity {

    private static final String TAG = ShimmerRecyclerview.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Recipe> cartList;
    private RecipeListAdapter mAdapter;

    private ShimmerFrameLayout mShimmerViewContainer;

    // URL to fetch menu json
    // this endpoint takes 2 sec before giving the response to add
    // some delay to test the Shimmer effect
    private static final String URL = "https://api.androidhive.info/json/shimmer/menu.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shimmer_recyclerview);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        recyclerView = findViewById(R.id.recycler_view);
        cartList = new ArrayList<>();
        mAdapter = new RecipeListAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        // making http call and fetching menu json
        fetchRecipes();
    }

    /**
     * method make volley network call and parses json
     */
    private void fetchRecipes() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Recipe> recipes = new Gson().fromJson(response.toString(), new TypeToken<List<Recipe>>() {
                        }.getType());

                        // adding recipes to cart list
                        cartList.clear();
                        cartList.addAll(recipes);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                        // stop animating Shimmer and hide the layout
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}
