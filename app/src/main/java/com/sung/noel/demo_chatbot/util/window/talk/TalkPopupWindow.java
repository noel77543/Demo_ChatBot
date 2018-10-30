package com.sung.noel.demo_chatbot.util.window.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;
import com.sung.noel.demo_chatbot.util.window.talk.adapter.TalkAdapter;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TalkPopupWindow extends PopupWindow  {
    private View view;
    private RecyclerView recyclerView;
    private Context context;
    private TalkAdapter talkAdapter;


    public TalkPopupWindow(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_talk, null, false);
        ButterKnife.bind(this, view);
        setContentView(view);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.talk_pop_animation);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
    }

    //----------------
    private void init() {
        talkAdapter = new TalkAdapter(context);
        recyclerView = ButterKnife.findById(view, R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(talkAdapter);

    }

    //----------

    @OnClick(R.id.tv_close)
    public void onClicked(View view) {
        dismiss();
    }

    //-----------------

    /***
     * show
     */
    public void showPopupWindow(View view,int height,int width ,ArrayList<Talk> talks) {
        setHeight(height);
        setWidth(width);
        talkAdapter.setData(talks);
        showAsDropDown(view,0,0, Gravity.BOTTOM);
    }
}
