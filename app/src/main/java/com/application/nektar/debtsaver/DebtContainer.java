package com.application.nektar.debtsaver;

/**
 * Created by pc on 24.02.2017.
 */
public class DebtContainer {
    private static DebtContainer mContainer;
    private String mName;
    private String mPhotoUrl;


    public static DebtContainer get() {
        if(mContainer == null){
            mContainer = new DebtContainer();
        }
        return mContainer;
    }

    private DebtContainer() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
