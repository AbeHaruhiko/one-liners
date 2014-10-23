package jp.caliconography.one_liners.util;

import com.squareup.otto.Bus;

public class BusHolder {
    private static Bus mBus = new Bus();

    public static Bus get() {
        return mBus;
    }
}