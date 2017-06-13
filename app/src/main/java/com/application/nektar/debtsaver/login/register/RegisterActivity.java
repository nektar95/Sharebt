package com.application.nektar.debtsaver.login.register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.application.nektar.debtsaver.NavigationActivity;
import com.application.nektar.debtsaver.R;
import com.application.nektar.debtsaver.login.ResetPasswordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterMvpView{
    @BindView(R.id.edittext_email_entry) EditText inputEmail;
    @BindView(R.id.edittext_password_entry) EditText inputPassword;
    @BindView(R.id.sign_in_button) Button btnSignIn;
    @BindView(R.id.sign_up_button) Button btnSignUp;
    @BindView(R.id.button_reset_password_entry) Button btnResetPassword;
    @BindView(R.id.progressBar_entry) ProgressBar progressBar;

    private RegisterMvpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

        presenter = new RegisterPresenter(this);
        presenter.setupFirebase();
    }

    @OnClick(R.id.sign_up_button)
    public void signUpClick(){
        presenter.onSignUpBtnClick(inputEmail.getText().toString().trim(),inputPassword.getText().toString().trim());
    }

    @OnClick(R.id.sign_in_button)
    public void signInClick(){
        presenter.onSignInBtnClick();
    }

    @OnClick(R.id.button_reset_password_entry)
    public void resetBtnClick(){
        presenter.onResetBtnClick();
    }

    @Override
    public void showProgreesBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgreesBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startNavActivity() {
        startActivity(new Intent(RegisterActivity.this, NavigationActivity.class));
        finish();
    }

    @Override
    public void closeRegister() {
        finish();
    }

    @Override
    public void startActivityReset() {
        startActivity(new Intent(RegisterActivity.this, ResetPasswordActivity.class));
    }

    @Override
    public void setPresenter(@NonNull RegisterMvpPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
