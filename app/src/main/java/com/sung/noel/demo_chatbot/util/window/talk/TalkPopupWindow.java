package com.sung.noel.demo_chatbot.util.window.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.TimeUtil;
import com.sung.noel.demo_chatbot.util.ai.AIRecognizeUtil;
import com.sung.noel.demo_chatbot.util.data.DataBaseHelper;
import com.sung.noel.demo_chatbot.util.window.talk.adapter.TalkAdapter;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.util.ArrayList;

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
    @BindView(R.id.tv_cover)
    TextView tvCover;

    private Context context;
    private TalkAdapter talkAdapter;
    private ArrayList<Talk> talks = new ArrayList<>();
    private int dataSize;
    private AIRecognizeUtil aiRecognizeUtil;
    private DataBaseHelper dataBaseHelper;

    private int viewWidth;
    private int keyboardHeight;
    private int viewHeight;


    //第幾個文字
    private int textIndex = 0;
    //行為重複間隔
    private final int DURATION = 150;
    //放大倍率
    private final float TEXT_SIZE = 1.5f;
    private Handler handler;
    private Runnable runnable;
    public TalkPopupWindow(final Context context, AIRecognizeUtil aiRecognizeUtil, int viewWidth, final int viewHeight) {
        this.context = context;
        this.aiRecognizeUtil = aiRecognizeUtil;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        view = LayoutInflater.from(context).inflate(R.layout.window_talk, null, false);
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
        dataBaseHelper = new DataBaseHelper(context, DataBaseHelper._DB_NAME, null, DataBaseHelper._DB_VERSION);
        talkAdapter = new TalkAdapter(context);
        stickyListHeadersListView.setAdapter(talkAdapter);
        stickyListHeadersListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        stickyListHeadersListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (textIndex < tvCover.getText().toString().length()) {
                    tvCover.setText(getSpannedText(tvCover.getText().toString(), textIndex));
                    textIndex++;
                } else {
                    textIndex = 0;
                }
                handler.postDelayed(this, DURATION);
            }
        };

        handler = new Handler();
    }


    //--------------

    private CharSequence getSpannedText(String text, int strIndex) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new RelativeSizeSpan(TEXT_SIZE), strIndex, strIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
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
                    talk.setMessage(editText.getText().toString());

                    dataBaseHelper.insert(DataBaseHelper._DB_TABLE_TALK, talk.toContentValues());
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
        talks = dataBaseHelper.getTalkDatas();
        dataSize = talks.size();
        talkAdapter.setData(talks);

        if (dataSize > 0) {
            stickyListHeadersListView.setSelection(dataSize - 1);
        }
    }
    //-------------

    public void loading(boolean isLoading) {
        if (isLoading) {
            tvCover.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, DURATION);
        } else {
            tvCover.setVisibility(View.GONE);
            handler.removeCallbacks(runnable);
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
