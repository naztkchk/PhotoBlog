package com.draxvel.simpleblog.login.signUp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.draxvel.simpleblog.login.ILoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingUpPresenter implements ISignUpPresenter{

    private ISignUpView iSignUpView;
    private ILoginView iLoginView;

    private FirebaseAuth mAuth;

    public SingUpPresenter(final ISignUpView iSignUpView, Activity activity){
        this.iSignUpView = iSignUpView;
        this.iLoginView = (ILoginView) activity;

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void registration(String email, String password, String confirm_password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)){

            if(TextUtils.equals(password, confirm_password)){

                iSignUpView.isVisibleProgressBar(true);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            iLoginView.showSetupActivity();
                        }else {
                            String e = task.getException().getMessage();
                            iLoginView.showError(e);
                        }
                        iSignUpView.isVisibleProgressBar(false);
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
