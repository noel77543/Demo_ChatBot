package com.sung.noel.demo_chatbot.util.window.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;
import com.sung.noel.demo_chatbot.util.SharedPreferenceUtil;
import com.sung.noel.demo_chatbot.util.ai.AIRecognizeUtil;
import com.sung.noel.demo_chatbot.util.window.talk.adapter.TalkAdapter;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TalkPopupWindow extends PopupWindow implements ViewTreeObserver.OnGlobalLayoutListener {
    private View view;
    @BindView(R.id.sticky_list_view)
    StickyListHeadersListView stickyListHeadersListView;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    private Context context;
    private TalkAdapter talkAdapter;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int dataSize;
    private AIRecognizeUtil aiRecognizeUtil;


    private int viewWidth;
    private int keyboardHeight;
    private int viewHeight;


    public TalkPopupWindow(final Context context, AIRecognizeUtil aiRecognizeUtil, int viewWidth, final int viewHeight) {
        this.context = context;
        this.aiRecognizeUtil = aiRecognizeUtil;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

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
        setHeight(viewHeight);
        setWidth(viewWidth);
        showAtLocation(view, Gravity.TOP, 0, 0);
        notifyData();
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    //--------------

    /***
     * 刷新資料
     */
    public void notifyData() {
        dataSize = sharedPreferenceUtil.getTalks().size();
        talkAdapter.setData(sharedPreferenceUtil.getTalks());

        if (dataSize > 0) {
            stickyListHeadersListView.setSelection(dataSize - 1);
        }
    }
    //--------------

    /***
     * 計算 鍵盤彈出時 遮擋的畫面高度
     */
    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        relativeLayout.getWindowVisibleDisplayFrame(rect);
        keyboardHeight = viewHeight - (rect.bottom - rect.top);

        int newViewHeight;
        // 被遮擋值 >0 表示鍵盤出現 請注意4的重要性 可嘗試改之為0 後查看keyboardHeight值的變化
        if (keyboardHeight > 0) {
            newViewHeight = viewHeight - keyboardHeight + 4;
        } else {
            newViewHeight = viewHeight;
        }
        update(viewWidth, newViewHeight);
    }

}
