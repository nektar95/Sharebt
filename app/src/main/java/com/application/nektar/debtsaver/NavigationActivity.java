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
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.nektar.debtsaver.login.LoginActivity;
import com.application.nektar.debtsaver.navigation.AddFragment;
import com.application.nektar.debtsaver.navigation.HomeFragment;
import com.application.nektar.debtsaver.navigation.StatsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by pc on 22.02.2017.
 */

public class NavigationActivity extends AppCompatActivity  {
    private Fragment mFragment;
    private AddFragment mAddFragment;
    private HomeFragment mHomeFragment;
    private StatsFragment mStatsFragment;
    private FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;
    private FirebaseAuth mFirebaseAuth;
    private FrameLayout mFrameLayout;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment_container);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = HomeFragment.newInstance();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment_container,mHomeFragment).commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                createFragments();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.action_home:{
                        if(mHomeFragment.isAdded()){
                            mFragmentTransaction.show(mHomeFragment);
                        }
                        else{
                            mFragmentTransaction.add(R.id.fragment_container,mHomeFragment);
                        }

                        if(mAddFragment.isAdded()){
                            mFragmentTransaction.hide(mAddFragment);
                        }
                        if(mStatsFragment.isAdded()){
                            mFragmentTransaction.hide(mStatsFragment);
                        }
                        break;
                    }
                    case R.id.action_add:{
                        if(mAddFragment.isAdded()){
                            mFragmentTransaction.show(mAddFragment);
                        }
                        else{
                            mFragmentTransaction.add(R.id.fragment_container,mAddFragment);
                        }

                        if(mHomeFragment.isAdded()){
                            mFragmentTransaction.hide(mHomeFragment);
                        }
                        if(mStatsFragment.isAdded()){
                            mFragmentTransaction.hide(mStatsFragment);
                        }

                        break;
                    }
                    case R.id.action_stats:{
                        if(mStatsFragment.isAdded()){
                            mFragmentTransaction.show(mStatsFragment);
                        }
                        else{
                            mFragmentTransaction.add(R.id.fragment_container,mStatsFragment);
                        }

                        if(mAddFragment.isAdded()){
                            mFragmentTransaction.hide(mAddFragment);
                        }
                        if(mHomeFragment.isAdded()){
                            mFragmentTransaction.hide(mHomeFragment);
                        }
                        break;
                    }
                }
                mFragmentTransaction.commit();
                return true;
            }
        });


    }

    private void createFragments(){
        if(mStatsFragment==null){
            mStatsFragment =  StatsFragment.newInstance();
        }
        if(mAddFragment==null){
            mAddFragment =  AddFragment.newInstance();
        }
        if(mHomeFragment==null){
            mHomeFragment =  HomeFragment.newInstance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
