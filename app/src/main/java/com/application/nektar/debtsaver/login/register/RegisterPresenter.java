package com.application.nektar.debtsaver.login.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.application.nektar.debtsaver.DebtContainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Aleksander on 11.06.2017.
 */

public class RegisterPresenter implements RegisterMvpPresenter {
    private RegisterMvpView registerView;
    private FirebaseAuth auth;
    private String email;

    public RegisterPresenter(@NonNull RegisterMvpView view) {
        registerView = view;
    }

    @Override
    public void setupFirebase() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResetBtnClick() {
        registerView.startActivityReset();
    }

    @Override
    public void onSignInBtnClick() {
        registerView.closeRegister();
    }

    //easier approach by making fetch in presenter instead making interactor

    @Override
    public void onSignUpBtnClick(String name, String password) {
        email = name;

        if (TextUtils.isEmpty(email)) {
            registerView.makeToast("Enter email address!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            registerView.makeToast("Enter password!");
            return;
        }

        if (password.length() < 6) {
            registerView.makeToast("Password too short, enter minimum 6 characters!");
            return;
        }

        registerView.showProgreesBar();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                registerView.makeToast("createUserWithEmail:onComplete:" + task.isSuccessful());
                registerView.hideProgreesBar();

                if (!task.isSuccessful()) {
                    registerView.makeToast("Authentication failed." + task.getException());
                } else {
                    DebtContainer.get().setName(email);
                    registerView.startNavActivity();
                }
            }
        });
    }

    @Override
    public void detachView() {
        registerView = null;
    }
}
