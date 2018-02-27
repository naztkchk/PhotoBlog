package com.draxvel.simpleblog.login;

public interface ILoginView {
    void showSignIn();
    void showSignUp();
    void showForgotPassword();
    void showMainActivity();
    void showSetupActivity();

    void showError(final String e);
}
