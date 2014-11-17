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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseQueryAdapter;

import java.util.List;

import it.gmariotti.cardslib.demo.extras.fragment.BaseListFragment;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.activities.BookDetailActivity;
import jp.caliconography.one_liners.adapter.ReviewAdapter;
import jp.caliconography.one_liners.model.parseobject.Review;

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
public class BookListFragment extends BaseListFragment {

    private boolean mIsLoading;
    private ReviewAdapter mReviewAdapter;

    public BookListFragment() {
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
        View root = inflater.inflate(R.layout.demo_extras_fragment_staggeredgrid, container, false);
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

        //Staggered grid view
        CardGridStaggeredView staggeredView = (CardGridStaggeredView) getActivity().findViewById(R.id.carddemo_extras_grid_stag);

        // 2014/09/18 安部追加
        if (staggeredView != null) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.button_floating_action);
            floatingActionButton.attachToListView(staggeredView);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(getActivity(), BookDetailActivity.class);
                    startActivity(detailIntent);
                }
            });

        }
        // 2014/09/18 安部追加

        //Set the empty view
        staggeredView.setEmptyView(getActivity().findViewById(android.R.id.empty));
        if (staggeredView != null) {

            mReviewAdapter = new ReviewAdapter(getActivity());
            mReviewAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Review>() {
                @Override
                public void onLoading() {
                    // do nothing
                }

                @Override
                public void onLoaded(List<Review> reviews, Exception e) {
                    displayList();
                    mIsLoading = false;
                }
            });
            staggeredView.setAdapter(mReviewAdapter);
        }

//        // 2014/11/04 安部追加
//        staggeredView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (!mIsLoading && totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
//                    // 最後尾までスクロールしたので、何かデータ取得する処理
//                    mIsLoading = true;
//                    mReviewAdapter.loadNextPage();
//
//                }
//            }
//        });
//        // 2014/11/04 安部追加
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.book_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            hideList(true);
            mReviewAdapter.clear();
            mReviewAdapter.loadObjects();
        }
        return super.onOptionsItemSelected(item);
    }
}
