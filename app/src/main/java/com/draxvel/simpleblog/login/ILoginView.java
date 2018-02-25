package com.draxvel.simpleblog.login;

public interface ILoginView {
    void showSignIn();
    void showSignUp();
    void showForgotPassword();
    void showMainActivity();

    void showError(final String e);
}
