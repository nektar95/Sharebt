package com.application.nektar.debtsaver.data;

/**
 * Created by pc on 03.03.2017.
 */

public class SingleDebt {
    private String mName;
    private double mValue;

    public SingleDebt(){

    }

    public SingleDebt(String name, double value) {
        mName = name;
        mValue = value;
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
