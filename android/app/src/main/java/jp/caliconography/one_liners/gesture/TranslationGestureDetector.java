package jp.caliconography.one_liners.gesture;

import android.view.MotionEvent;
import android.view.View;

abstract public class TranslationGestureDetector {
    private TranslationGestureListener mListener;

    public TranslationGestureDetector(TranslationGestureListener listener) {
        mListener = listener;
    }

    protected TranslationGestureListener getListener() {
        return mListener;
    }

    /**
     * タッチ処理
     */
    abstract public boolean onTouch(View v, MotionEvent event);

}