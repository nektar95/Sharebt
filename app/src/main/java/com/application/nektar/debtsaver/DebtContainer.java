package com.application.nektar.debtsaver;

import android.graphics.Bitmap;

/**
 * Created by pc on 24.02.2017.
 */
public class DebtContainer {
    private static DebtContainer mContainer;
    private String mName;
    private String mPhotoUrl;
    private Bitmap mPhotoBitmap;


    public static DebtContainer get() {
        if(mContainer == null){
            mContainer = new DebtContainer();
        }
        return mContainer;
    }

    public void clearCache(){
        mName = "";
        mPhotoUrl ="";
    }

    private DebtContainer() {
        mPhotoUrl="";
        mName="";
    }

    public Bitmap getPhotoBitmap() {
        return mPhotoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        mPhotoBitmap = photoBitmap;
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
