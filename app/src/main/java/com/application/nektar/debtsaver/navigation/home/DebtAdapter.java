package com.application.nektar.debtsaver.navigation.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleDebt;

import java.util.List;

/**
 * Created by Aleksander on 08.06.2017.
 */

public class DebtAdapter extends RecyclerView.Adapter<DebtHolder> {
        private List<SingleDebt> mSingleDebts;
        private int lastPosition = -1;

        public DebtAdapter(List<SingleDebt> singleDebts) {
            mSingleDebts = singleDebts;
        }

        @Override
        public DebtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.single_debt_item,parent,false);
            return new DebtHolder(view);
        }

        @Override
        public void onBindViewHolder(DebtHolder holder, int position) {
            SingleDebt singleDebt = mSingleDebts.get(position);
            holder.bindResult(singleDebt);

            if (position > lastPosition) {
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(250);
                animation.setFillAfter(true);
                holder.mLinearLayout.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public void onViewDetachedFromWindow(DebtHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.clearAnimation();
        }

        @Override
        public int getItemCount() {
            return mSingleDebts.size();
        }
    }
