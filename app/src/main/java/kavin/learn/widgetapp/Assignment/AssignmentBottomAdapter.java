package kavin.learn.widgetapp.Assignment;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kavin.learn.widgetapp.R;

public class AssignmentBottomAdapter extends RecyclerView.Adapter<AssignmentBottomAdapter.ViewHolder> {

    private Context context;
    private List<AssignBottomPojo> assignBottomPojos;

    public AssignmentBottomAdapter(Context context, List<AssignBottomPojo> assignBottomPojos) {
        this.context = context;
        this.assignBottomPojos = assignBottomPojos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_bottom_row, parent, false);
        return new AssignmentBottomAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bot_row_Text.setText(assignBottomPojos.get(position).getName());

        holder.bot_row_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog;
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.assign_adapter_popup);
                dialog.getWindow().getAttributes().windowAnimations = R.style.JustSlideAnimation;
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                TextView cancel=dialog.findViewById(R.id.tv_cancel);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignBottomPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bot_row_Text;
        ImageView bot_row_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bot_row_Text=itemView.findViewById(R.id.bottom_row_tv);
            bot_row_menu=itemView.findViewById(R.id.bottom_row_menu);
        }
    }
}
