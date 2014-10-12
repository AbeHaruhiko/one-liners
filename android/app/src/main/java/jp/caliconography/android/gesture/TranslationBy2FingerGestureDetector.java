package jp.caliconography.android.gesture;

import android.view.MotionEvent;
import android.view.View;

public class TranslationBy2FingerGestureDetector extends TranslationGestureDetector {
    private float mX1, mY1, mX2, mY2; // タッチイベント時の座標
    private int mPointerID1, mPointerID2; // ポインタID記憶用
    private float mFocusX, mFocusY;

    public TranslationBy2FingerGestureDetector(TranslationGestureListener listener) {
        super(listener);
    }

    /**
     * タッチ処理
     */
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
                mX1 = x;
                mY1 = y;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // 3本目の指以降は無視する
                if (mPointerID1 == -1) {
                    mPointerID1 = pointerId;
                    mX1 = x;
                    mY1 = y;
                    if (mPointerID2 != -1) {
                        int ptrIndex = event.findPointerIndex(mPointerID2);
                        mX2 = event.getX(ptrIndex);
                        mY2 = event.getY(ptrIndex);
                        mFocusX = calcCenter(mX1, mX2);
                        mFocusY = calcCenter(mY1, mY2);
                        getListener().onTranslationBegin(this);
                    }
                } else if (mPointerID2 == -1) {
                    mPointerID2 = pointerId;
                    mX2 = x;
                    mY2 = y;
                    if (mPointerID1 != -1) {
                        int ptrIndex = event.findPointerIndex(mPointerID1);
                        mX1 = event.getX(ptrIndex);
                        mY1 = event.getY(ptrIndex);
                        mFocusX = calcCenter(mX1, mX2);
                        mFocusY = calcCenter(mY1, mY2);
                        getListener().onTranslationBegin(this);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mPointerID1 == -1 || mPointerID2 == -1) {
                    // 二本指でない場合は移動しない。
                    break;
                }
                // 1
                float x1 = event.getX(event.findPointerIndex(mPointerID1));
                float y1 = event.getY(event.findPointerIndex(mPointerID1));
                float x2 = event.getX(event.findPointerIndex(mPointerID2));
                float y2 = event.getY(event.findPointerIndex(mPointerID2));

                mX2 = x2;
                mY2 = y2;
                mX1 = x1;
                mY1 = y1;

                // 4
                mFocusX = calcCenter(x1, x2);
                mFocusY = calcCenter(y1, y2);

                // 5
                getListener().onTranslation(this);
                break;

            case MotionEvent.ACTION_UP:
                mPointerID1 = -1;
                mPointerID2 = -1;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (mPointerID1 == pointerId) {
                    mPointerID1 = -1;
                    if (mPointerID2 != -1) {
                        getListener().onTranslationEnd(this);
                    }
                } else if (mPointerID2 == pointerId) {
                    mPointerID2 = -1;
                    if (mPointerID1 != -1) {
                        getListener().onTranslationEnd(this);
                    }
                }
                break;
        }

        return true;
    }

    public float getX1() {
        return mX1;
    }

    public float getY1() {
        return mY1;
    }

    public float getX2() {
        return mX2;
    }

    public float getY2() {
        return mY2;
    }

    public float getFocusX() {
        return mFocusX;
    }

    public float getFocusY() {
        return mFocusY;
    }

    private float calcCenter(float p1, float p2) {
        return (p1 + p2) / 2;
    }
}