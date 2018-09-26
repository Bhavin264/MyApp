package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.Forum.DataForum;
import com.fittrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/2/2017.
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.CustomViewHolder> {

    String TAG = "ForumAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataForum> dataForumList = new ArrayList<>();

    public ForumAdapter(Activity activity, List<DataForum> dataForumList) {
        this.activity = activity;
        this.dataForumList = dataForumList;
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
    public void onBindViewHolder(final ForumAdapter.CustomViewHolder holder, final int position) {

        holder.tv_topic.setText(dataForumList.get(position).getForumTopic());
        holder.tv_title.setText(dataForumList.get(position).getForumTitle());
    }


    @Override
    public int getItemCount() {
        return dataForumList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_topic, tv_title;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_topic = (TextView) itemView.findViewById(R.id.tv_topic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);

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




