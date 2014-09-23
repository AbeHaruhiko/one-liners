/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package it.gmariotti.cardslib.demo.extras.cards;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;

import jp.caliconography.one_liners.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * This class provides a simple card with Thumbnail loaded with built-in method and Picasso library
 * Please refer to https://github.com/square/picasso for full doc
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class PicassoCard extends Card {

    protected String mTitle;
    protected String mSecondaryTitle;
    protected int count;

    public PicassoCard(Context context, Uri thumbnailUri) {
        this(context, R.layout.carddemo_extra_picasso_inner_content, thumbnailUri);
    }

    public PicassoCard(Context context, int innerLayout, Uri thumbnailUri) {
        super(context, innerLayout);
        init(thumbnailUri);
    }

    private void init(Uri thumbnailUri) {

        //Add thumbnail
        PicassoCardThumbnail cardThumbnail = new PicassoCardThumbnail(mContext, thumbnailUri);
        //It must be set to use a external library!
        cardThumbnail.setExternalUsage(true);
        addCardThumbnail(cardThumbnail);

        //Add ClickListener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                String cardTitle= mId!=null? mId: mTitle;
//                Toast.makeText(getContext(), "Click Listener card="+cardTitle, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView title = (TextView) parent.findViewById(R.id.carddemo_extra_picasso_main_inner_title);
        TextView secondaryTitle = (TextView) parent.findViewById(R.id.carddemo_extra_picasso_main_inner_secondaryTitle);

        if (title != null)
            title.setText(mTitle);

        if (secondaryTitle != null)
            secondaryTitle.setText(mSecondaryTitle);

    }

    /**
     * CardThumbnail which uses Picasso Library.
     * If you use an external library you have to provide your login inside #setupInnerViewElements.
     *
     * This method is called before built-in method.
     * If {@link it.gmariotti.cardslib.library.internal.CardThumbnail#isExternalUsage()} is false it uses the built-in method.
     */
    class PicassoCardThumbnail extends CardThumbnail {

        Uri mThumbnailUri;

        public PicassoCardThumbnail(Context context, Uri thumbnailUri) {
            super(context);
            mThumbnailUri = thumbnailUri;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {

            /*
             * If your cardthumbnail uses external library you have to provide how to load the image.
             * If your cardthumbnail doesn't use an external library it will use a built-in method
             */

            //Here you have to set your image with an external library
            //Only for test, use a Resource Id and a Url

            //It is just an example !

            if (((PicassoCard) getParentCard()).getCount() % 2 == 0) {
                Picasso.with(getContext()).setIndicatorsEnabled(true);  //only for debug tests
                Picasso.with(getContext())
                        .load(mThumbnailUri)
                        .error(R.drawable.ic_error_loadingsmall)
                        .into((ImageView) viewImage);
            } else {
                Picasso.with(getContext()).setIndicatorsEnabled(true);  //only for debug tests
                Picasso.with(getContext())
                        .load(R.drawable.ic_tris)
                        .resize(96, 96)
                        .into((ImageView) viewImage);
            }
            /*
            viewImage.getLayoutParams().width = 96;
            viewImage.getLayoutParams().height = 96;
            */
        }
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSecondaryTitle() {
        return mSecondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        mSecondaryTitle = secondaryTitle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}