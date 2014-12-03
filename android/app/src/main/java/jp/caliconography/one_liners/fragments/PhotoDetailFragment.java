package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;
import hugo.weaving.DebugLog;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.dummy.DummyContent;
import jp.caliconography.one_liners.event.PopupMenuItemClickedEvent;
import jp.caliconography.one_liners.gesture.TranslationBy1FingerGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationBy2FingerGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationGestureDetector;
import jp.caliconography.one_liners.gesture.TranslationGestureListener;
import jp.caliconography.one_liners.model.LineConfig;
import jp.caliconography.one_liners.model.PaintConfig;
import jp.caliconography.one_liners.model.PointInFloat;
import jp.caliconography.one_liners.model.parseobject.ParseLineConfig;
import jp.caliconography.one_liners.model.parseobject.ParseShapeConfig;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.util.BusHolder;
import jp.caliconography.one_liners.util.Utils;
import jp.caliconography.one_liners.util.parse.ParseObjectAsyncProcResult;
import jp.caliconography.one_liners.util.parse.ParseObjectAsyncUtil;
import jp.caliconography.one_liners.widget.ColorPopupItem;
import jp.caliconography.one_liners.widget.PopupMenu;
import jp.caliconography.one_liners.widget.PopupMenuItem;
import jp.caliconography.one_liners.widget.StrokeWidthPopupItem;

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
    public static final int MAX_PHOTO_HEIGHT = 1200;
    private static final int NEW_PHOTO_DIALOG_LISTENER_ID = 0;
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    @InjectView(R.id.progress_container)
    View mProgressContainer;
    @InjectView(R.id.progress_text)
    TextView mProgressText;
    @InjectView(R.id.photo)
    SurfaceView mPhotoView;
    //    @InjectView(R.id.load_image)
//    Button mBtnLoadImage;
    @InjectView(R.id.color_popup)
    PopupMenu mColorPopup;
    @InjectView(R.id.stroke_width_popup)
    PopupMenu mStrokeWidthPopup;

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
    private Review mReview = null;


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
        mReview = ((BookDetailActivity) getActivity()).getCurrentReview();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_photo_detail, container, false);

        ButterKnife.inject(this, rootView);

        // ポップアップメニューを構成
        createColorPopupMenu();
        createStrokeWidthPopupMenu();

        mSurfaceHolder = mPhotoView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);

        mMatrix = new Matrix();

        mScale = 1.0f;

        createGetsureDetectors();

//        createDefaultPaint();
        mPaint = new jp.caliconography.one_liners.model.Paint(true);

        // 保存済みのbitmapがあれば表示
        if (mReview.getPhotoFile() != null) {
            mReview.getOriginalPhotoFile().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    mBitmap = Utils.getBitmapFromByteArray(bytes);
                    while (!mSurfaceCreated) {
                    }

                    getScaleForFitBitmapToView();

                    Canvas canvas = null;
                    try {
                        canvas = mSurfaceHolder.lockCanvas(null);
                        if (canvas != null) {
                            setPhotoBitmapToCanvas(canvas);

                            // 線取得
                            // TODO: fetch
                            ArrayList<ParseShapeConfig> paintConfigs = mReview.getPaintConfigs();
                            for (ParseShapeConfig config : paintConfigs) {
                                try {
                                    LineConfig lineConfig = new LineConfig((ParseLineConfig) config);
                                    mLineConfigArray.add(lineConfig);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            renderAllPath(canvas);
                        }
                    } finally {
                        if (canvas != null) {
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {

                }
            });
        }

        return rootView;
    }

    private void createStrokeWidthPopupMenu() {
        ArrayList<PopupMenuItem> lineWidthMenuItems = new ArrayList<PopupMenuItem>();
        lineWidthMenuItems.add(new StrokeWidthPopupItem(getActivity().getApplicationContext(), 1, PaintConfig.StrokeWidth.THIN, R.drawable.icon_reflection_heart_red));
        lineWidthMenuItems.add(new StrokeWidthPopupItem(getActivity().getApplicationContext(), 2, PaintConfig.StrokeWidth.MID, R.drawable.icon_reflection_arrow_red));
        lineWidthMenuItems.add(new StrokeWidthPopupItem(getActivity().getApplicationContext(), 3, PaintConfig.StrokeWidth.FAT, R.drawable.icon_reflection_arrow_red));
        mStrokeWidthPopup.addItems(lineWidthMenuItems);
    }

    private void createColorPopupMenu() {
        ArrayList<PopupMenuItem> colorMenuItems = new ArrayList<PopupMenuItem>();
        colorMenuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 1, PaintConfig.Color.RED, R.drawable.icon_reflection_heart_red));
        colorMenuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 2, PaintConfig.Color.BLUE, R.drawable.icon_reflection_arrow_red));
        colorMenuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 3, PaintConfig.Color.GREEN, R.drawable.icon_reflection_arrow_red));
        colorMenuItems.add(new ColorPopupItem(getActivity().getApplicationContext(), 4, PaintConfig.Color.BLACK, R.drawable.icon_reflection_arrow_red));
        mColorPopup.addItems(colorMenuItems);
    }

    private void tryToSetPhotoBitmapToCanvas() {
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

//    private void createDefaultPaint() {
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
//        mPaint.setColor(0x88cdcdcd);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(20);
//    }

//    @OnClick(R.id.load_image)
//    void onClickLoadImage(View view) {
//        launchChooser();
//    }

    @OnTouch(R.id.photo)
    boolean onTouchPhoto(View view, MotionEvent motionEvent) {
        if (mBitmap != null) {
            mScaleGestureDetector.onTouchEvent(motionEvent);
            mTranslationBy1FingerGestureDetector.onTouch(view, motionEvent);
            mTranslationBy2FingerGestureDetector.onTouch(view, motionEvent);
        }

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

    private void renderAllPath(Canvas canvas, boolean withTranslate, boolean withScale) {
        for (LineConfig lineConfig : mLineConfigArray) {
            Path path = new Path();

            // 線の中点を原点(0, 0)へ配置。
            setLineOnOrigin(lineConfig, path);

            path.transform(buildMatrixForPanZoom(lineConfig, withTranslate, withScale));

            // 各Path用のPaintを生成（line.getPaint().setStrokeWidth()すると累乗になってしまうため。
            Paint paint = new Paint(lineConfig.getPaint());
            paint.setStrokeWidth(lineConfig.getPaint().getUnScaledStrokeWidth().getWidthInt() * (withScale ? mScale : 1));

            canvas.drawPath(path, paint);

            path.reset();

        }
    }

    private void renderAllPath(Canvas canvas) {
        renderAllPath(canvas, true, true);
    }

    private void renderAllPathIgnoreTranslateAndScale(Canvas canvas) {
        renderAllPath(canvas, false, false);
    }

    private Matrix buildMatrixForPanZoom(LineConfig lineConfig, boolean withTranslate, boolean withScale) {
        // 線の中点を求める
        PointInFloat lineCenter = PointInFloat.getMidpoint(new PointInFloat(lineConfig.getStartX(), lineConfig.getStartY()), new PointInFloat(lineConfig.getEndX(), lineConfig.getEndY()));

        float[] valueHolder = getMatrixFloats(lineConfig.getMatrix());

        Matrix matrix = new Matrix();

        float scaleOfThisLine = (withScale ? mScale : 1) / valueHolder[0];

        // 最初に線を描いた時点のscale(valueHolder[0])から今(mScale)何倍になっているか。 = mScale / valueHolder[0]
        matrix.postScale(scaleOfThisLine, scaleOfThisLine);

        // 本来の位置にtranslate
        matrix.postTranslate(lineCenter.x * scaleOfThisLine, lineCenter.y * scaleOfThisLine);

        // 描いた時点の移動分
        matrix.postTranslate(-lineConfig.getTranslateX() * scaleOfThisLine, -lineConfig.getTranslateY() * scaleOfThisLine);

        // 移動分
        if (withTranslate) {
            matrix.postTranslate(mTranslateX, mTranslateY);
        }
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
            mBitmap = Utils.scaleDownBitmap(getBitmapFromLocalFile(data), MAX_PHOTO_HEIGHT, getActivity());

            tryToSetPhotoBitmapToCanvas();

            mPictureUri = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.photo_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            returnToBookDetail();
            return true;

        } else if (id == R.id.new_photo) {

            if (mBitmap != null) {

                DialogFragment
                        .newInstance(true)
                        .setTitle(R.string.dialog_title_confirm)
                        .setMessage(R.string.dialog_confirm_message_overwrite_photo)
                        .setPositiveButtonText(R.string.dialog_posigive_button_text)
                        .setNegativeButtonText(R.string.dialog_negative_button_text)
                        .setListener(NEW_PHOTO_DIALOG_LISTENER_ID, new DialogFragment.IDialogFragmentListener() {

                            @Override
                            public void onEvent(int id, int event) {
                                switch (event) {

                                    case DialogFragment.IDialogFragmentListener.ON_POSITIVE_BUTTON_CLICKED:
                                        launchChooser();
                                        break;

                                    case DialogFragment.IDialogFragmentListener.ON_NEGATIVE_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_NEUTRAL_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_CLOSE_BUTTON_CLICKED:
                                    case DialogFragment.IDialogFragmentListener.ON_CANCEL:
                                        break;
                                }
                            }
                        })
                        .show(getFragmentManager());

            } else {
                launchChooser();
            }

        } else if (id == R.id.save_photo) {

            // 写真があるか判定
            if (mBitmap == null) {
                return false;
            }

            if (Utils.isOffline(getActivity())) {
                // TODO: エラーメッセージ表示が仮
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "ネットワークに接続できません",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            showProgressBar();

            ArrayList<Task<ParseObjectAsyncProcResult>> tasks = new ArrayList<Task<ParseObjectAsyncProcResult>>();

            // 編集履歴を保存
            ArrayList<ParseShapeConfig> paintConfigs = new ArrayList<ParseShapeConfig>();
            for (LineConfig config : mLineConfigArray) {
                try {
                    ParseLineConfig confParseObj = new ParseLineConfig();
                    confParseObj.setConfig(config);
                    paintConfigs.add(confParseObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            mReview.setPaintConfigs(paintConfigs);
            tasks.add(ParseObjectAsyncUtil.saveAsync(mReview));

            Bitmap bitmap = Bitmap.createBitmap((int) (mBitmap.getWidth()), (int) (mBitmap.getHeight()), Bitmap.Config.ARGB_8888);
            // view のサイズで Bitmap を作成
            Canvas canvas = new Canvas(bitmap);        // bitmap をターゲットにした Canvas を作成
            setPhotoBitmapToCanvasIgnoreTranslateAndScale(canvas);

            // 線の書き込み前にオリジナルを保存
            final ParseFile originalPhotoFile = new ParseFile("original.png", Utils.bitmapToByte(bitmap));
            tasks.add(ParseObjectAsyncUtil.saveAsync(originalPhotoFile));

            renderAllPathIgnoreTranslateAndScale(canvas);

            // bitmap保存
            final ParseFile file = new ParseFile("photo.png", Utils.bitmapToByte(bitmap));
            tasks.add(ParseObjectAsyncUtil.saveAsync(file));

            Task.whenAll(tasks).continueWith(new Continuation<Void, Void>() {
                @Override
                public Void then(Task<Void> task) throws Exception {
                    if (task.getError() == null) {
                        addOriginalPhotoToReview(originalPhotoFile);
                        addPhotoToReview(file);
                        returnToBookDetail();
                    } else {
                        hideProgressBar();
                        // TODO: エラーメッセージ表示が仮
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Error saving: " + task.getError().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
            });

            bitmap.recycle();
            mBitmap.recycle();

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

    private Bitmap getBitmapFromLocalFile(Intent intent) {
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

    private void setPhotoBitmapToCanvas(Canvas canvas, boolean withTranslate, boolean withScale) {

        mMatrix.reset();
        if (withScale) {
            mMatrix.postScale(mScale, mScale);
        }
        if (withTranslate) {
            mMatrix.postTranslate(mTranslateX, mTranslateY);
        }

        canvas.drawColor(Color.WHITE);       // 画像部分はmatrixで縮小されるので余白ができる。余白部分を白で表示させるための処理。
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void setPhotoBitmapToCanvas(Canvas canvas) {
        setPhotoBitmapToCanvas(canvas, true, true);
    }

    private void setPhotoBitmapToCanvasIgnoreTranslateAndScale(Canvas canvas) {
        setPhotoBitmapToCanvas(canvas, false, false);
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

    @Subscribe
    public void onItemClicked(PopupMenuItemClickedEvent event) {
        if (event.getItem() instanceof ColorPopupItem) {
            mPaintConfig.setColor(((ColorPopupItem) event.getItem()).getValue());
            mColorPopup.close();
        } else if (event.getItem() instanceof StrokeWidthPopupItem) {
            mPaintConfig.setStrokeWidth(((StrokeWidthPopupItem) event.getItem()).getValue());
            mStrokeWidthPopup.close();
        }
    }

    private void returnToBookDetail() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack(BookDetailFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void addOriginalPhotoToReview(ParseFile photoFile) {
        mReview.setOriginalPhotoFile(photoFile);
        mReview.setPhotoFileWidth(mBitmap.getWidth());
        mReview.setPhotoFileHeight(mBitmap.getHeight());
    }

    private void addPhotoToReview(ParseFile photoFile) {
        mReview.setPhotoFile(photoFile);
        mReview.setPhotoFileWidth(mBitmap.getWidth());
        mReview.setPhotoFileHeight(mBitmap.getHeight());
    }
}