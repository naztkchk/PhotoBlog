package com.draxvel.simpleblog.ui.auth.signUp;

public interface ISignUpPresenter {
    void registration(final String email, final String password, final String confirm_password);
    void showSignIn();
}
