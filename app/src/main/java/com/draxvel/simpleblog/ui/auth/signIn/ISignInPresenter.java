package com.draxvel.simpleblog.ui.auth.signIn;

public interface ISignInPresenter {
    void auth(final String email, final String password);

    void showSignUp();
    void showForgotPassword();
}
