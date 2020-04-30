package kavin.learn.widgetapp.FireBaseGet.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kavin.learn.widgetapp.FireBaseGet.FirebaseGetListActivity;
import kavin.learn.widgetapp.FireBaseGet.Pojo.Folder;
import kavin.learn.widgetapp.R;

/**
 * Created by Kavin on 11/14/2019.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    private List<Folder> folders;

    public FolderAdapter(Context context, List<Folder> uploads) {
        this.folders = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Folder folder = folders.get(position);

        holder.textViewName.setText(folder.getName());

        Glide.with(context).load(folder.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context, FirebaseGetListActivity.class);
                i.putExtra("folder",""+folder.getName());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
