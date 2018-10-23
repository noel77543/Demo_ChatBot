package com.sung.noel.demo_chatbot.Talk;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sung.noel.demo_chatbot.R;

import butterknife.ButterKnife;

public class TalkDialog extends Dialog{
    public TalkDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_talk);
        ButterKnife.bind(this);
        setCancelable(false);
        init();
    }

    //----------------
    private void init() {
    }
}
