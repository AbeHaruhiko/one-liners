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

package jp.caliconography.one_liners.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import it.gmariotti.cardslib.demo.extras.cards.PicassoCard;
import it.gmariotti.cardslib.demo.extras.fragment.BaseListFragment;
import it.gmariotti.cardslib.demo.extras.staggered.data.ServerDatabase;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import jp.caliconography.android.util.BusHolder;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.event.BookSearchCompletedEvent;
import jp.caliconography.one_liners.model.BookSearchResult;
import jp.caliconography.one_liners.services.RakutenBooksTotalSearchClient;

/**
 * This example uses a staggered card with different different photos and text.
 * <p/>
 * This example uses cards with a foreground layout.
 * Pay attention to style="@style/card.main_layout_foreground" in card layout.
 * <p/>
 * .DynamicHeightPicassoCardThumbnailView
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class BookSearchResultListFragment extends BaseListFragment {

    ServerDatabase mServerDatabase;
    CardArrayAdapter mCardArrayAdapter;

    public BookSearchResultListFragment() {
        super();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.carddemo_extras_title_staggered;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FragmentでMenuを表示する為に必要
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_search_result_list, container, false);
        setupListFragment(root);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hideList(false);
        mProgressContainer.setVisibility(View.GONE);

        //Set the arrayAdapter
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        //Staggered grid view
        CardListView cardListView = (CardListView) getActivity().findViewById(R.id.book_search_result_list);

        //Set the empty view
        cardListView.setEmptyView(getActivity().findViewById(android.R.id.empty));
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(cardListView);
            cardListView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
        }

        //Load cards
//        new LoaderAsyncTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        final SearchView searchView = (SearchView) menuItem.getActionView();

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {

                mCardArrayAdapter.clear();
                searchView.clearFocus();
                mProgressContainer.setVisibility(View.VISIBLE);

                new RakutenBooksTotalSearchClient().search(queryText);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }

    @Subscribe
    public void onBookSearchCompleted(BookSearchCompletedEvent event) {
        displayResult(event.getBookSearchResult());
    }

    private void displayResult(BookSearchResult bookSearchResult) {
        ArrayList<Card> cards = initCard(bookSearchResult);
        updateAdapter(cards);
        displayList();
    }

    private ArrayList<Card> initCard(BookSearchResult bookSearchResult) {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (BookSearchResult.ItemHolder itemHolder : bookSearchResult.Items) {

            PicassoCard card = new PicassoCard(this.getActivity(), Uri.parse(itemHolder.Item.mediumImageUrl.toString()));
            card.setOnClickListener(new Card.OnCardClickListener() {

                @Override
                public void onClick(Card card, View view) {
                    PicassoCard picassoCard = (PicassoCard) card;

                    // TODO 呼び出し元でintent作成メソッドを定義する。
                    Intent intent = new Intent();
                    intent.putExtra("title", picassoCard.getTitle());
                    intent.putExtra("author", picassoCard.getSecondaryTitle());
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            });
            card.setTitle(itemHolder.Item.title);
            card.setSecondaryTitle(itemHolder.Item.author + " / " + itemHolder.Item.publisherName);

            cards.add(card);
        }

        return cards;
//        return null;
    }

    /**
     * Update the adapter
     */
    private void updateAdapter(ArrayList<Card> cards) {
        if (cards != null) {
            mCardArrayAdapter.addAll(cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        BusHolder.get().register(this);
    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);

        super.onPause();
    }
}
