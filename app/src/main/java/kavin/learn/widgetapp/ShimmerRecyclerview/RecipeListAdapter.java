package kavin.learn.widgetapp.ShimmerRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kavin.learn.widgetapp.R;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {
    private Context context;
    private List<Recipe> cartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price, chef, timestamp;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            chef = view.findViewById(R.id.chef);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public RecipeListAdapter(Context context, List<Recipe> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Recipe recipe = cartList.get(position);
        holder.name.setText(recipe.getName());
        holder.chef.setText("By " + recipe.getChef());
        holder.description.setText(recipe.getDescription());
        holder.price.setText("Price: ₹" + recipe.getPrice());
        holder.timestamp.setText(recipe.getTimestamp());

        Glide.with(context)
                .load(recipe.getThumbnail())
                .into(holder.thumbnail);
    }
    // recipe
    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
