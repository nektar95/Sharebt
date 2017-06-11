package com.application.nektar.debtsaver.login.register;

import com.application.nektar.debtsaver.login.base.BasePresenter;

/**
 * Created by Aleksander on 11.06.2017.
 */

public interface RegisterMvpPresenter extends BasePresenter{
    void onResetBtnClick();
    void onSignInBtnClick();
    void onSignUpBtnClick();
    void setupFirebase();
}
