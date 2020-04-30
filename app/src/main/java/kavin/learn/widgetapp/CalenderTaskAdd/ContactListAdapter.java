package kavin.learn.widgetapp.CalenderTaskAdd;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kavin.learn.widgetapp.R;
import kavin.learn.widgetapp.ShimmerRecyclerview.Recipe;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {
    private Context context;
    private List<Contactpojo> contactpojoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price, chef, timestamp;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }


    public ContactListAdapter(Context context, List<Contactpojo> contactpojos) {
        this.context = context;
        this.contactpojoList = contactpojos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Contactpojo recipe = contactpojoList.get(position);
        holder.name.setText(recipe.getName());
        holder.description.setText(recipe.getDescription());
        Glide.with(context)
                .load(recipe.getThumbnail())
                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.contact_popup);
                dialog.getWindow().getAttributes().windowAnimations = R.style.JustSlideAnimation;
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                TextView tv_name=dialog.findViewById(R.id.tv_name);
                TextView tv_desc=dialog.findViewById(R.id.tv_desc);
                CircleImageView im=dialog.findViewById(R.id.top_circle_img);

                tv_name.setText(recipe.getName());
                tv_desc.setText(recipe.getDescription());

                Glide.with(context)
                        .load(recipe.getThumbnail())
                        .into(im);

                dialog.show();
            }
        });
    }
    // recipe
    @Override
    public int getItemCount() {
        return contactpojoList.size();
    }
}
