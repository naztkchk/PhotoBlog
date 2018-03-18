package com.draxvel.simpleblog.data.source.auth;

public interface IAuth {

    interface SignInCallback {

        void onSignIn ();

        void onFailure(final String msg);
    }

    interface SignUpCallback {

        void onSignUp ();

        void onFailure(final String msg);
    }

    boolean isSignIn();

    void signIn (final String email, final String password, SignInCallback callback);

    void signUp (final String email, final String password, SignUpCallback callback);
}
