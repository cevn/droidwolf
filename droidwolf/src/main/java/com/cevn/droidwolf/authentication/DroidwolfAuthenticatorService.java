package com.cevn.droidwolf.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sameer on 11/3/13.
 */
public class DroidwolfAuthenticatorService extends Service {
    public IBinder onBind(Intent intent) {
        DroidwolfAuthenticator authenticator = new DroidwolfAuthenticator(this);
        return authenticator.getIBinder();
    }
}
