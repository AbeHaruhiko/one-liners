package jp.caliconography.one_liners;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface GitHubService {
  @GET("/services/api/BooksBook/Search/20130522")
  void searchBook(@QueryMap Map<String, String> options, Callback<BookSearchResult> callback);

    /* Gsonはクラス名は自由、変数名をJSONのKeyにすること */
    static class BookSearchResult {
        int count;
        List<ItemHolder> Items;

        static class ItemHolder {
            ItemProperties Item;

            static class ItemProperties {
                String title;
                String author;
                String publisherName;
                String salesDate;
                URI itemUrl;
                URI smallImageUrl;
                URI mediumImageUrl;
            }
        }
    }

}