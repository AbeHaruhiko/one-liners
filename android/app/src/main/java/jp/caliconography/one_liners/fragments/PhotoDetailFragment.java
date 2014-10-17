package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private boolean mTranslatingBy1Finger, mTranslatingBy2Finger;


    private boolean mSurfaceCreated;
    private Paint mPaint;
    private String mOriginalBitmapFileName;
    private Bitmap mOffScreenBitmap;

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
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);

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

        Canvas canvas = null;
        try {
            canvas = mSurfaceHolder.lockCanvas(null);

            if (canvas != null) {
                if (mTranslatingBy1Finger) {
                    drawPath(canvas);
                } else if (mTranslatingBy2Finger) {
                    setPhotoBitmapToCanvas(canvas);
                }
            }
        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        return true;
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mSurfaceCreated = true;

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    // オフスクリーン用のBitmapを生成する
                    mOffScreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);

                    if (mBitmap != null) {
                        // 背景をセット
                        setPhotoBitmapToCanvas(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

//            if (mBitmap != null) {
//                try {
//                    canvas = mSurfaceHolder.lockCanvas(null);
//                    if (canvas != null) {
//                        setPhotoBitmapToCanvas(canvas);
//                    }
//                } finally {
//                    if (canvas != null) {
//                        mSurfaceHolder.unlockCanvasAndPost(canvas);
//                    }
//                }
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            mTranslateX = width / 2;
            mTranslateY = height / 2;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

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
            mScale *= detector.getScaleFactor();
            return true;
        }
    };

    private TranslationGestureListener mTranslationBy1FingerListener = new TranslationGestureListener() {
        @DebugLog
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
            mTranslatingBy1Finger = false;
        }

        @DebugLog
        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {

            mTranslatingBy1Finger = true;
            mTranslatingBy2Finger = false;

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
        }
    };

    private TranslationGestureListener mTranslationBy2FingerListener = new TranslationGestureListener() {
        @DebugLog
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
            mTranslatingBy2Finger = false;
        }

        @DebugLog
        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {

            mTranslatingBy2Finger = true;
            mTranslatingBy1Finger = false;

            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();
        }

        @DebugLog
        @Override
        public void onTranslation(TranslationGestureDetector detector) {
            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            float deltaX = twoFingerDetector.getFocusX() - mPrevX;
            float deltaY = twoFingerDetector.getFocusY() - mPrevY;
            mTranslateX += deltaX;
            mTranslateY += deltaY;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();
        }
    };

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

            // オリジナルをファイルに保管しておく。
            mOriginalBitmapFileName = saveImageToCacheDir(mBitmap, getActivity().getApplicationContext());

            if (mSurfaceCreated) {
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

    private String saveImageToCacheDir(Bitmap bmp, Context context) {
        File cacheDir = context.getExternalCacheDir();

        cleanCacheDir(cacheDir);

        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
        File file = new File(cacheDir, fileName);
        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 90, outputStream);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

    private void cleanCacheDir(File cacheDir) {
        // 一時キャッシュディレクトリをクリーニング
        File[] files = cacheDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    private void setPhotoBitmapToCanvas(Canvas canvas) {

        mMatrix.reset();
        mMatrix.postScale(mScale, mScale);
        mMatrix.postTranslate(-mBitmap.getWidth() / 2 * mScale, -mBitmap.getHeight() / 2 * mScale);
        mMatrix.postTranslate(mTranslateX, mTranslateY);

        // オフスクリーンバッファを生成する
        Canvas offScreen = new Canvas(mOffScreenBitmap);

        offScreen.drawColor(Color.WHITE);       // 画像部分はmatrixで縮小されるので余白ができる。余白部分を白で表示させるための処理。
        offScreen.drawBitmap(mBitmap, mMatrix, null);

//        if (mOriginX > 0 && mOriginY > 0 && mCurrentX > 0 && mCurrentY > 0) {
//            Path path = new Path();
//            path.moveTo(mOriginX, mOriginY);
//            path.lineTo(mCurrentX, mCurrentY);
//            offScreen.drawPath(path, mPaint);
//            path.reset();
//        }

        // オフスクリーンバッファを描画する
        canvas.drawBitmap(mOffScreenBitmap, 0, 0, null);
    }

    private void drawPath(Canvas canvas) {

        if (mOriginX > 0 && mOriginY > 0 && mCurrentX > 0 && mCurrentY > 0) {
            // オフスクリーンバッファを生成する
            Canvas offScreen = new Canvas(mOffScreenBitmap);

            Path path = new Path();
            path.moveTo(mOriginX, mOriginY);
            path.lineTo(mCurrentX, mCurrentY);
            offScreen.drawPath(path, mPaint);
            path.reset();

            // オフスクリーンバッファを描画する
            canvas.drawBitmap(mOffScreenBitmap, 0, 0, null);
        }
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