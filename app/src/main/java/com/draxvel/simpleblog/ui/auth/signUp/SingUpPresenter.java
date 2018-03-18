package com.draxvel.simpleblog.ui.auth.signUp;

import android.app.Activity;
import android.text.TextUtils;

import com.draxvel.simpleblog.data.Auth;
import com.draxvel.simpleblog.data.IAuth;
import com.draxvel.simpleblog.ui.auth.ILoginView;

public class SingUpPresenter implements ISignUpPresenter{

    private ISignUpView iSignUpView;
    private ILoginView iLoginView;

    private Auth mAuth;

    public SingUpPresenter(final ISignUpView iSignUpView, Activity activity){
        this.iSignUpView = iSignUpView;
        this.iLoginView = (ILoginView) activity;

        mAuth = new Auth();
    }

    @Override
    public void registration(String email, String password, String confirm_password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)){

            if(TextUtils.equals(password, confirm_password)){

                iSignUpView.setVisibleProgressBar(true);

                mAuth.signUp(email, password, new IAuth.SignUpCallback() {
                    @Override
                    public void onSignUp() {
                        iSignUpView.setVisibleProgressBar(false);
                        iLoginView.showSetupActivity();
                    }

                    @Override
                    public void onFailure(String msg) {
                        iSignUpView.setVisibleProgressBar(false);
                        iLoginView.showError(msg);
                    }
                });

            } else {
                iSignUpView.passwordsDoesntMatch();
            }
        }
    }

    @Override
    public void showSignIn() {
        iLoginView.showSignIn();
    }
}
