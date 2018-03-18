package com.draxvel.simpleblog.data;

public interface IAuth {

    interface SignInCallback {

        void onSignIn ();

        void onFailure(final String msg);
    }

    interface SignUpCallback {

        void onSignUp ();

        void onFailure(final String msg);
    }

    void signIn (String email, String password, SignInCallback callback);

    void signUp (final String email, final String password, SignUpCallback callback);
}
