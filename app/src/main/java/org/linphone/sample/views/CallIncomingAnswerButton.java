package org.linphone.sample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.linphone.sample.R;

public class CallIncomingAnswerButton extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private LinearLayout mRoot;
    private boolean mUseSliderMode = false;
    private CallIncomingButtonListener mListener;
    private View mDeclineButton;

    private int mScreenWidth;
    private boolean mBegin;
    private float mAnswerX, mOldSize;

    public CallIncomingAnswerButton(Context context) {
        super(context);
        init();
    }

    public CallIncomingAnswerButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CallIncomingAnswerButton(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSliderMode(boolean enabled) {
        mUseSliderMode = enabled;
        findViewById(R.id.acceptUnlock).setVisibility(enabled ? VISIBLE : GONE);
    }

    public void setListener(CallIncomingButtonListener listener) {
        mListener = listener;
    }

    public void setDeclineButton(View decline) {
        mDeclineButton = decline;
    }

    private void init() {
        inflate(getContext(), R.layout.call_incoming_answer_button, this);
        mRoot = findViewById(R.id.root);
        mRoot.setOnClickListener(this);
        mRoot.setOnTouchListener(this);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void onClick(View v) {
        if (!mUseSliderMode) {
            performClick();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mUseSliderMode) {
            float curX;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDeclineButton.setVisibility(View.GONE);
                    mAnswerX = motionEvent.getX() - mRoot.getWidth();
                    mBegin = true;
                    mOldSize = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    curX = motionEvent.getX() - mRoot.getWidth();
                    view.scrollBy((int) (mAnswerX - curX), view.getScrollY());
                    mOldSize -= mAnswerX - curX;
                    mAnswerX = curX;
                    if (mOldSize < -25) mBegin = false;
                    if (curX < (mScreenWidth / 4) - mRoot.getWidth() && !mBegin) {
                        performClick();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mDeclineButton.setVisibility(View.VISIBLE);
                    view.scrollTo(0, view.getScrollY());
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (mListener != null) {
            mListener.onAction();
        }
        return true;
    }
}
