package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodAvoid.DataFoodStrictly;
import com.fittrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/14/2017.
 */
public class FoodAvoidAdapter extends RecyclerView.Adapter<FoodAvoidAdapter.CustomViewHolder> {

    String TAG = "FoodAvoidAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataFoodStrictly> foodStrictlyList = new ArrayList<>();

    public FoodAvoidAdapter(Activity activity, List<DataFoodStrictly> foodStrictlyList) {
        this.activity = activity;
        this.foodStrictlyList = foodStrictlyList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_food_avoid, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final FoodAvoidAdapter.CustomViewHolder holder, final int position) {

        holder.tv_avoid_text.setText(foodStrictlyList.get(position).getStrictly());
    }


    @Override
    public int getItemCount() {
        return foodStrictlyList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_avoid_text;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_avoid_text = (TextView) itemView.findViewById(R.id.tv_avoid_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition(), 0, itemView);
                    }
                }
            });
        }
    }
}





