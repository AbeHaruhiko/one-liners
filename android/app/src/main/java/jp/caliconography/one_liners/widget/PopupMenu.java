package jp.caliconography.one_liners.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.caliconography.one_liners.R;

/**
 * Created by abeharuhiko on 2014/10/23.
 */
public class PopupMenu extends FrameLayout {

    @InjectView(R.id.popup_menu_base)
    Button mBaseButton;
    @InjectView(R.id.menu1)
    ImageView mMenu1;
    @InjectView(R.id.menu2)
    ImageView mMenu2;
    @InjectView(R.id.menu3)
    ImageView mMenu3;
    @InjectView(R.id.menu4)
    ImageView mMenu4;
    @InjectView(R.id.menu5)
    ImageView mMenu5;

    private final ArrayList<ImageView> viewList = new ArrayList<ImageView>();

    private boolean isOpenMenu = false;
    private final int RADIUS = 300;     //メニューが開いたときの半径の長さ
    private final int DEGREE = 90;     //メニューが開く角度

    public PopupMenu(Context context) {
        super(context);
    }

    public PopupMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        // LayoutInflaterでレイアウトxmlの内容でViewを作る
        // LayoutInflater#inflate()の第2引数ではルートとなるViewとして自分自身を指定する
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_menu, this);

        ButterKnife.inject(this, layout);

        viewList.add(mMenu1);
        viewList.add(mMenu2);
        viewList.add(mMenu3);
        viewList.add(mMenu4);
        viewList.add(mMenu5);
    }

    public PopupMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @OnClick(R.id.popup_menu_base)
    void onClickBase(View view) {
        if (!isOpenMenu) {
            openAnimation();
        } else {
            closeAnimation();
        }
        isOpenMenu = !isOpenMenu;
    }

    //全体の角度から１つのメニュー同士の間の角度を取得
    public float getDegree() {
        return DEGREE / (viewList.size() - 1);
    }

    //角度と半径からx軸方向にどれだけ移動するか取得
    public int getTranslateX(float degree) {
        return (int) (RADIUS * Math.cos(Math.toRadians(degree)));
    }

    //角度と半径からy軸方向にどれだけ移動するか取得
    public int getTranslateY(float degree) {
        return (int) (RADIUS * Math.sin(Math.toRadians(degree)));
    }

    //メニューをオープンするメソッド
    public void openAnimation() {
        for (int i = 0; i < viewList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewList.get(i), "translationY", 0f, -100f * i);
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new AnticipateOvershootInterpolator(2));
            objectAnimator.start();
        }
    }

    //メニューをクローズするアニメーション
    public void closeAnimation() {
        for (int i = 0; i < viewList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewList.get(i), "translationY", -100f * i, 0f);
            objectAnimator.setDuration(200);
            objectAnimator.start();
        }
    }
}
