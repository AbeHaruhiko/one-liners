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

package jp.caliconography.one_liners.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;
import jp.caliconography.one_liners.R;

/**
 * This class provides a simple card with Thumbnail loaded with built-in method and Picasso library
 * Please refer to https://github.com/square/picasso for full doc
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SearchResultCard extends Card {

    protected String mTitle;
    protected String mAuthor;

    public SearchResultCard(Context context) {
        this(context, R.layout.carddemo_extra_picasso_inner_content);
    }

    public SearchResultCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView title = (TextView) parent.findViewById(R.id.carddemo_extra_picasso_main_inner_title);
        TextView secondaryTitle = (TextView) parent.findViewById(R.id.carddemo_extra_picasso_main_inner_secondaryTitle);

        if (title != null)
            title.setText(mTitle);

        if (secondaryTitle != null)
            secondaryTitle.setText(mAuthor);

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }
}
