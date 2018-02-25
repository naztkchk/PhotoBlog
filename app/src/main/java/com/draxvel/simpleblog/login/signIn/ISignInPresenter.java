package com.draxvel.simpleblog.login.signIn;

public interface ISignInPresenter {
    void auth(final String email, final String password);

    void showSignUp();
    void showForgotPassword();
}
