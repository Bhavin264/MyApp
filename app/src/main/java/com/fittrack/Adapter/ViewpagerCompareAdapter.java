package com.fittrack.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fittrack.Model.CompareClg.DataCompareList;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/18/2017.
 */
public class ViewpagerCompareAdapter extends android.support.v4.view.PagerAdapter {
    Context context;
    //    ArrayList<String> dataCompareList = new ArrayList<String>();
    ImageView image1, image2;
    private LayoutInflater layoutInflater = null;
    List<DataCompareList> dataCompareList = new ArrayList<>();
    private TextView tv_date1, tv_weight1, tv_date2, tv_weight2;

    public ViewpagerCompareAdapter(Context context, List<DataCompareList> dataCompareList) {
        this.context = context;
        this.dataCompareList = dataCompareList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataCompareList.size();
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
        Drawable drawable = context.getResources().getDrawable(R.drawable.image_placeholder_compare);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        View layout = layoutInflater.inflate(R.layout.viewpager_compare, container, false);

        image1 = (ImageView) layout.findViewById(R.id.image1);
        tv_date1 = (TextView) layout.findViewById(R.id.tv_date1);
        tv_weight1 = (TextView) layout.findViewById(R.id.tv_weight1);
        image2 = (ImageView) layout.findViewById(R.id.image2);
        tv_date2 = (TextView) layout.findViewById(R.id.tv_date2);
        tv_weight2 = (TextView) layout.findViewById(R.id.tv_weight2);

        tv_date1.setText(dataCompareList.get(position).getBeforeDate());
        tv_weight1.setText(dataCompareList.get(position).getBeforeWeight());

        if (dataCompareList.size() > 0) {
            Picasso.with(context).load(dataCompareList.get(position).getUserBeforePhoto()).resize(w, h)
                    .placeholder(R.drawable.image_placeholder_compare)
                    .transform(new RoundedCornerImages())
                    .error(R.drawable.image_placeholder_compare).
                    into(image1);
        } else {
            image1.setImageResource(R.drawable.image_placeholder_compare);
        }
        tv_date2.setText(dataCompareList.get(position).getAfterDate());
        tv_weight2.setText(dataCompareList.get(position).getAfterWeight());
        if (dataCompareList.size() > 0) {
            Picasso.with(context).load(dataCompareList.get(position).getUserAfterPhoto()).resize(w, h)
                    .placeholder(R.drawable.image_placeholder_compare)
                    .transform(new RoundedCornerImages())
                    .error(R.drawable.image_placeholder_compare).
                    into(image2);
        } else {
            image2.setImageResource(R.drawable.image_placeholder_compare);
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

