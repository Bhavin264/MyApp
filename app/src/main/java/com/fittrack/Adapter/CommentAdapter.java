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
import com.fittrack.Model.Forum.ForumDetails.ForumComments;
import com.fittrack.R;
import com.fittrack.Utility.RoundedCornerImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umesh on 8/2/2017.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {

    String TAG = "CommentAdapter";
    private Activity activity;
    private RecyclerViewItemClickListener listener;
    List<ForumComments> commentsList = new ArrayList<>();

    public CommentAdapter(Activity activity, List<ForumComments> commentsList) {
        this.activity = activity;
        this.commentsList = commentsList;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.listener = recyclerViewItemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(activity).inflate(R.layout.item_comment, null);
        return new CustomViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.CustomViewHolder holder, final int position) {

        holder.tv_username.setText(commentsList.get(position).getUsername());
        holder.tv_date.setText(commentsList.get(position).getDateAdded());
        holder.tv_comment.setText(commentsList.get(position).getCommentText());

        Drawable drawable = activity.getResources().getDrawable(R.drawable.user_placeholder_small);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();

        if (!commentsList.get(position).getProfileImage().equalsIgnoreCase("")) {

            Picasso.with(activity).load(commentsList.get(position).getProfileImage())
                    .resize(w, h)
                    .centerCrop()
                    .transform(new RoundedCornerImages())
                    .placeholder(R.drawable.user_placeholder_small)
                    .error(R.drawable.user_placeholder_small)
                    .into(holder.img_Profile);

        } else {
            holder.img_Profile.setImageResource(R.drawable.user_placeholder_small);
        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_Profile;
        private TextView tv_username, tv_date, tv_comment;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            img_Profile = (ImageView) itemView.findViewById(R.id.img_Profile);
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





