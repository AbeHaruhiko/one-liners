package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import jp.caliconography.one_liners.util.Utils;

/**
 * Created by abeharuhiko on 2014/11/06.
 */
public class DynamicHeightParseImageView extends ParseImageView {

    private ParseFile mParseFile;
    private double mHeightRatio;

    public DynamicHeightParseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightParseImageView(Context context) {
        super(context);
    }

    public ParseFile getParseFile() {
        return mParseFile;
    }

    @Override
    public void setParseFile(ParseFile file) {
        super.setParseFile(file);
        this.mParseFile = file;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("layout_width must be match_parent");
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        // Honor aspect ratio for height but no larger than 2x width.
        int height = Math.min((int) (width / mHeightRatio), width * 2);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Load the image
        if (getParseFile() != null) {
            getParseFile().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    setImageBitmap(Utils.getBitmapFromByteArray(bytes));
                }
            });
        }
    }
}
