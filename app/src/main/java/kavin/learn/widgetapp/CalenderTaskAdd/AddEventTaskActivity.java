package kavin.learn.widgetapp.CalenderTaskAdd;

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
import kavin.learn.widgetapp.ShimmerRecyclerview.Recipe;
import kavin.learn.widgetapp.ShimmerRecyclerview.RecipeListAdapter;

public class AddEventTaskActivity extends AppCompatActivity {

    RecyclerView contact_recyclerview;
    private List<Contactpojo> contactpojoList;
    private ContactListAdapter mAdapter;

    private ShimmerFrameLayout mShimmerViewContainer;
    private static final String URL = "https://api.androidhive.info/json/shimmer/menu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_task);

        findviews();

        fetchRecipes();
    }

    private void findviews() {
        contact_recyclerview=findViewById(R.id.contact_recyclerview);
        mShimmerViewContainer=findViewById(R.id.shimmer_view_container);

        contactpojoList = new ArrayList<>();
        mAdapter = new ContactListAdapter(this, contactpojoList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        contact_recyclerview.setLayoutManager(mLayoutManager);
        contact_recyclerview.setItemAnimator(new DefaultItemAnimator());contact_recyclerview.setAdapter(mAdapter);
    }

    private void fetchRecipes() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contactpojo> contactpojos = new Gson().fromJson(response.toString(), new TypeToken<List<Contactpojo>>() {
                        }.getType());

                        // adding recipes to cart list
                        contactpojoList.clear();
                        contactpojoList.addAll(contactpojos);

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
                Log.e("OnError", "Error: " + error.getMessage());
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
