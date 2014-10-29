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
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;
import hugo.weaving.DebugLog;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.event.PhotoSavedEvent;
import jp.caliconography.one_liners.event.PopupMenuItemClickedEvent;
import jp.caliconography.one_liners.gesture.TranslationBy1FingerGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationBy2FingerGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationGestureListener;
import jp.caliconography.one_liners.model.LineConfig;
import jp.caliconography.one_liners.model.PaintConfig;
import jp.caliconography.one_liners.model.PointInFloat;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.util.BusHolder;
import jp.caliconography.one_liners.util.Utils;
import jp.caliconography.one_liners.widget.ColorPopupItem;
import jp.caliconography.one_liners.widget.PopupMenu;
import jp.caliconography.one_liners.widget.PopupMenuItem;

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

    @InjectView(R.id.progressContainer)
    View mProgressContainer;
    @InjectView(R.id.photo)
    SurfaceView mPhotoView;
    @InjectView(R.id.load_image)
    Button mBtnLoadImage;
    @InjectView(R.id.color_popup)
    PopupMenu mColorPopup;

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
    private PaintConfig mPaintConfig = new PaintConfig();

    private boolean mSurfaceCreated;
    private Paint mPaint;
    ArrayList<LineConfig> mLineConfigArray = new ArrayList<LineConfig>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FragmentでMenuを表示する為に必要
        this.setHasOptionsMenu(true);

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_photo_detail, container, false);

        ButterKnife.inject(this, rootView);

        // ポップアップメニューを構成
        ArrayList<PopupMenuItem> menuItems = new ArrayList<PopupMenuItem>();
        menuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 1, PaintConfig.Color.RED, R.drawable.icon_reflection_heart_red));
        menuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 2, PaintConfig.Color.BLUE, R.drawable.icon_reflection_arrow_red));
        menuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 3, PaintConfig.Color.GREEN, R.drawable.icon_reflection_arrow_red));
        menuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 4, PaintConfig.Color.BLACK, R.drawable.icon_reflection_arrow_red));
        mColorPopup.addItems(menuItems);

        mSurfaceHolder = mPhotoView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);

        mMatrix = new Matrix();

        mScale = 1.0f;

        createGetsureDetectors();

        createDefaultPaint();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        BusHolder.get().register(this);
    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);

        super.onPause();
    }

    private void createGetsureDetectors() {
        mScaleGestureDetector = new ScaleGestureDetector(getActivity().getApplicationContext(), mOnScaleListener);
        mTranslationBy1FingerGestureDetector = new TranslationBy1FingerGestureDetector(mTranslationBy1FingerListener);
        mTranslationBy2FingerGestureDetector = new TranslationBy2FingerGestureDetector(mTranslationBy2FingerListener);
    }

    private void createDefaultPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0x88cdcdcd);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
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

    private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
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
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    private final ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
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
            Log.d(TAG, "mScale=" + Float.toString(mScale));

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);

                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    drawTempPath(canvas);
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

    private final TranslationGestureListener mTranslationBy1FingerListener = new TranslationGestureListener() {
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
                    drawTempPath(canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    };

    private final TranslationGestureListener mTranslationBy2FingerListener = new TranslationGestureListener() {
        @DebugLog
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
        }

        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {
            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();
        }

        @DebugLog
        @Override
        public void onTranslation(TranslationGestureDetector detector) {
            TranslationBy2FingerGestureDetector twoFingerDetector = (TranslationBy2FingerGestureDetector) detector;
            mTranslateX += twoFingerDetector.getFocusX() - mPrevX;
            mTranslateY += twoFingerDetector.getFocusY() - mPrevY;
            mPrevX = twoFingerDetector.getFocusX();
            mPrevY = twoFingerDetector.getFocusY();

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(null);

                if (canvas != null) {
                    setPhotoBitmapToCanvas(canvas);
                    drawTempPath(canvas);
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
        for (LineConfig lineConfig : mLineConfigArray) {
            Path path = new Path();

            // 線の中点を原点(0, 0)へ配置。
            setLineOnOrigin(lineConfig, path);

            path.transform(buildMatrixForPanZoom(lineConfig));

            // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
            Paint paint = new Paint(lineConfig.getPaint());
            paint.setStrokeWidth(lineConfig.getPaint().getUnScaledStrokeWidth().getWidthInt() * mScale);

            canvas.drawPath(path, paint);

            path.reset();

        }
    }

    private Matrix buildMatrixForPanZoom(LineConfig lineConfig) {
        // 線の中点を求める
        PointInFloat lineCenter = PointInFloat.getMidpoint(new PointInFloat(lineConfig.getStartX(), lineConfig.getStartY()), new PointInFloat(lineConfig.getEndX(), lineConfig.getEndY()));

        float[] valueHolder = getMatrixFloats(lineConfig.getMatrix());

        Matrix matrix = new Matrix();

        float scaleOfThisLine = mScale / valueHolder[0];

        // 最初に線を描いた時点のscale(valueHolder[0])から今(mScale)何倍になっているか。 = mScale / valueHolder[0]
        matrix.postScale(scaleOfThisLine, scaleOfThisLine);

        // 本来の位置にtranslate
        matrix.postTranslate(lineCenter.x * scaleOfThisLine, lineCenter.y * scaleOfThisLine);

        // 描いた時点の移動分
        matrix.postTranslate(-lineConfig.getTranslateX() * scaleOfThisLine, -lineConfig.getTranslateY() * scaleOfThisLine);

        // 移動分
        matrix.postTranslate(mTranslateX, mTranslateY);
        return matrix;
    }

    private float[] getMatrixFloats(Matrix matrix) {
        float[] valueHolder = new float[9];
        matrix.getValues(valueHolder);
        return valueHolder;
    }

    private void setLineOnOrigin(LineConfig lineConfig, Path path) {
        // 線の中点を求める
        PointInFloat lineCenter = PointInFloat.getMidpoint(new PointInFloat(lineConfig.getStartX(), lineConfig.getStartY()), new PointInFloat(lineConfig.getEndX(), lineConfig.getEndY()));

        path.moveTo(lineConfig.getStartX() - lineCenter.x, lineConfig.getStartY() - lineCenter.y);
        path.lineTo(lineConfig.getEndX() - lineCenter.x, lineConfig.getEndY() - lineCenter.y);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this.getActivity(), new Intent(this.getActivity(), BookDetailActivity.class));
            return true;
        } else if (id == R.id.save_photo) {

            showProgressBar();

            File file_ = Utils.getScreenShotFile(mPhotoView, getActivity());
            Log.d(TAG, file_.getAbsolutePath());

            // Fileとして保存
            final ParseFile file = new ParseFile("photo.png", Review.bitmapToByte(getViewBitmap(mPhotoView)));
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        BusHolder.get().post(new PhotoSavedEvent(file));
                    } else {
                        hideProgressBar();
                    }
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideProgressBar() {
        mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        mProgressContainer.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    private void getScaleForFitBitmapToView() {
        float heightScale = (float) mSurfaceHolder.getSurfaceFrame().height() / (float) mBitmap.getHeight();
        float widthScale = (float) mSurfaceHolder.getSurfaceFrame().width() / (float) mBitmap.getWidth();
        mScale = Math.min(heightScale, widthScale);
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

    private LineConfig drawPath(Canvas canvas, int color) {

        // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
        jp.caliconography.one_liners.model.Paint paint = new jp.caliconography.one_liners.model.Paint(mPaint);
        paint.setColor(color);
        paint.setStrokeWidth(mPaintConfig.getStrokeWidth().getWidthInt() * mScale);
        paint.setUnScaledStrokeWidth(mPaintConfig.getStrokeWidth());

        Path path = new Path();
        path.moveTo(mOriginX, mOriginY);
        path.lineTo(mCurrentX, mCurrentY);

        canvas.drawPath(path, paint);
        path.reset();

        return new LineConfig(mOriginX, mOriginY, mCurrentX, mCurrentY, paint, new Matrix(mMatrix), mTranslateX, mTranslateY);
    }

    private void drawTempPath(Canvas canvas) {
        drawPath(canvas, 0x88cdcdcd);
    }

    private void fixPath(Canvas canvas) {
        LineConfig lineConfig = drawPath(canvas, mPaintConfig.getColor().getColorInt());
        // TODO 点も描けるようにしたい。
        if (mOriginX != mCurrentX || mOriginY != mCurrentY) {
            mLineConfigArray.add(lineConfig);
        }
    }

//    private void fixPath(Canvas canvas) {
//
//        // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
//        Paint paint = new Paint(mPaint);
//        paint.setColor(mPaintConfig.getColor().getColorInt());
//        paint.setAlpha(0x88);
//        paint.setStrokeWidth(mPaint.getStrokeWidth() * mScale);
//
//        Path path = new Path();
//        path.moveTo(mOriginX, mOriginY);
//        path.lineTo(mCurrentX, mCurrentY);
//
//        canvas.drawPath(path, paint);
//        path.reset();
//    }

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

    public Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();
        if (cache == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    @Subscribe
    public void onItemClicked(PopupMenuItemClickedEvent event) {
        mPaintConfig.setColor(((ColorPopupItem) event.getItem()).getValue());
        mColorPopup.close();
    }

    @Subscribe
    public void onPhotoSaved(PhotoSavedEvent event) {

        final Review review = new Review();
        review.setPhotoFile(event.getFile());
        review.setPhoto(getViewBitmap(mPhotoView));
//        review.pinInBackground();
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "bytes saved.");

                Intent intent = new Intent();
                intent.putExtra("reviewId", review.getObjectId());

                Activity photoDetailActivity = getActivity();
                photoDetailActivity.setResult(Activity.RESULT_OK, intent);
                photoDetailActivity.finish();
            }
        });

    }
}