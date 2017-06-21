package com.example.bazinga.samplelist.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bazinga.samplelist.R;

import java.util.ArrayList;

public class AnimationTestActivity extends AppCompatActivity {

    private EditText inputEditText;

    private FloatingActionButton fabInfo;
    private FloatingActionButton fabEmail;
    private FloatingActionButton fabMaps;

    private TextView badgeTextView;

    private boolean isFabOpen = false;
    private boolean isPositionSet = false;

    float animEmailDeltaY = 0f;
    float animMapsDeltaY = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabInfo = (FloatingActionButton) findViewById(R.id.fab_info);
        fabEmail = (FloatingActionButton) findViewById(R.id.fab_email);
        fabMaps = (FloatingActionButton) findViewById(R.id.fab_map);

        inputEditText = (EditText) findViewById(R.id.input_edit_text);

        badgeTextView = (TextView) findViewById(R.id.badge_text);

        Animation animFabInfo = AnimationUtils.loadAnimation(this, R.anim.fab_fade_in);

        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Log.d("ABHI", "" + fabEmail.getY() + " " + fabInfo.getY());

                badgeTextView.setText(inputEditText.getText());

                if (!isPositionSet) {
                    animEmailDeltaY = fabEmail.getY() - fabInfo.getY();
                    animMapsDeltaY = fabMaps.getY() - fabInfo.getY();
                    isPositionSet = true;
                }

                if (!isFabOpen) {
                    startFadeInAnimation(animEmailDeltaY, animMapsDeltaY);
                    isFabOpen = true;
                } else {
                    startFadeOutAnimation(animEmailDeltaY, animMapsDeltaY);
                    isFabOpen = false;
                }

            }
        });
        fabInfo.startAnimation(animFabInfo);
    }

    private void startFadeInAnimation(float animEmailDeltaY, float animMapsDeltaY) {

        ObjectAnimator animEmailAlphaFadeIn = ObjectAnimator.ofFloat(fabEmail, "alpha", 0f, 1f);
        ObjectAnimator animMapsAlphaFadeIn = ObjectAnimator.ofFloat(fabMaps, "alpha", 0f, 1f);
        ObjectAnimator animEmailTranslateYFadeIn = ObjectAnimator.ofFloat(fabEmail, "translationY", -animEmailDeltaY, 0);
        ObjectAnimator animMapsTranslateYFadeIn = ObjectAnimator.ofFloat(fabMaps, "translationY", -animMapsDeltaY, 0);

        ObjectAnimator animRotate = ObjectAnimator.ofFloat(fabInfo, "rotation", 45f);

        ArrayList<Animator> animatorArrayListFadeIn = new ArrayList<>();
        animatorArrayListFadeIn.add(animEmailAlphaFadeIn);
        animatorArrayListFadeIn.add(animMapsAlphaFadeIn);
        animatorArrayListFadeIn.add(animEmailTranslateYFadeIn);
        animatorArrayListFadeIn.add(animMapsTranslateYFadeIn);
        animatorArrayListFadeIn.add(animRotate);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.playTogether(animatorArrayListFadeIn);
        animatorSet.start();
    }

    private void startFadeOutAnimation(float animEmailDeltaY, float animMapsDeltaY) {

        ObjectAnimator animEmailAlphaFadeOut = ObjectAnimator.ofFloat(fabEmail, "alpha", 1f, 0f);
        ObjectAnimator animMapsAlphaFadeOut = ObjectAnimator.ofFloat(fabMaps, "alpha", 1f, 0f);
        ObjectAnimator animEmailTranslateYFadeOut = ObjectAnimator.ofFloat(fabEmail, "translationY", 0, -animEmailDeltaY);
        ObjectAnimator animMapsTranslateYFadeOut = ObjectAnimator.ofFloat(fabMaps, "translationY", 0, -animMapsDeltaY);

        ObjectAnimator animRotate = ObjectAnimator.ofFloat(fabInfo, "rotation", 0);

        ArrayList<Animator> animatorArrayListFadeOut = new ArrayList<>();
        animatorArrayListFadeOut.add(animEmailAlphaFadeOut);
        animatorArrayListFadeOut.add(animMapsAlphaFadeOut);
        animatorArrayListFadeOut.add(animEmailTranslateYFadeOut);
        animatorArrayListFadeOut.add(animMapsTranslateYFadeOut);
        animatorArrayListFadeOut.add(animRotate);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.playTogether(animatorArrayListFadeOut);
        animatorSet.start();
    }
}
