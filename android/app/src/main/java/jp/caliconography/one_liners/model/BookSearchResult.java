package jp.caliconography.one_liners.model;

import java.net.URI;
import java.util.List;

/* Gsonはクラス名は自由、変数名をJSONのKeyにすること */
public class BookSearchResult {
    public int count;
    public List<ItemHolder> Items;

    public class ItemHolder {
        public ItemProperties Item;

        public class ItemProperties {
            public String title;
            public String author;
            public String publisherName;
            public String salesDate;
            public URI itemUrl;
            public URI smallImageUrl;
            public URI mediumImageUrl;
            public URI largeImageUrl;
            public URI affiliateUrl;

            public String getAuthorAndPublisher() {
                return author
                        + ((author.isEmpty() || publisherName.isEmpty()) ? "" : " / ")
                        + publisherName;
            }
        }
    }
}
