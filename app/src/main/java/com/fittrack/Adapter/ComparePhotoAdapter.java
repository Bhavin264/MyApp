package com.fittrack.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.CompareClg.DataCompareList;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/11/2017.
 */
public class ComparePhotoAdapter extends RecyclerView.Adapter<ComparePhotoAdapter.CustomViewHolder> {

    String TAG = "ComparePhotoAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataCompareList> dataCompareList = new ArrayList<>();

    public ComparePhotoAdapter(Activity activity, List<DataCompareList> dataCompareList) {
        this.activity = activity;
        this.dataCompareList = dataCompareList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_compare_photo, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ComparePhotoAdapter.CustomViewHolder holder, final int position) {

//        holder.rl_bg1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_square_green));
//        holder.rl_bg2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_square_green));
//        holder.ll_bg1.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_square_transparent));
//        holder.ll_bg2.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_square_transparent));
        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_compare);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        holder.tv_date1.setText(dataCompareList.get(position).getBeforeDate());
        holder.tv_weight1.setText(dataCompareList.get(position).getBeforeWeight());
        holder.tv_date2.setText(dataCompareList.get(position).getAfterDate());
        holder.tv_weight2.setText(dataCompareList.get(position).getAfterWeight());

        if (!dataCompareList.get(position).getUserBeforePhoto().equalsIgnoreCase("")) {
            Picasso.with(activity).load(dataCompareList.get(position).getUserBeforePhoto())
                    .resize(w, h)
                    .centerCrop()
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.image_placeholder_compare)
                    .error(R.drawable.image_placeholder_compare)
                    .into(holder.img_photo1);
        } else {
            holder.img_photo1.setImageResource(R.drawable.image_placeholder_compare);
        }

        if (!dataCompareList.get(position).getUserAfterPhoto().equalsIgnoreCase("")) {
            Picasso.with(activity).load(dataCompareList.get(position).getUserAfterPhoto())
                    .resize(w, h)
                    .centerCrop()
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.image_placeholder_compare)
                    .error(R.drawable.image_placeholder_compare)
                    .into(holder.img_photo2);
        } else {
            holder.img_photo2.setImageResource(R.drawable.image_placeholder_compare);
        }



    }


    @Override
    public int getItemCount() {
        return dataCompareList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_compare;
        private ImageView img_photo1, img_photo2;
        private TextView tv_date1, tv_date2, tv_weight1, tv_weight2;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            ll_compare = (LinearLayout) itemView.findViewById(R.id.ll_compare);
            img_photo1 = (ImageView) itemView.findViewById(R.id.img_photo1);
            img_photo2 = (ImageView) itemView.findViewById(R.id.img_photo2);

            tv_date1 = (TextView) itemView.findViewById(R.id.tv_date1);
            tv_date2 = (TextView) itemView.findViewById(R.id.tv_date2);

            tv_weight1 = (TextView) itemView.findViewById(R.id.tv_weight1);
            tv_weight2 = (TextView) itemView.findViewById(R.id.tv_weight2);


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






