package com.doan.pharcity;

import static com.doan.pharcity.R.id.menu_tad_2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_page);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPageAddapter viewPageAddapter = new ViewPageAddapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPageAddapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tad_1).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tad_2).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tad_3).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tad_4).setChecked(true);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if (item.getItemId() == R.id.menu_tad_1) {
                   mViewPager.setCurrentItem(0);
                   home_Fragment homeFragment = (home_Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
                   homeFragment.reloaddata();
                   return true;
               } else if (item.getItemId() == R.id.menu_tad_2) {
                   mViewPager.setCurrentItem(1);
                   return true;
               } else if (item.getItemId() == R.id.menu_tad_3) {
                   mViewPager.setCurrentItem(2);
                   return true;
               } else if (item.getItemId() == R.id.menu_tad_4) {
                   mViewPager.setCurrentItem(3);
                   return true;
               }
               return false;
           }
       });

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment =fragmentManager.findFragmentById(R.id.f)
    }
}