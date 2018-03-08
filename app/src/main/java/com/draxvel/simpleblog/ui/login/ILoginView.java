package com.draxvel.simpleblog.ui.login;

import com.draxvel.simpleblog.ui.IView;

public interface ILoginView extends IView{
    void showSignIn();
    void showSignUp();
    void showForgotPassword();
    void showMainActivity();
    void showSetupActivity();
}
