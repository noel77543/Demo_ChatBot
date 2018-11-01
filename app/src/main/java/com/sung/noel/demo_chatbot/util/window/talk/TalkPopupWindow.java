package com.sung.noel.demo_chatbot.util.window.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Handler;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;
import com.sung.noel.demo_chatbot.util.SharedPreferenceUtil;
import com.sung.noel.demo_chatbot.util.ai.AIRecognizeUtil;
import com.sung.noel.demo_chatbot.util.window.talk.adapter.TalkAdapter;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TalkPopupWindow extends PopupWindow {
    private View view;
    private StickyListHeadersListView stickyListHeadersListView;
    private EditText editText;
    private Context context;
    private TalkAdapter talkAdapter;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int dataSize;
    private AIRecognizeUtil aiRecognizeUtil;


    private int viewWidth;
    private int viewHeightLarge;
    private int viewHeightSmall;


    public TalkPopupWindow(Context context, AIRecognizeUtil aiRecognizeUtil, int viewWidth, int viewHeightLarge, int keyboardHeight) {
        this.context = context;
        this.aiRecognizeUtil = aiRecognizeUtil;
        this.viewWidth = viewWidth;
        this.viewHeightLarge = viewHeightLarge;
        viewHeightSmall = viewHeightLarge - keyboardHeight;
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
        sharedPreferenceUtil = new SharedPreferenceUtil(context, SharedPreferenceUtil._USER_DEFAULT_NAME);
        talkAdapter = new TalkAdapter(context);

        stickyListHeadersListView = ButterKnife.findById(view, R.id.sticky_list_view);
        editText = ButterKnife.findById(view, R.id.edit_text);
        stickyListHeadersListView.setAdapter(talkAdapter);
        stickyListHeadersListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        stickyListHeadersListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    //----------

    @OnClick({R.id.tv_close, R.id.btn_sent})
    public void onClicked(View view) {
        switch (view.getId()) {
            //關閉
            case R.id.tv_close:
                dismiss();
                break;
            //發送
            case R.id.btn_sent:
                String text = editText.getText().toString();
                if (text.length() > 0) {
                    Talk talk = new Talk();
                    talk.setType(Talk._TYPE_USER);
                    talk.setText(editText.getText().toString());
                    sharedPreferenceUtil.addTalk(talk);
                    aiRecognizeUtil.connectToDialogflow(text);
                    editText.setText("");
                }
                break;
        }
    }

    //-----------------

    /***
     * show
     */
    public void showPopupWindow(View view) {
        setHeight(viewHeightLarge);
        setWidth(viewWidth);
        showAsDropDown(view, 0, 0, Gravity.CENTER);
        notifyData();
    }

    //--------------

    /***
     * 依照小鍵盤出現與唪更改高度
     */
    public void updateSize(boolean isKeyboardVisible) {
        int viewHeight;
        //當小鍵盤出現
        if (isKeyboardVisible) {
            viewHeight = viewHeightSmall;
        }
        //當小鍵盤消失
        else {
            viewHeight = viewHeightLarge;
        }
        setHeight(viewHeight);
        setWidth(viewWidth);
        update();
    }


    //--------------

    /***
     * 刷新資料
     */
    public void notifyData() {
        dataSize = sharedPreferenceUtil.getTalks().size();
        talkAdapter.setData(sharedPreferenceUtil.getTalks());
        if (dataSize > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stickyListHeadersListView.smoothScrollToPosition(dataSize - 1);
                }
            }, 200);
        }
    }
}
