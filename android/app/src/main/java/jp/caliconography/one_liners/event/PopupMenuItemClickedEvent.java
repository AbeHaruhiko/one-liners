package jp.caliconography.one_liners.event;

import jp.caliconography.one_liners.widget.PopupMenuItem;

/**
 * Created by abe on 2014/10/24.
 */
public class PopupMenuItemClickedEvent {
    private final PopupMenuItem mItem;

    public PopupMenuItemClickedEvent(final PopupMenuItem popupMenuItem) {
        this.mItem = popupMenuItem;
    }

    public PopupMenuItem getItem() {
        return mItem;
    }
}
