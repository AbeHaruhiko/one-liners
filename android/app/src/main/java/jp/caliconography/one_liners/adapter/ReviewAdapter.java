package jp.caliconography.one_liners.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.widget.DynamicHeightParseImageView;

public class ReviewAdapter extends ParseQueryAdapter<Review> {

    public static final float TRANSPARENT = 0f;
    public static final float OPAQUE = 1f;
    public static final String ALPHA = "alpha";
    public static final int FADEIN_DURATION = 400;

    public ReviewAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Review>() {
            public ParseQuery<Review> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery(Review.class);
                query.orderByDescending(Review.KEY_CREATEDAT);
                return query;
            }
        });
        setPlaceholder(context.getResources().getDrawable(R.drawable.photo_placeholder));
        setObjectsPerPage(7);
    }

    @Override
    public View getItemView(Review review, View convertView, ViewGroup parent) {

//        if (convertView == null) {
            // recycle poolにviewがなかった場合。
            convertView = View.inflate(getContext(), R.layout.carddemo_extras_staggered_card, null);

            ParseFile photoFile = review.getParseFile(Review.KEY_PHOTO);
        DynamicHeightParseImageView photoView = (DynamicHeightParseImageView) convertView.findViewById(R.id.card_thumbnail_image);
        if (photoFile == null) {
            photoView.setHeightRatio(0d);
            photoView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.transparent_dot));
        } else {
                photoView.setHeightRatio(1d * review.getPhotoFileWidth() / review.getPhotoFileHeight());
                photoView.setParseFile(photoFile);
                photoView.loadInBackground();
            }

            // カードのコンテンツ部分
            FrameLayout mainContent = (FrameLayout) convertView.findViewById(R.id.card_main_content_layout);

            // レイアウトを読み込んで、FrameLayoutにセットする。
            View innerContent = View.inflate(getContext(), R.layout.carddemo_extras_staggered_inner_main, mainContent);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.carddemo_staggered_inner_title);
            titleTextView.setText(review.getTitle());

            TextView authorTextView = (TextView) convertView.findViewById(R.id.carddemo_staggered_inner_subtitle);
            authorTextView.setText(review.getAuthor());

            // フェイドインアニメーション
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(convertView, ALPHA, TRANSPARENT, OPAQUE);
            objectAnimator.setDuration(FADEIN_DURATION);
            objectAnimator.start();
//        }

//        super.getItemView(review, convertView, parent);

        return convertView;
    }

    @Override
    public View getNextPageView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            // recycle poolにviewがなかった場合。
            convertView = View.inflate(getContext(), R.layout.list_loading, null);
        }
        return convertView;
    }
}