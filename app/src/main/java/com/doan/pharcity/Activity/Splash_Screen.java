package com.doan.pharcity.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.doan.pharcity.databinding.ActivitySplashScreenBinding;

public class Splash_Screen extends BaseActivity {
    
    private ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

//        binding.startBnt.setOnClickListener(view -> {
//            Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }



}