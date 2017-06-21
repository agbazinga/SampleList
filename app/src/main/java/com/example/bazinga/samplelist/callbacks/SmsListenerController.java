package com.example.bazinga.samplelist.callbacks;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Bazinga on 5/9/2017.
 */

public class SmsListenerController {

    private static CopyOnWriteArrayList<SmsListener> sSmsListenersList = new CopyOnWriteArrayList<>();

    public static void addSmsListener(SmsListener smsListener) {
        if (!sSmsListenersList.contains(smsListener)) {
            sSmsListenersList.add(smsListener);
        }
    }

    public static void removeSmsListener(SmsListener smsListener) {
        if (sSmsListenersList.contains(smsListener)) {
            sSmsListenersList.remove(smsListener);
        }
    }

    public static void notifyOnSmsReceived(String message) {
        if (!sSmsListenersList.isEmpty()) {
            for (SmsListener smsListener : sSmsListenersList) {
                smsListener.onMessageReceived(message);
            }
        }
    }
}
