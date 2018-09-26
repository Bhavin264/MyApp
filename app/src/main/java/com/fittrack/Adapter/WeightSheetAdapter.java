package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.WeightSheet.DataWeightDate;
import com.fittrack.Model.WeightSheet.DataWeightSheet;
import com.fittrack.R;
import com.fittrack.Utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/1/2017.
 */
public class WeightSheetAdapter extends RecyclerView.Adapter<WeightSheetAdapter.CustomViewHolder> {
    String TAG = "WeightSheetAdapter";

    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataWeightDate> dataWeightDates = new ArrayList<>();


    public WeightSheetAdapter(Activity activity, List<DataWeightDate> dataWeightDates) {
        this.activity = activity;
        this.dataWeightDates = dataWeightDates;

    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_weightsheet, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final WeightSheetAdapter.CustomViewHolder holder, final int position) {

        holder.tv_date.setText(dataWeightDates.get(position).getCreateDate());

        /*Add sub weight list View*/
        final List<DataWeightSheet> dataWeightSheetList = new ArrayList<>();
        dataWeightSheetList.clear();
        dataWeightSheetList.addAll(dataWeightDates.get(position).getWeightData());

        LogUtil.Print("dataServiceLists", "  " + dataWeightSheetList.size());

        if (dataWeightSheetList.size() != 0) {

            SubWeightSheetAdaper subWeightSheetAdaper = new SubWeightSheetAdaper(activity, dataWeightSheetList);
            holder.rv_sub_weight_list.setAdapter(subWeightSheetAdaper);
            subWeightSheetAdaper.notifyDataSetChanged();

        } else {

        }
    }


    @Override
    public int getItemCount() {
        return dataWeightDates.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView rv_sub_weight_list;
        private TextView tv_date;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            rv_sub_weight_list = (RecyclerView) itemView.findViewById(R.id.rv_sub_weight_list);
            rv_sub_weight_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            rv_sub_weight_list.setItemAnimator(new DefaultItemAnimator());
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


