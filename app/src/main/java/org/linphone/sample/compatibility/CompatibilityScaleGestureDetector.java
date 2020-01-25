package org.linphone.sample.compatibility;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class CompatibilityScaleGestureDetector
        extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private ScaleGestureDetector detector;
    private CompatibilityScaleGestureListener listener;

    public CompatibilityScaleGestureDetector(Context context) {
        detector = new ScaleGestureDetector(context, this);
    }

    public void setOnScaleListener(CompatibilityScaleGestureListener newListener) {
        listener = newListener;
    }

    public void onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (listener == null) {
            return false;
        }

        return listener.onScale(this);
    }

    public float getScaleFactor() {
        return detector.getScaleFactor();
    }

    public void destroy() {
        listener = null;
        detector = null;
    }
}
