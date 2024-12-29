package com.doan.pharcity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.pharcity.Fragment.AccountFragment;
import com.doan.pharcity.Fragment.HomeFragment;
import com.doan.pharcity.Fragment.Shoping_cartFragment;
import com.doan.pharcity.Login;
import com.doan.pharcity.R;

import com.doan.pharcity.SQLiteHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private RecyclerView RecyclerViewCategory;
    private NavigationView navigationView;
    private ListView listViewManHinhChinh;
    private DrawerLayout drawerLayout;
    private ImageView imageView;
    private Timer mTimer;
    private long backPressedTime;
    private Toast mToask;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    //Tạo để lưu trữ tạm thời

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteHelper dbHelper = new SQLiteHelper(this);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

//        clearLoginState(this);
//        Toast.makeText(this, "Đã đặt lại trạng thái đăng nhập!", Toast.LENGTH_SHORT).show();
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.homeFragment) {
                selectedFragment = new HomeFragment();

            } else if (item.getItemId() == R.id.shoping_cartFragment) {
                selectedFragment = new Shoping_cartFragment();
            } else if (item.getItemId() == R.id.accountFragment) {
                if (isUserLogin()) {
                    selectedFragment = new AccountFragment();
                } else {
                    // Chuyển hướng đến màn hình đăng nhập
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    return false;
                }
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return true;

        });
    }

    // Method to load fragment into the container
    private void loadFragment(Fragment fragment) {
        // Create a FragmentTransaction to add the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);  // Optional: if you want to add the transaction to the back stack
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            mToask.cancel();
            super.onBackPressed();
            return;
        } else {
            mToask = Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT);
            mToask.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    private boolean isUserLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("isUserLogin", false); // Đổi "isLoggedIn" thành "isUserLogin"
    }

    public  String getUserId(android.content.Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId",null);
    }

    public void clearLoginState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Xóa toàn bộ dữ liệu trong SharedPreferences
        editor.apply();
    }

}