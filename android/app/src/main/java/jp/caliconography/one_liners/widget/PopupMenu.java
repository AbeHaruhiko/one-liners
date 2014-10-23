package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
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

    private boolean isOpenMenu;
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
            //アニメーションで移動する分だけマージンを取る
            setMargin(i);
            //メニューが開くアニメーションを設定。
            TranslateAnimation openAnimation = new TranslateAnimation(-getTranslateX(getDegree() * i), 0, getTranslateY(getDegree() * i), 0);
            openAnimation.setDuration(500);
            openAnimation.setStartOffset(100 * i);
            AnticipateOvershootInterpolator overshootInterpolator = new AnticipateOvershootInterpolator(2);
            openAnimation.setInterpolator(overshootInterpolator);
            viewList.get(i).startAnimation(openAnimation);
        }
    }

    //メニューをクローズするアニメーション
    public void closeAnimation() {
        for (int i = 0; i < viewList.size(); i++) {
            //マージンを元に戻す
            resetMargin(i);
            //メニューが閉じるアニメーションを設定する
            TranslateAnimation closeAnimation = new TranslateAnimation(getTranslateX(getDegree() * i), 0, -getTranslateY(getDegree() * i), 0);
            closeAnimation.setDuration(300);
            closeAnimation.setStartOffset(100 * i);
            viewList.get(i).startAnimation(closeAnimation);
        }
    }

    //アニメーション後の座標までマージンを取るメソッド
    public void setMargin(int index) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewList.get(index).getLayoutParams();
        params.bottomMargin = getTranslateY(getDegree() * index);
        params.leftMargin = getTranslateX(getDegree() * index);
        viewList.get(index).setLayoutParams(params);
        viewList.get(index).setVisibility(View.VISIBLE);
    }

    //マージンを元に戻すメソッド
    public void resetMargin(int index) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewList.get(index).getLayoutParams();
        params.bottomMargin = 0;
        params.leftMargin = 0;
        viewList.get(index).setLayoutParams(params);
    }
}
