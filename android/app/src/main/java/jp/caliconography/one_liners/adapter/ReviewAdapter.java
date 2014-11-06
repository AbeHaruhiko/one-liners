package jp.caliconography.one_liners.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import it.gmariotti.cardslib.demo.extras.staggered.DynamicHeightPicassoCardThumbnailView;
import it.gmariotti.cardslib.demo.extras.staggered.data.Image;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.model.parseobject.Review;

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
        DynamicHeightPicassoCardThumbnailView photoView = (DynamicHeightPicassoCardThumbnailView) v.findViewById(R.id.card_thumbnail_layout);
        ParseFile photoFile = review.getParseFile("photo");
        if (photoFile != null) {
            photoView.bindTo(new Image(null, photoFile.getUrl(), null, review.getPhotoFileWidth(), review.getPhotoFileHeight(), 0, 0));
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.carddemo_staggered_inner_title);
//        titleTextView.setText(review.getTitle());
        return v;
    }
}