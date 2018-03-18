package com.draxvel.simpleblog.ui.login.signIn;

import android.app.Activity;
import android.text.TextUtils;

import com.draxvel.simpleblog.data.Auth;
import com.draxvel.simpleblog.data.IAuth;
import com.draxvel.simpleblog.ui.login.ILoginView;

public class SignInPresenter implements ISignInPresenter {

    private ISignInView iSignInView;
    private ILoginView iLoginView;

    private Auth mAuth;

    public SignInPresenter(ISignInView iSignInView, Activity activity) {
        this.iSignInView = iSignInView;
        this.iLoginView = (ILoginView) activity;

        mAuth = new Auth();
    }

    public void auth(final String email, final String password) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            iSignInView.setVisibleProgressBar(true);

            mAuth.signIn(email, password, new IAuth.SignInCallback() {
                @Override
                public void onSignIn() {
                    iSignInView.setVisibleProgressBar(false);
                    iLoginView.showMainActivity();
                }

                @Override
                public void onFailure(String msg) {
                    iSignInView.setVisibleProgressBar(false);
                    iLoginView.showError(msg);
                }
            });
        }
    }

    @Override
    public void showSignUp() {
        iLoginView.showSignUp();
    }

    @Override
    public void showForgotPassword() {
        iLoginView.showForgotPassword();
    }
}
