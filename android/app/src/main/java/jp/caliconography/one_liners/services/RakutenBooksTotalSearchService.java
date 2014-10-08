package jp.caliconography.one_liners.services;

import java.net.URI;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface RakutenBooksTotalSearchService {
    @GET("/services/api/BooksTotal/Search/20130522")
    void searchBook(@QueryMap Map<String, String> options, Callback<BookSearchResult> callback);

    /* Gsonはクラス名は自由、変数名をJSONのKeyにすること */
    static class BookSearchResult {
        public int count;
        public List<ItemHolder> Items;

        public static class ItemHolder {
            public ItemProperties Item;

            public static class ItemProperties {
                public String title;
                public String author;
                public String publisherName;
                public String salesDate;
                public URI itemUrl;
                public URI smallImageUrl;
                public URI mediumImageUrl;
            }
        }
    }

}