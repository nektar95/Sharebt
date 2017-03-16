package com.application.nektar.debtsaver.data;

/**
 * Created by pc on 15.03.2017.
 */

public class SingleUser {
    private String mName;

    public SingleUser(){

    }

    public SingleUser(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
