package jp.caliconography.one_liners.services;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import jp.caliconography.one_liners.event.BookSearchCompletedEvent;
import jp.caliconography.one_liners.model.BookSearchResult;
import jp.caliconography.one_liners.util.BusHolder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public class RakutenBooksTotalSearchClient {

    private static final String RAKUTEN_API_ENDPOINT = "https://app.rakuten.co.jp/";
    public static final String APP_ID = "1014192012049542780";
    public static final String BOOK_GENRE_ID_BOOK = "000";
    public static final String FORMAT_JSON = "json";

    public interface RakutenBooksTotalSearch {
        @GET("/services/api/BooksTotal/Search/20130522")
        void searchBook(@QueryMap Map<String, String> options, Callback<BookSearchResult> callback);
    }

    public void search(String queryText) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(RAKUTEN_API_ENDPOINT)
                .build();

        RakutenBooksTotalSearchClient.RakutenBooksTotalSearch service = restAdapter.create(RakutenBooksTotalSearchClient.RakutenBooksTotalSearch.class);
        service.searchBook(buildParamMap(queryText), getBookSearchResultCallback());
    }

    private Callback<BookSearchResult> getBookSearchResultCallback() {
        return new Callback<BookSearchResult>() {
            @Override
            public void success(BookSearchResult result, Response response) {
                BusHolder.get().post(new BookSearchCompletedEvent(result));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("", null, retrofitError);
            }
        };
    }

    private Map<String, String> buildParamMap(String queryText) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("applicationId", APP_ID);
        paramMap.put("format", FORMAT_JSON);
        paramMap.put("booksGenreId", BOOK_GENRE_ID_BOOK);
        paramMap.put("keyword", queryText);
        return paramMap;
    }
}
