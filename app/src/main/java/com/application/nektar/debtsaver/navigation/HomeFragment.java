package com.application.nektar.debtsaver.navigation;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

/**
 * Created by pc on 23.02.2017.
 */

public class HomeFragment extends Fragment {
    private RecyclerView mDebtsRecyclerView;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }
}
