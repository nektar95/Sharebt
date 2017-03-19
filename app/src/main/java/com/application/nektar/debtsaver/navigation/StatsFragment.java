package com.application.nektar.debtsaver.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pc on 23.02.2017.
 */

public class StatsFragment extends Fragment {
    private List<SingleDebt> mDebtsList;
    private Map<SingleDebt,String> mKeyList;
    private TextView mAllDebtTextView;
    private TextView mMinusDebtTextView;
    private TextView mPlusDebtTextView;
    private float mPlusDebts;
    private float mMinusDebts;
    private PieChart mPieChart;

    public static StatsFragment newInstance(){
        return new StatsFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        mAllDebtTextView = (TextView) view.findViewById(R.id.stats_fragment_all_debts);
        mMinusDebtTextView = (TextView) view.findViewById(R.id.stats_fragment_minus_debts);
        mPlusDebtTextView = (TextView) view.findViewById(R.id.stats_fragment_plus_debts);
        mPieChart = (PieChart) view.findViewById(R.id.stats_fragment_pie_chart);

        mDebtsList = new ArrayList<>();
        mKeyList = new HashMap<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child(id).child("debtsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlusDebts = 0f;
                mMinusDebts = 0f;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    SingleDebt debt = snapshot.getValue(SingleDebt.class);
                    if(debt.getValue()>0){
                        mPlusDebts += debt.getValue();
                    } else {
                        mMinusDebts += debt.getValue();
                    }

                }
                updateDebts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void updateDebts(){
        mPlusDebtTextView.setText(String.format(Locale.getDefault(),"%.2f",mPlusDebts));
        mMinusDebtTextView.setText(String.format(Locale.getDefault(),"%.2f",mMinusDebts));
        mAllDebtTextView.setText(String.format(Locale.getDefault(),"%.2f",mMinusDebts+mPlusDebts));
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mPlusDebts,"Debts"));
        entries.add(new PieEntry(mMinusDebts*(-1),"Obligations"));

        final PieDataSet dataSet = new PieDataSet(entries,"");

        dataSet.setColors(ContextCompat.getColor(getActivity(),R.color.green),ContextCompat.getColor(getActivity(),R.color.red));
        dataSet.setValueTextSize(18f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setHighlightEnabled(true);

        PieData data = new PieData(dataSet);

        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });


        mPieChart.setData(data);
        mPieChart.getDescription().setText("");
        mPieChart.invalidate();

    }
}
