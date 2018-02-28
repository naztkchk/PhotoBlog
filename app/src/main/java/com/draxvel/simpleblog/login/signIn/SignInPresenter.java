package com.draxvel.simpleblog.login.signIn;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.draxvel.simpleblog.login.ILoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInPresenter implements ISignInPresenter {

    private ISignInView iSignInView;
    private ILoginView iLoginView;

    private FirebaseAuth mAuth;

    public SignInPresenter(ISignInView iSignInView, Activity activity) {
        this.iSignInView = iSignInView;
        this.iLoginView = (ILoginView) activity;

        mAuth = FirebaseAuth.getInstance();
    }


    public void auth(final String email, final String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            iSignInView.setVisibleProgressBar(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        iLoginView.showMainActivity();
                    } else {
                        String e = task.getException().getMessage();
                        iLoginView.showError(e);
                    }

                    iSignInView.setVisibleProgressBar(false);
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
