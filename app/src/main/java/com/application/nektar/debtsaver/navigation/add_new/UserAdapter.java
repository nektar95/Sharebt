package com.application.nektar.debtsaver.navigation.add_new;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.data.SingleUser;

import java.util.List;

/**
 * Created by Aleksander on 08.06.2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    private List<SingleUser> mSingleUsers;
    private int lastPosition = -1;

    public UserAdapter(List<SingleUser> singleDebts) {
        mSingleUsers = singleDebts;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.users_single_item,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        SingleUser singleDebt = mSingleUsers.get(position);
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
    public void onViewDetachedFromWindow(UserHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mSingleUsers.size();
    }
}
