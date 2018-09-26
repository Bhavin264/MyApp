package com.fittrack.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.FoodCategory.DataDietFood;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class DietFoodAdapter extends RecyclerView.Adapter<DietFoodAdapter.CustomViewHolder> {

    String TAG = "DietFoodAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataDietFood> dataDietFoodList = new ArrayList<>();

    public DietFoodAdapter(Activity activity, List<DataDietFood> dataDietFoodList) {
        this.activity = activity;
        this.dataDietFoodList = dataDietFoodList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_dietfood_category, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final DietFoodAdapter.CustomViewHolder holder, final int position) {

        holder.tv_food_name.setText(dataDietFoodList.get(position).getFoodTitle());
        holder.tv_food_calories.setText(dataDietFoodList.get(position).getCalories());
        holder.tv_food_fat.setText(dataDietFoodList.get(position).getTotalFat());
        holder.tv_food_carbs.setText(dataDietFoodList.get(position).getTotalCarbohydrate());
        holder.tv_food_protein.setText(dataDietFoodList.get(position).getProtein());
        holder.tv_serving_size.setText(dataDietFoodList.get(position).getServingSize());

        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_compare);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();

        if (!dataDietFoodList.get(position).getFoodImage().equalsIgnoreCase("")) {
            Picasso.with(activity).load(dataDietFoodList.get(position).getFoodImage())
                    .resize(w, h)
                    .centerCrop()
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.image_placeholder_compare)
                    .error(R.drawable.image_placeholder_compare)
                    .into(holder.img_food);
        } else {
            holder.img_food.setImageResource(R.drawable.image_placeholder_compare);
        }
    }


    @Override
    public int getItemCount() {
        return dataDietFoodList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_food;
        private TextView tv_food_name, tv_food_calories, tv_food_fat, tv_food_carbs, tv_food_protein, tv_serving_size;

        public CustomViewHolder(final View itemView) {
            super(itemView);

            img_food = (ImageView) itemView.findViewById(R.id.img_food);
            tv_food_name = (TextView) itemView.findViewById(R.id.tv_food_name);
            tv_food_calories = (TextView) itemView.findViewById(R.id.tv_food_calories);
            tv_food_fat = (TextView) itemView.findViewById(R.id.tv_food_fat);
            tv_food_carbs = (TextView) itemView.findViewById(R.id.tv_food_carbs);
            tv_food_protein = (TextView) itemView.findViewById(R.id.tv_food_protein);
            tv_serving_size = (TextView) itemView.findViewById(R.id.tv_serving_size);

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






