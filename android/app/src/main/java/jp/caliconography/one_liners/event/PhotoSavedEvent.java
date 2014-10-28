package jp.caliconography.one_liners.event;

import com.parse.ParseFile;

/**
 * Created by abe on 2014/10/28.
 */
public class PhotoSavedEvent {
    private final ParseFile mFile;

    public PhotoSavedEvent(final ParseFile file) {
        this.mFile = file;
    }

    public ParseFile getFile() {
        return mFile;
    }
}
