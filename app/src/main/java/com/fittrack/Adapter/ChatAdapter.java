package com.fittrack.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fittrack.Interface.RecyclerViewItemClickListener;
import com.fittrack.Model.Chat.DataChat;
import com.fittrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder> {

    String TAG = "ChatAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<DataChat> dataChats = new ArrayList<>();

    public ChatAdapter(Activity activity, List<DataChat> dataChats) {
        this.activity = activity;
        this.dataChats = dataChats;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_chat, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.CustomViewHolder holder, final int position) {

        if (dataChats != null) {

            if (dataChats.get(position).getSendFrom().equalsIgnoreCase("0")) {

                 /*send from 0 is sender*/
                holder.layout_sender.setVisibility(View.VISIBLE);
                holder.layout_receiver.setVisibility(View.GONE);
                holder.tv_message_sender.setText(dataChats.get(position).getMessage());
                holder.tv_sender_time.setText(dataChats.get(position).getTimeCompare());


            } else if (dataChats.get(position).getSendFrom().equalsIgnoreCase("1")) {

                  /*send from 2 is receiver*/
                holder.layout_sender.setVisibility(View.GONE);
                holder.layout_receiver.setVisibility(View.VISIBLE);
                holder.tv_message_receiver.setText(dataChats.get(position).getMessage());
                holder.tv_receiver_time.setText(dataChats.get(position).getTimeCompare());

            }
        }
    }


    @Override
    public int getItemCount() {
        return dataChats.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_message_sender, tv_message_receiver,tv_sender_time,tv_receiver_time;
        RelativeLayout layout_receiver, layout_sender;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_message_sender = (TextView) itemView.findViewById(R.id.tv_message_sender);
            tv_message_receiver = (TextView) itemView.findViewById(R.id.tv_message_receiver);
            layout_receiver = (RelativeLayout) itemView.findViewById(R.id.layout_receiver);
            layout_sender = (RelativeLayout) itemView.findViewById(R.id.layout_sender);
            tv_sender_time = (TextView) itemView.findViewById(R.id.tv_sender_time);
            tv_receiver_time = (TextView) itemView.findViewById(R.id.tv_receiver_time);

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





