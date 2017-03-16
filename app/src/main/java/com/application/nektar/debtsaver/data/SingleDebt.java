package com.application.nektar.debtsaver.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 03.03.2017.
 */

public class SingleDebt {
    private String mName;
    private double mValue;
    private List<SingleUser> mUserList;

    public SingleDebt(){
        mUserList  =new ArrayList<>();
    }

    public SingleDebt(String name, double value) {
        mName = name;
        mValue = value;
        mUserList  =new ArrayList<>();
    }

    public List<SingleUser> getUserList() {
        return mUserList;
    }

    public void setUserList(List<SingleUser> userList) {
        mUserList = userList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }
}
