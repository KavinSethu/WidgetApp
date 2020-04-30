package kavin.learn.widgetapp.Assignment;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kavin.learn.widgetapp.R;
import kavin.learn.widgetapp.ShimmerRecyclerview.Recipe;
import kavin.learn.widgetapp.ShimmerRecyclerview.RecipeListAdapter;

public class AssignmentTopAdapter extends RecyclerView.Adapter<AssignmentTopAdapter.ViewHolder> {

    private Context context;
    private List<AssignTopPojo> topPojoList;

    public AssignmentTopAdapter(Context context, List<AssignTopPojo> topPojoList) {
        this.context = context;
        this.topPojoList = topPojoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_top_row, parent, false);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        double itemWidth = width / 2.33;

        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) itemWidth;
        itemView.setLayoutParams(layoutParams);

        return new AssignmentTopAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.top_row_Text.setText(topPojoList.get(position).getName());

        Glide.with(context).load(topPojoList.get(position).getImage()).into(holder.top_row_bg);

    }

    @Override
    public int getItemCount() {
        return topPojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView top_row_Text;
        ImageView top_row_bg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            top_row_bg=itemView.findViewById(R.id.top_row_bg);
            top_row_Text=itemView.findViewById(R.id.top_row_Text);
        }
    }
}
