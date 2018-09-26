package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodCategory.DataFoodCategory;
import com.fittrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {

    String TAG = "CategoryAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataFoodCategory> foodCategoryList = new ArrayList<>();

    public CategoryAdapter(Activity activity, List<DataFoodCategory> foodCategoryList) {
        this.activity = activity;
        this.foodCategoryList = foodCategoryList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_forum, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.CustomViewHolder holder, final int position) {

        holder.tv_topic.setText(foodCategoryList.get(position).getCategoryName());
    }


    @Override
    public int getItemCount() {
        return foodCategoryList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_topic, tv_title;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_topic = (TextView) itemView.findViewById(R.id.tv_topic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_title.setVisibility(View.GONE);

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





