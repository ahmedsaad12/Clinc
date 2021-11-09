package com.clinc.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.clinc.R;
import com.clinc.activities_fragments.activity_home.HomeActivity;
import com.clinc.databinding.ActivitySplashBinding;
import com.clinc.language.Language;
import com.clinc.preferences.Preferences;
import com.clinc.tags.Tags;

import io.paperdb.Paper;

public class Splash_Activity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private Animation animation;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        preferences = Preferences.getInstance();

        animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.lanuch);
        binding.logo.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                    Intent intent=new Intent(Splash_Activity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
