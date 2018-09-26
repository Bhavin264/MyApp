package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.WeightSheet.DataWeightSheet;
import com.fittrack.R;
import com.fittrack.Utility.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/1/2017.
 */
public class SubWeightSheetAdaper extends RecyclerView.Adapter<SubWeightSheetAdaper.CustomViewHolder> {

    String TAG = "SubWeightSheetAdaper";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataWeightSheet> dataWeightSheetList = new ArrayList<>();

    public SubWeightSheetAdaper(Activity activity, List<DataWeightSheet> dataWeightSheetList) {
        this.activity = activity;
        this.dataWeightSheetList = dataWeightSheetList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_sub_weightsheet, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final SubWeightSheetAdaper.CustomViewHolder holder, final int position) {

        holder.tv_date.setText(MyUtils.GetDateOnRequireFormat(dataWeightSheetList.get(position).getDate(),
                "dd-MM-yyyy", "dd MMMM, yyyy"));
//        holder.tv_date.setText(dataWeightSheetList.get(position).getDate());
        holder.tv_day.setText(dataWeightSheetList.get(position).getDay());
        holder.tv_weight.setText(dataWeightSheetList.get(position).getWeight());
    }


    @Override
    public int getItemCount() {
        return dataWeightSheetList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date, tv_day, tv_weight;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
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



