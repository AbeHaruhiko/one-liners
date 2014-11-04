package jp.caliconography.one_liners.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

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
    public View getItemView(Review mereviewl, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.carddemo_extras_staggered_card, null);
        }

        super.getItemView(mereviewl, v, parent);

        ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = mereviewl.getParseFile("photo");
        if (photoFile != null) {
            mealImage.setParseFile(photoFile);
            mealImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
        titleTextView.setText(mereviewl.getTitle());
        TextView ratingTextView = (TextView) v
                .findViewById(R.id.favorite_meal_rating);
        ratingTextView.setText(mereviewl.getRating());
        return v;
    }
}