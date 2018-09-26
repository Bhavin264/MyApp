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
import com.fittrack.Model.PhotoGallery.DataPhoto;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/2/2017.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.CustomViewHolder> {

    String TAG = "PhotoAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataPhoto> dataPhotos = new ArrayList<>();

    public PhotoAdapter(Activity activity, List<DataPhoto> dataPhotos) {
        this.activity = activity;
        this.dataPhotos = dataPhotos;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_photo, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.CustomViewHolder holder, final int position) {

        Drawable drawable = activity.getResources().getDrawable(R.drawable.image_placeholder_compare);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();

        holder.tv_date.setText(dataPhotos.get(position).getDate());
        holder.tv_weight.setText(dataPhotos.get(position).getWeight());

        if (!dataPhotos.get(position).getUserPhoto().equalsIgnoreCase("")) {
            Picasso.with(activity).load(dataPhotos.get(position).getUserPhoto())
                    .resize(w, h)
                    .centerCrop()
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.image_placeholder_compare)
                    .error(R.drawable.image_placeholder_compare)
                    .into(holder.img_photo_gallery);
        } else {
            holder.img_photo_gallery.setImageResource(R.drawable.image_placeholder_compare);
        }

    }


    @Override
    public int getItemCount() {
        return dataPhotos.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_photo_gallery;
        private TextView tv_date, tv_weight;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            img_photo_gallery = (ImageView) itemView.findViewById(R.id.img_photo_gallery);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_weight = (TextView) itemView.findViewById(R.id.tv_weight);

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





