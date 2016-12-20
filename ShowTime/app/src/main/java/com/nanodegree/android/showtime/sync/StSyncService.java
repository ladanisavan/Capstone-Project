package com.nanodegree.android.showtime.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();

    private static StSyncAdapter sStSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sStSyncAdapter == null) {
                sStSyncAdapter =
                        new StSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sStSyncAdapter.getSyncAdapterBinder();
    }
}