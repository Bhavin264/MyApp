package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodAvoid.DataFoodEat;
import com.fittrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/14/2017.
 */
public class FoodEatAdapter extends RecyclerView.Adapter<FoodEatAdapter.CustomViewHolder> {

    String TAG = "FoodEatAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataFoodEat> FoodEatList = new ArrayList<>();

    public FoodEatAdapter(Activity activity, List<DataFoodEat> FoodEatList) {
        this.activity = activity;
        this.FoodEatList = FoodEatList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_food_eat, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final FoodEatAdapter.CustomViewHolder holder, final int position) {

        holder.tv_eat_text.setText(FoodEatList.get(position).getEat());
    }


    @Override
    public int getItemCount() {
        return FoodEatList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_eat_text;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_eat_text = (TextView) itemView.findViewById(R.id.tv_eat_text);


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






