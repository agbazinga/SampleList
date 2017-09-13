package com.example.bazinga.samplelist.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.callbacks.SmsListener;
import com.example.bazinga.samplelist.callbacks.SmsListenerController;
import com.example.bazinga.samplelist.ui.views.PinEntryView;
import com.github.glomadrian.codeinputlib.CodeInput;

/**
 * Created by Bazinga on 5/8/2017.
 */

public class OtpDetectorActivity extends Activity implements SmsListener {

    private EditText otpEditText;
    private TextView textDescription;
    public static int PERMISSION_REQUEST_CODE = 1000;
    private PinEntryView pinEntryView;
    private CodeInput codeInput;

    private LinearLayout mDpiTestContainer;
    private TextView mDpiOne, mDpiTwo, mDpiThree, mSampleTextView;
    private float density, densityDpi;
    private DisplayMetrics mDisplayMetrics;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_detector);
        String permissionsRequired[] = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};
        checkPermissions(permissionsRequired);
        otpEditText = (EditText) findViewById(R.id.et_otp);
        textDescription = (TextView) findViewById(R.id.tv_otp_description);
        codeInput = (CodeInput) findViewById(R.id.otp_code_input);
        pinEntryView = (PinEntryView) findViewById(R.id.pin_entry_simple);
        mDpiTestContainer = (LinearLayout) findViewById(R.id.dpi_test_parent);
        mDpiOne = (TextView) findViewById(R.id.dpi_test_tv_one);
        mDpiTwo = (TextView) findViewById(R.id.dpi_test_tv_two);
        mDpiThree = (TextView) findViewById(R.id.dpi_test_tv_three);
        mSampleTextView = (TextView) findViewById(R.id.sample_text_view);
        SmsListenerController.addSmsListener(this);
        mSampleTextView.setSelected(true);
        mDisplayMetrics = getResources().getDisplayMetrics();
        densityDpi = mDisplayMetrics.densityDpi;
        density = mDisplayMetrics.density;

        mDpiOne.setText("WP:" + mDisplayMetrics.widthPixels + " HP:" + mDisplayMetrics.heightPixels);
        mDpiTwo.setText("Density:" + mDisplayMetrics.density + " ScaledDensity:" + mDisplayMetrics.scaledDensity);
        mDpiThree.setText("Density DPI " + densityDpi);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textDescription.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textDescription.setText("done!");
            }
        }.start();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager.isNotificationPolicyAccessGranted()) {

            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "notification access policy activity not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pinEntryView.setText("4444");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int width = mDpiTestContainer.getWidth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmsListenerController.removeSmsListener(this);
    }

    @Override
    public void onMessageReceived(String message) {
        //Toast.makeText(this, "Sms Recieved : " + message, Toast.LENGTH_SHORT).show();
        otpEditText.setText(message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            for (int grantResult : grantResults) {
                permissionCheck += grantResult;
            }
            if (grantResults.length > 0 && permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "All Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @TargetApi(23)
    private void checkPermissions(final String[] permissions) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRationale = false;
        for (String permission : permissions) {
            permissionCheck += checkSelfPermission(permission);
            showRationale = showRationale || shouldShowRequestPermissionRationale(permission);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRationale) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Grant Permissions").setTitle("Grant permissions to continue app usage").setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                    }
                }).create();
                builder.show();
            } else {
                // Request permission first time.
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "Required permissions granted", Toast.LENGTH_SHORT).show();
        }
    }
}
