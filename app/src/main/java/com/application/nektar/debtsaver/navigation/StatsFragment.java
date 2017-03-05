package com.application.nektar.debtsaver.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.nektar.debtsaver.R;

/**
 * Created by pc on 23.02.2017.
 */

public class StatsFragment extends Fragment {
    public static StatsFragment newInstance(){
        return new StatsFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        return view;
    }
}
