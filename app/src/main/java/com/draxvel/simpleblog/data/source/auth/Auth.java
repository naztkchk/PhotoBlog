package com.draxvel.simpleblog.data.source.auth;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth implements IAuth {

    private FirebaseAuth firebaseAuth;

    public Auth() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isSignIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null;
    }

    @Override
    public void signIn(String email, String password, final SignInCallback callback) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    callback.onSignIn();
                } else {
                    String e = task.getException().getMessage();
                    callback.onFailure(e);
                }
            }
        });

    }

    @Override
    public void signUp(final String email, final String password, final SignUpCallback callback) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onSignUp();
                } else {
                    String e = task.getException().getMessage();
                    callback.onFailure(e);
                }
            }
        });

    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }
}
