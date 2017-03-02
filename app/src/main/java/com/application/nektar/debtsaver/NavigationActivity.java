package com.application.nektar.debtsaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.application.nektar.debtsaver.login.LoginActivity;
import com.application.nektar.debtsaver.navigation.AddFragment;
import com.application.nektar.debtsaver.navigation.HomeFragment;
import com.application.nektar.debtsaver.navigation.StatsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by pc on 22.02.2017.
 */

public class NavigationActivity extends AppCompatActivity {
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFragmentManager = getSupportFragmentManager();
        mFragment = HomeFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.fragment_container,mFragment).commit();


        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:{
                        mFragment = new HomeFragment();
                        break;
                    }
                    case R.id.action_add:{
                        mFragment = new AddFragment();
                        break;
                    }
                    case R.id.action_stats:{
                        mFragment = new StatsFragment();
                        break;
                    }
                }
                mFragmentManager.beginTransaction().replace(R.id.fragment_container,mFragment).commit();
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
