package jp.caliconography.one_liners.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
        setObjectsPerPage(3);
    }

    @Override
    public View getItemView(Review review, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.carddemo_extras_staggered_card, null);
        }

        super.getItemView(review, v, parent);

//        ParseImageView photoView = (ParseImageView) v.findViewById(R.id.card_thumbnail_image);
//        ParseFile photoFile = review.getParseFile("photoView");
//        if (photoFile != null) {
//            photoView.setParseFile(photoFile);
//            photoView.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    // nothing to do
//                }
//            });
//        }

        // Picassoを通さず直接ParseImageViewにセットしている。
        DynamicHeightParseImageView photoView = (DynamicHeightParseImageView) v.findViewById(R.id.card_thumbnail_image);
        photoView.setHeightRatio(1d * review.getPhotoFileWidth() / review.getPhotoFileHeight());
        ParseFile photoFile = review.getParseFile(Review.KEY_PHOTO);
        if (photoFile != null) {
            photoView.setParseFile(photoFile);
            photoView.loadInBackground();
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.carddemo_staggered_inner_title);
//        titleTextView.setText(review.getTitle());

        // フェイドインアニメーション
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, ALPHA, TRANSPARENT, OPAQUE);
        objectAnimator.setDuration(FADEIN_DURATION);
        objectAnimator.start();

        return v;
    }
}