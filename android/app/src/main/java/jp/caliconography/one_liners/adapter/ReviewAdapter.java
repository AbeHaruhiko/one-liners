package jp.caliconography.one_liners.adapter;

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

    public ReviewAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Review>() {
            public ParseQuery<Review> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery(Review.class);
                query.orderByDescending("createdAt");
                return query;
            }
        });
        setPlaceholder(context.getResources().getDrawable(R.drawable.photo_placeholder));
        setObjectsPerPage(1);
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
        ParseFile photoFile = review.getParseFile("photo");
        if (photoFile != null) {
            photoView.setParseFile(photoFile);
            photoView.loadInBackground();
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.carddemo_staggered_inner_title);
//        titleTextView.setText(review.getTitle());
        return v;
    }
}