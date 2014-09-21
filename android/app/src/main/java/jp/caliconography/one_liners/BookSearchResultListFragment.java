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

package jp.caliconography.one_liners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.demo.extras.cards.PicassoCard;
import it.gmariotti.cardslib.demo.extras.fragment.BaseListFragment;
import it.gmariotti.cardslib.demo.extras.staggered.DynamicHeightPicassoCardThumbnailView;
import it.gmariotti.cardslib.demo.extras.staggered.data.Image;
import it.gmariotti.cardslib.demo.extras.staggered.data.MockImageLoader;
import it.gmariotti.cardslib.demo.extras.staggered.data.Section;
import it.gmariotti.cardslib.demo.extras.staggered.data.ServerDatabase;
import it.gmariotti.cardslib.library.extra.staggeredgrid.internal.CardGridStaggeredArrayAdapter;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        new LoaderInitAsyncTask(activity).execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hideList(false);

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
            cardListView.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
        }

        //Load cards
//        new LoaderAsyncTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("https://app.rakuten.co.jp/")
                        .build();

                GitHubService service = restAdapter.create(GitHubService.class);

                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("applicationId", "1014192012049542780");
                paramMap.put("title", "テスト");
                paramMap.put("format", "json");
                paramMap.put("booksGenreId", "001004008");

                service.searchBook(paramMap, new Callback<GitHubService.BookSearchResult>() {
                    @Override
                    public void success(GitHubService.BookSearchResult result, Response response) {
                        Log.d("", result.toString());
                        Log.d("", response.toString());

                        ArrayList<Card> cards = initCard(result);
                        updateAdapter(cards);
                        displayList();

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e("", null, retrofitError);
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private ArrayList<Card> initCard(GitHubService.BookSearchResult result) {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (GitHubService.BookSearchResult.ItemHolder itemHolder: result.Items) {

            PicassoCard card = new PicassoCard(this.getActivity(), Uri.parse(itemHolder.Item.mediumImageUrl.toString()));
            card.setTitle(itemHolder.Item.title);
            card.setSecondaryTitle(itemHolder.Item.author + " / " + itemHolder.Item.publisherName);

            cards.add(card);
        }

        return cards;
//        return null;
    }

    //-------------------------------------------------------------------------------------------------------------
    // Images loader
    //-------------------------------------------------------------------------------------------------------------

    /**
     * Async Task to init images
     */
    class LoaderInitAsyncTask extends AsyncTask<Void, Void, Void> {

        Context mContext;


        LoaderInitAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Initialize Image loader
            MockImageLoader loader = MockImageLoader.getInstance(((Activity) mContext).getApplication());
            mServerDatabase = new ServerDatabase(loader);
            return null;
        }
    }

    /**
     * Async Task to elaborate images
     */
    class LoaderAsyncTask extends AsyncTask<Void, Void, ArrayList<Card>> {

        LoaderAsyncTask() {
        }

        @Override
        protected ArrayList<Card> doInBackground(Void... params) {
            //elaborate images
            SystemClock.sleep(1000); //delay to simulate download, don't use it in a real app
            mServerDatabase.getImagesForSection(Section.STAG);
            ArrayList<Card> cards = initCard();
            return cards;
        }

        @Override
        protected void onPostExecute(ArrayList<Card> cards) {
            //Update the adapter
            updateAdapter(cards);
            displayList();
        }
    }


    /**
     * This method builds a simple list of cards
     */
    private ArrayList<Card> initCard() {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 100; i++) {

            PicassoCard card = new PicassoCard(this.getActivity(), null);
            card.setTitle("A simple card loaded with Picasso " + i);
            card.setSecondaryTitle("Simple text..." + i);

            cards.add(card);
        }

        return cards;

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


    //-------------------------------------------------------------------------------------------------------------
    // Cards
    //-------------------------------------------------------------------------------------------------------------


    /**
     * Card
     */
//    public class StaggeredCard extends Card {
//
//        protected int height;
//        protected String headerTitle;
//
//        protected Image image;
//
//        public StaggeredCard(Context context) {
//            super(context, R.layout.carddemo_extras_staggered_inner_main);
//        }
//
//        private void init() {
//            /*
//            //Add the header
//            CardHeader header = new CardHeader(getContext());
//            header.setTitle(headerTitle);
//            header.setPopupMenu(R.menu.extras_popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//                @Override
//                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(),"Header:"+ ((Card) card).getCardHeader().getTitle() + "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            addCardHeader(header);
//            */
//
//            //Add the thumbnail
//            StaggeredCardThumb thumbnail = new StaggeredCardThumb(getContext());
//            thumbnail.image = image;
//            addCardThumbnail(thumbnail);
//
//            //A simple clickListener
//            setOnClickListener(new OnCardClickListener() {
//                @Override
//                public void onClick(Card card, View view) {
//                    //Do something
//                }
//            });
//        }
//
//        @Override
//        public void setupInnerViewElements(ViewGroup parent, View view) {
//
//            TextView title = (TextView) view.findViewById(R.id.carddemo_staggered_inner_title);
//            title.setText(image.title.toUpperCase());
//
//            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_staggered_inner_subtitle);
//            subtitle.setText(getString(R.string.carddemo_extras_title_stag));
//        }
//
//
//        /**
//         * A StaggeredCardThumbnail.
//         * It uses a DynamicHeightPicassoCardThumbnailView which  maintains its own width to height ratio.
//         */
//        class StaggeredCardThumb extends CardThumbnail {
//
//            Image image;
//
//            public StaggeredCardThumb(Context context) {
//                super(context);
//                setExternalUsage(true);
//            }
//
//            @Override
//            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
//
//                //Use a DynamicHeightPicassoCardThumbnailView to maintain width/height ratio
//                DynamicHeightPicassoCardThumbnailView thumbView = (DynamicHeightPicassoCardThumbnailView) getCardThumbnailView();
//                thumbView.bindTo(image);
//
//            }
//        }
//
//    }


}
