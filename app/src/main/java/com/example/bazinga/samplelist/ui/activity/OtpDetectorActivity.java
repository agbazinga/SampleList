package com.example.bazinga.samplelist.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
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

        SmsListenerController.addSmsListener(this);


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textDescription.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textDescription.setText("done!");
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pinEntryView.setText("4444");
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
