package com.example.bazinga.samplelist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.bazinga.samplelist.callbacks.SmsListenerController;
import com.example.bazinga.samplelist.ui.activity.SingleInstanceActivity;
import com.example.bazinga.samplelist.ui.activity.SingleTaskActivity;
import com.example.bazinga.samplelist.ui.activity.SingleTopActivity;
import com.example.bazinga.samplelist.ui.activity.StandardActivity;

/**
 * Created by Bazinga on 5/8/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private final String ACTION_LAUNCH_MODE = "com.example.bazinga.intent.ACTION_LAUNCH_MODE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ABHI", "intent " + intent.getAction());
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = getSmsMessage(bundle, pdus[i]);

                    String messageBody = smsMessage.getDisplayMessageBody();
                    String messageAddress = smsMessage.getDisplayOriginatingAddress();
                    Log.d("ABHI", messageAddress + " " + messageBody);

                    int startIndex = messageBody.indexOf("TestOtp");
                    Log.d("ABHI", "startIndex " + startIndex);
                    if (startIndex != -1) {
                        int begin = startIndex + "TestOtp".length() + 1;
                        int end = begin + 6;
                        String extractedOtp = messageBody.substring(begin, end);
                        Log.d("ABHI", "Extracted OTP" + extractedOtp);

                        SmsListenerController.notifyOnSmsReceived(extractedOtp);
                    }

                }
            }
        } else if (intent.getAction().equals(ACTION_LAUNCH_MODE)) {
            int launchMode = intent.getIntExtra("launch_mode", 0);
            Log.d("ABHI", "launchMode " + launchMode);
            Intent launchIntent;
            switch (launchMode) {
                case 0:
                    launchIntent = new Intent(context, StandardActivity.class);
                    context.startActivity(launchIntent);
                    break;
                case 1:
                    launchIntent = new Intent(context, SingleTopActivity.class);
                    context.startActivity(launchIntent);
                    break;
                case 2:
                    launchIntent = new Intent(context, SingleTaskActivity.class);
                    context.startActivity(launchIntent);
                    break;
                case 3:
                    launchIntent = new Intent(context, SingleInstanceActivity.class);
                    context.startActivity(launchIntent);
                    break;

            }
        }

    }


    private SmsMessage getSmsMessage(Bundle bundle, Object pdu) {
        SmsMessage currentSmsMessage = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSmsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
        } else {
            currentSmsMessage = SmsMessage.createFromPdu((byte[]) pdu);
        }
        return currentSmsMessage;
    }
}
