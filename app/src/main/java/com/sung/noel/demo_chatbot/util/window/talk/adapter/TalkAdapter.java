package com.sung.noel.demo_chatbot.util.window.talk.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class TalkAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<Talk> talks;
    private Context context;
    private LayoutInflater inflater;

    public TalkAdapter(Context context) {
        this.context = context;
        talks = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    //-----------

    public void setData(ArrayList<Talk> talks) {
        this.talks = talks;
        notifyDataSetChanged();
    }
    //-----------

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_talk_header, parent, false);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        String talker;
        Talk talk = talks.get(position);
        //機器人
        if (talk.getType() == Talk._TYPE_BOT) {
            talker = context.getString(R.string.talk_board_header_bot);
            headerViewHolder.tvHeader.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
        //使用者
        else {
            talker = context.getString(R.string.talk_board_header_user);
            headerViewHolder.tvHeader.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
        headerViewHolder.tvHeader.setText(talker);
        return convertView;
    }
    //-----------

    @Override
    public long getHeaderId(int position) {
        return talks.get(position).getType();
    }
    //-----------

    @Override
    public int getCount() {
        return talks.size();
    }
    //-----------

    @Override
    public Object getItem(int i) {
        return talks.get(i);
    }
    //-----------

    @Override
    public long getItemId(int position) {
        return position;
    }
    //-----------

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MessageViewHolder messageViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_talk_message, viewGroup, false);
            messageViewHolder = new MessageViewHolder(convertView);
            convertView.setTag(messageViewHolder);
        } else {
            messageViewHolder = (MessageViewHolder) convertView.getTag();
        }
        Talk talk = talks.get(position);
        int gravity;
        if (talk.getType() == Talk._TYPE_BOT) {
            gravity = Gravity.START;
        } else {
            gravity = Gravity.END;
        }
        messageViewHolder.linearLayout.setGravity(gravity);
        messageViewHolder.tvMessage.setText(talk.getMessage());
        return convertView;
    }


    //--------

    class MessageViewHolder {
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.linear_layout)
        LinearLayout linearLayout;

        MessageViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    //--------

    class HeaderViewHolder {
        @BindView(R.id.tv_header)
        TextView tvHeader;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
            tvHeader.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
