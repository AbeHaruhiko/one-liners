package jp.caliconography.android.gesture;

import android.view.MotionEvent;
import android.view.View;

import hugo.weaving.DebugLog;

public class TranslationBy1FingerGestureDetector extends TranslationGestureDetector {
    private float mX, mY; // タッチイベント時の座標
    private int mPointerID1, mPointerID2; // ポインタID記憶用

    public TranslationBy1FingerGestureDetector(TranslationGestureListener listener) {
        super(listener);
    }

    /**
     * タッチ処理
     */
    @DebugLog
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // 最初の指の設定
                mPointerID1 = pointerId;
                mPointerID2 = -1;
                mX = x;
                mY = y;
                getListener().onTranslationBegin(this);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // 3本目の指以降は無視する
                if (mPointerID2 == -1) {
                    mPointerID2 = pointerId;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (mPointerID1 == pointerId) {
                    mPointerID1 = -1;
                    mX = x;
                    mY = y;
                    getListener().onTranslationEnd(this);
                } else if (mPointerID2 == pointerId) {
                    mPointerID2 = -1;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPointerID1 != -1) {
                    mX = x;
                    mY = y;
                    getListener().onTranslationEnd(this);
                }
                mPointerID1 = -1;
                mPointerID2 = -1;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mPointerID1 >= 0 && mPointerID2 == -1) {
                    int ptrIndex = event.findPointerIndex(mPointerID1);
                    mX = event.getX(ptrIndex);
                    mY = event.getY(ptrIndex);

                    getListener().onTranslation(this);
                }
                if (mPointerID1 >= 0) {
                    int ptrIndex = event.findPointerIndex(mPointerID1);
                    mX = event.getX(ptrIndex);
                    mY = event.getY(ptrIndex);
                }
                break;
        }

        return true;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }
}