package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;
import hugo.weaving.DebugLog;
import jp.caliconography.android.gesture.TranslationBy1FingerGestureDetector;
import jp.caliconography.android.gesture.TranslationBy2FingerGestureDetector;
import jp.caliconography.android.gesture.TranslationGestureDetector;
import jp.caliconography.android.gesture.TranslationGestureListener;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.model.Line;
import jp.caliconography.one_liners.model.PointInFloat;

/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link jp.caliconography.one_liners.activities.BookListActivity}
 * in two-pane mode (on tablets) or a {@link jp.caliconography.one_liners.activities.PhotoDetailActivity}
 * on handsets.
 */
public class PhotoDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final int IMAGE_CHOOSER_RESULTCODE = 0;
    static final String TAG = PhotoDetailFragment.class.getSimpleName();
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    @InjectView(R.id.photo)
    SurfaceView mPhotoView;
    @InjectView(R.id.load_image)
    Button mBtnLoadImage;

    private Uri mPictureUri;
    private SurfaceHolder mSurfaceHolder;
    private Matrix mMatrix;
    private float mTranslateX;
    private float mTranslateY;
    private float mScale;
    private ScaleGestureDetector mScaleGestureDetector;
    private TranslationBy1FingerGestureDetector mTranslationBy1FingerGestureDetector;
    private TranslationBy2FingerGestureDetector mTranslationBy2FingerGestureDetector;
    private Bitmap mBitmap;
    private float mPrevX, mPrevY;       // matrixのtranslateで前回からの差分で計算すつるための前回検出点
    private float mOriginX, mOriginY;   // 一本指でスワイプを開始した点
    private float mCurrentX, mCurrentY; // 一本指の現在点


    private boolean mSurfaceCreated;
    private Paint mPaint;
    ArrayList<Line> mLineArray = new ArrayList<Line>();
    private float mDeltaX;
    private float mDeltaY;
    private boolean mTranslatingBy2Finger;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        ButterKnife.inject(this, rootView);

        mSurfaceHolder = mPhotoView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);

        mMatrix = new Matrix();

        mScale = 1.0f;
        mScaleGestureDetector = new ScaleGestureDetector(getActivity().getApplicationContext(), mOnScaleListener);
        mTranslationBy1FingerGestureDetector = new TranslationBy1FingerGestureDetector(mTranslationBy1FingerListener);
        mTranslationBy2FingerGestureDetector = new TranslationBy2FingerGestureDetector(mTranslationBy2FingerListener);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0x88cdcdcd);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        return rootView;
    }

    @OnClick(R.id.load_image)
    void onClickLoadImage(View view) {
        launchChooser();
    }

    @OnTouch(R.id.photo)
    boolean onTouchPhoto(View view, MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        mTranslationBy1FingerGestureDetector.onTouch(view, motionEvent);
        mTranslationBy2FingerGestureDetector.onTouch(view, motionEvent);

        return true;
    }

    private PointInFloat mSurfaceCenter;
    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mSurfaceCreated = true;

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    if (mBitmap != null) {
                        getScaleForFitBitmapToView();
                        // 背景をセット
                        setPhotoBitmapToCanvas(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @DebugLog
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
//            mTranslateX = width / 2;
//            mTranslateY = height / 2;
            mSurfaceCenter = new PointInFloat(width / 2, height / 2);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    private float mDeltaScale;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mDeltaScale = detector.getScaleFactor();
            mScale *= mDeltaScale;
            Log.d(TAG, "mScale=" + Float.toString(mScale));

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);

                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    drawPath(canvas);
                    Log.d(TAG, "______onScale______");
                    renderAllPath(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            return true;
        }
    };

    private TranslationGestureListener mTranslationBy1FingerListener = new TranslationGestureListener() {
        @DebugLog
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
            // 線確定
            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    Log.d(TAG, "______一本指のonTranslationEnd______");
                    renderAllPath(canvas);
                    // TODO 点も描けるようにしたい。
                    if (mOriginX != mCurrentX || mOriginY != mCurrentY) {
                        mLineArray.add(new Line(mOriginX, mOriginY, mCurrentX, mCurrentY, new Paint(mPaint), new Matrix(mMatrix)));
                        Log.d(TAG, "___added!!___" + mLineArray.size());
                    }
                    fixPath(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @DebugLog
        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {

            TranslationBy1FingerGestureDetector oneFingerDetector = (TranslationBy1FingerGestureDetector) detector;
            mCurrentX = mOriginX = oneFingerDetector.getX();
            mCurrentY = mOriginY = oneFingerDetector.getY();
        }

        @DebugLog
        @Override
        public void onTranslation(TranslationGestureDetector detector) {
            TranslationBy1FingerGestureDetector oneFingerDetector = (TranslationBy1FingerGestureDetector) detector;
            mCurrentX = oneFingerDetector.getX();
            mCurrentY = oneFingerDetector.getY();

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);

                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    renderAllPath(canvas);
                    drawPath(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    };

    private TranslationGestureListener mTranslationBy2FingerListener = new TranslationGestureListener() {
        @DebugLog
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
            mDeltaX = 0;
            mDeltaY = 0;
            mTranslatingBy2Finger = false;
        }

        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {
            mTranslatingBy2Finger = true;
            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();
        }

        @DebugLog
        @Override
        public void onTranslation(TranslationGestureDetector detector) {
            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            mDeltaX = twoFingerDetector.getFocusX() - mPrevX;
            mDeltaY = twoFingerDetector.getFocusY() - mPrevY;
            mTranslateX += mDeltaX;
            mTranslateY += mDeltaY;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);

                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    drawPath(canvas);
                    Log.d(TAG, "______一本指のonTranslation______");
                    renderAllPath(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    };

    private void renderAllPath(Canvas canvas) {
        for (Line line : mLineArray) {
            mPaint.setColor(0x88ff0000);

//            Log.d(TAG, "all!!!___" + line.getStartX() + ": " + line.getStartY() + ": " + line.getEndX() + ": " + line.getEndY());

            Path path = new Path();

            // 一旦SurfaceViewの中心に配置して、移動させる。（背景と同じ動きにさせるため）

//            // 線の中点を求める
//            PointInFloat lineCenter = PointInFloat.getMidpoint(new PointInFloat(line.getStartX(), line.getStartY()), new PointInFloat(line.getEndX(), line.getEndY()));
//
//            // 線の中点を原点(0, 0)へ配置。
//            path.moveTo(line.getStartX() - lineCenter.x, line.getStartY() - lineCenter.y);
//            path.lineTo(line.getEndX() - lineCenter.x, line.getEndY() - lineCenter.y);

            path.moveTo(line.getStartX(), line.getStartY());
            path.lineTo(line.getEndX(), line.getEndY());

            float[] valueHolder = new float[9];
            line.getMatrix().getValues(valueHolder);

            Matrix tmpMatrix = new Matrix();
            // 最初に線を描いた時点のscale(valueHolder[0])から今(mScale)何倍になっているか。 = mScale / valueHolder[0]
            tmpMatrix.postScale(mScale / valueHolder[0], mScale / valueHolder[0]);
//            if (mScaleGestureDetector.isInProgress()) {
//            } else {
            // 本来の位置にtranslate
//            tmpMatrix.postTranslate(lineCenter.x, lineCenter.y);

//            // ドラッグ分
//            line.addTranslateX(mDeltaX);
//            line.addTranslateY(mDeltaY);
            tmpMatrix.postTranslate(mTranslateX, mTranslateY);
//            tmpMatrix.postTranslate((float)mSurfaceCenter.x, (float)mSurfaceCenter.y);
//            tmpMatrix.postTranslate(mTranslateX - mSurfaceCenter.x, mTranslateY - mSurfaceCenter.y);
//            }
            path.transform(tmpMatrix);

            // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
            Paint tmpPaint = new Paint(line.getPaint());
            tmpPaint.setColor(0x88fffcfc);
            tmpPaint.setStrokeWidth(line.getPaint().getStrokeWidth() * mScale);

            canvas.drawPath(path, tmpPaint);
            path.reset();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CHOOSER_RESULTCODE) {

            if (resultCode != Activity.RESULT_OK) {
                if (mPictureUri != null) {
                    getActivity().getContentResolver().delete(mPictureUri, null, null);
                    mPictureUri = null;
                }
                return;
            }
            mBitmap = getBitmap(data);

            if (mSurfaceCreated) {
                getScaleForFitBitmapToView();

                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas(null);
                    if (canvas != null) {
                        setPhotoBitmapToCanvas(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }

            mPictureUri = null;
        }
    }

    private void getScaleForFitBitmapToView() {
        // SurfaceViewにフィットさせるための設定。
        float hightScale = (float) mSurfaceHolder.getSurfaceFrame().height() / (float) mBitmap.getHeight();
        float widthScale = (float) mSurfaceHolder.getSurfaceFrame().width() / (float) mBitmap.getWidth();
        mScale = Math.min(hightScale, widthScale);
    }

    private Bitmap getBitmap(Intent intent) {
        // 戻り値からInputStreamを取得
        InputStream in = null;
        Bitmap bitmap = null;
        // 読み込む際のオプション
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;       // このbitmapをオフスクリーンバッファにしたい。= これを引数にcanvasを生成したい。mutableでないとnew Canvas(bitmap)で例外
        try {
            Uri result = (intent == null) ? mPictureUri : intent.getData();

            in = getActivity().getContentResolver().openInputStream(result);
            // Bitmapの取得
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    private void setPhotoBitmapToCanvas(Canvas canvas) {

        mMatrix.reset();
        mMatrix.postScale(mScale, mScale);
        mMatrix.postTranslate(mTranslateX, mTranslateY);

        canvas.drawColor(Color.WHITE);       // 画像部分はmatrixで縮小されるので余白ができる。余白部分を白で表示させるための処理。
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void drawPath(Canvas canvas) {

        mPaint.setColor(0x88cdcdcd);

        // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
        Paint tmpPaint = new Paint();
        tmpPaint.setAntiAlias(true);
        tmpPaint.setDither(true);
        tmpPaint.setColor(mPaint.getColor());
        tmpPaint.setStyle(mPaint.getStyle());
        tmpPaint.setStrokeJoin(mPaint.getStrokeJoin());
        tmpPaint.setStrokeCap(mPaint.getStrokeCap());
        tmpPaint.setStrokeWidth(mPaint.getStrokeWidth() * mScale);

        Path path = new Path();
        path.moveTo(mOriginX, mOriginY);
        path.lineTo(mCurrentX, mCurrentY);

        canvas.drawPath(path, tmpPaint);
        path.reset();
    }

    private void fixPath(Canvas canvas) {

        mPaint.setColor(0x88ff0000);

        // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
        Paint tmpPaint = new Paint();
        tmpPaint.setAntiAlias(true);
        tmpPaint.setDither(true);
        tmpPaint.setColor(mPaint.getColor());
        tmpPaint.setStyle(mPaint.getStyle());
        tmpPaint.setStrokeJoin(mPaint.getStrokeJoin());
        tmpPaint.setStrokeCap(mPaint.getStrokeCap());
        tmpPaint.setStrokeWidth(mPaint.getStrokeWidth() * mScale);

        Path path = new Path();
        path.moveTo(mOriginX, mOriginY);
        path.lineTo(mCurrentX, mCurrentY);
        canvas.drawPath(path, tmpPaint);
        path.reset();

        Log.d(TAG, "fixed!!___" + mOriginX + ": " + mOriginY + ": " + mCurrentX + ": " + mCurrentY);

    }

    private void launchChooser() {
        // ギャラリーから選択
        Intent imagePickIntent = getImagePickIntent();

        // カメラで撮影
        Intent cameraIntent = getCameraIntent();

        // ギャラリー選択のIntentでcreateChooser()
        Intent chooserIntent = Intent.createChooser(imagePickIntent, "Pick Image");
        // EXTRA_INITIAL_INTENTS にカメラ撮影のIntentを追加
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, IMAGE_CHOOSER_RESULTCODE);
    }

    private Intent getCameraIntent() {
        String filename = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mPictureUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i2.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
        return i2;
    }

    private Intent getImagePickIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        return i;
    }
}