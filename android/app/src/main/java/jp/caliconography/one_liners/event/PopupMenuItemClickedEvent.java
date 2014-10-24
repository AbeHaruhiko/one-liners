package jp.caliconography.one_liners.event;

/**
 * Created by abe on 2014/10/24.
 */
public class PopupMenuItemClickedEvent {
    private final int id;

    public PopupMenuItemClickedEvent(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
