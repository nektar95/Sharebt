package com.application.nektar.debtsaver;

/**
 * Created by pc on 24.02.2017.
 */
public class DebtContainer {
    private static DebtContainer mContainer;


    public static DebtContainer get() {
        if(mContainer == null){
            mContainer = new DebtContainer();
        }
        return mContainer;
    }

    private DebtContainer() {
    }
}
