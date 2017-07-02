package com.application.nektar.debtsaver;

import android.graphics.Bitmap;

import com.application.nektar.debtsaver.data.SingleDebt;
import com.application.nektar.debtsaver.data.SingleUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 24.02.2017.
 */
public class DebtContainer {
    private static DebtContainer mContainer;
    private String mName;
    private String mPhotoUrl;
    private Bitmap mPhotoBitmap;

    private List<SingleDebt> mDebtsList;
    private Map<SingleDebt,String> mKeyList;

    private List<SingleUser> mCheckedUsers;


    public static DebtContainer get() {
        if(mContainer == null){
            mContainer = new DebtContainer();
        }
        return mContainer;
    }

    public void clearCache(){
        mName = "";
        mPhotoUrl ="";
        mDebtsList.clear();
        mKeyList.clear();
        mCheckedUsers.clear();
    }

    public void resetCache(){
        mDebtsList.clear();
        mKeyList.clear();
        mCheckedUsers.clear();
    }

    private DebtContainer() {
        mPhotoUrl="";
        mName="";
        mDebtsList = new ArrayList<>();
        mKeyList = new HashMap<>();
        mCheckedUsers = new ArrayList<>();
    }

    public List<SingleUser> getCheckedUsers() {
        return mCheckedUsers;
    }

    public List<SingleDebt> getDebtsList() {
        return mDebtsList;
    }

    public Map<SingleDebt, String> getKeyList() {
        return mKeyList;
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
