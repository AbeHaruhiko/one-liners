package jp.caliconography.one_liners.event;

import jp.caliconography.one_liners.model.BookSearchResult;

/**
 * Created by abe on 2014/10/08.
 */
public class BookSearchCompletedEvent {
    private final BookSearchResult mBookSearchResult;

    public BookSearchCompletedEvent(final BookSearchResult bookSearchResult) {
        mBookSearchResult = bookSearchResult;
    }

    public BookSearchResult getBookSearchResult() {
        return mBookSearchResult;
    }
}
