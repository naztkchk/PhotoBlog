package com.draxvel.simpleblog.login;

import com.draxvel.simpleblog.IView;

public interface ILoginView extends IView{
    void showSignIn();
    void showSignUp();
    void showForgotPassword();
    void showMainActivity();
    void showSetupActivity();
}
