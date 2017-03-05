package com.application.nektar.debtsaver.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;
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
    private Double mPlusDebts;
    private Double mMinusDebts;

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

        mDebtsList = new ArrayList<>();
        mKeyList = new HashMap<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child(id).child("debtsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlusDebts = 0d;
                mMinusDebts = 0d;
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
    }
}
