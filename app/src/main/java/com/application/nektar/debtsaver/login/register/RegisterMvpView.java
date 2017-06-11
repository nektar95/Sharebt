package com.application.nektar.debtsaver.login.register;

import com.application.nektar.debtsaver.login.base.BaseView;

/**
 * Created by Aleksander on 11.06.2017.
 */

public interface RegisterMvpView extends BaseView<RegisterMvpPresenter> {
    void closeRegister();
    void startActivityReset();
    void showProgreesBar();
    void hideProgreesBar();
    void makeToast(String s);
    void startNavActivity();
    String getEmail();
    String getPassword();
}
