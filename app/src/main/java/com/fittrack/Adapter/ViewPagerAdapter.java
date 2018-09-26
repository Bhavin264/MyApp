package com.fittrack.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fittrack.Model.PhotoGallery.DataPhoto;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/22/2017.
 */
public class ViewPagerAdapter extends PagerAdapter {
    Context context;
//    ArrayList<String> dataPhotos = new ArrayList<String>();
    ImageView image;
    private LayoutInflater layoutInflater = null;
    List<DataPhoto> dataPhotos = new ArrayList<>();
    private TextView tv_date,tv_weight;

    public ViewPagerAdapter(Context context, List<DataPhoto> dataPhotos) {
        this.context = context;
        this.dataPhotos = dataPhotos;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    //	public float getPageWidth(int position)
//    {
//		return 0.7f;
//    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.image_placeholder_big);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        View layout = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);

        image = (ImageView) layout.findViewById(R.id.image);
        tv_date = (TextView) layout.findViewById(R.id.tv_date);
        tv_weight = (TextView) layout.findViewById(R.id.tv_weight);

        tv_date.setText(dataPhotos.get(position).getDate());
        tv_weight.setText(dataPhotos.get(position).getWeight());

        if (dataPhotos.size() > 0) {
            Picasso.with(context).load(dataPhotos.get(position).getUserPhoto()).resize(w, h)
                    .placeholder(R.drawable.image_placeholder_big)
                    .transform(new RoundedCornerImages())
                    .error(R.drawable.image_placeholder_big).
                    into(image);
        } else {
            image.setImageResource(R.drawable.image_placeholder_big);
        }
        container.addView(layout);
        return layout;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
