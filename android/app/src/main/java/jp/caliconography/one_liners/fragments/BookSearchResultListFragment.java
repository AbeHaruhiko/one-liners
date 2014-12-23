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
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import it.gmariotti.cardslib.demo.extras.fragment.BaseListFragment;
import it.gmariotti.cardslib.demo.extras.staggered.data.ServerDatabase;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.event.BookSearchCompletedEvent;
import jp.caliconography.one_liners.model.BookSearchResult;
import jp.caliconography.one_liners.model.SearchResultCard;
import jp.caliconography.one_liners.model.parseobject.Review;
import jp.caliconography.one_liners.services.RakutenBooksTotalSearchClient;
import jp.caliconography.one_liners.util.BusHolder;

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

        inflater.inflate(R.menu.book_search, menu);

        // SearchViewを取得する
        MenuItem menuItem = menu.findItem(R.id.search_view);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getString(R.string.book_search_query_hint));
        searchView.setIconified(false);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            FragmentManager fm = getActivity().getFragmentManager();
            fm.popBackStack(BookDetailFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        for (final BookSearchResult.ItemHolder itemHolder : bookSearchResult.Items) {

//            PicassoCard card = new PicassoCard(this.getActivity(), Uri.parse(itemHolder.Item.mediumImageUrl.toString()));
            SearchResultCard card = new SearchResultCard(getActivity());

            // title
            card.setTitle(itemHolder.Item.title);

            // author
            card.setAuthor(itemHolder.Item.getAuthorAndPublisher());

            // thumbnail
            CardThumbnail thumbnail = new CardThumbnail(getActivity());
            thumbnail.setUrlResource(itemHolder.Item.mediumImageUrl.toString());
            card.addCardThumbnail(thumbnail);

            // click listener
            card.setOnClickListener(new Card.OnCardClickListener() {

                @Override
                public void onClick(Card card, View view) {
                    addSearchResultToReviewAndReturn(itemHolder.Item);
                }
            });

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

    private void addSearchResultToReviewAndReturn(BookSearchResult.ItemHolder.ItemProperties item) {
        Review review = ((BookDetailActivity) getActivity()).getCurrentReview();
        review.setTitle(item.title);
        review.setAuthor(item.getAuthorAndPublisher());
        review.setThumnnailUrl(item.largeImageUrl.toString());
        review.setAffiliateUrl(item.affiliateUrl.toString());
        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack(BookDetailFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
