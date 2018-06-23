package com.example.nadza.booktastic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    LinearLayout wS;
    DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataHolder.databaseHelper= new BazaOpenHelper(this);
        DataHolder.updateData();
        //pozvati update iz staticke klase
        Stetho.initializeWithDefaults(this);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        wS=(LinearLayout)findViewById(R.id.welcomeScreen);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    wS.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_add:
                    fragment = new AddOnlineFragment();
                    wS.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_map:
                    fragment = new MapFragment();
                    wS.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    return true;
            }
            return false;
        }
    };

}
