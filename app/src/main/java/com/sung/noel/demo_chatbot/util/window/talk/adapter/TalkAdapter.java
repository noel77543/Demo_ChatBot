package com.sung.noel.demo_chatbot.util.window.talk.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Talk> talks;
    private Context context;


    public TalkAdapter(Context context) {
        this.context = context;
        talks = new ArrayList<>();
    }

    //-----------

    public void setData(ArrayList<Talk> talks) {
        this.talks = talks;
        notifyDataSetChanged();
    }


    //----------
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //使用者
        if (viewType == Talk._TYPE_USER) {
            return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.list_talk_user, parent, false));
        }
        //機器人
        else {
            return new BotViewHolder(LayoutInflater.from(context).inflate(R.layout.list_talk_bot, parent, false));
        }
    }
    //----------

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Talk talk = talks.get(position);

        //使用者
        if (viewHolder instanceof UserViewHolder) {
            UserViewHolder userViewHolder = (UserViewHolder) viewHolder;
            userViewHolder.tvMessage.setText(talk.getText());
            userViewHolder.tvDate.setText(talk.getDate());

        }
        //機器人
        else if (viewHolder instanceof BotViewHolder) {
            BotViewHolder botViewHolder = (BotViewHolder) viewHolder;
            botViewHolder.tvMessage.setText(talk.getText());
            botViewHolder.tvDate.setText(talk.getDate());

        }
    }
    //-------------

    @Override
    public int getItemViewType(int position) {
        return talks.get(position).getType();
    }


    //----------

    @Override
    public int getItemCount() {
        return talks.size();
    }

    //--------

    class BotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        BotViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }
    //--------

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_date)
        TextView tvDate;

        UserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        }
    }
}
