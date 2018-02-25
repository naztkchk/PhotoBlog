package com.draxvel.simpleblog.login.signIn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.draxvel.simpleblog.R;

public class SignInFragment extends Fragment implements ISignInView{

    private View root;
    private SignInPresenter signInPresenter;

    private ProgressBar login_pb;
    private EditText email_et;
    private EditText password_et;
    private Button log_in_btn;
    private TextView signUp_tv;
    private TextView recover_password_tv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_signin, container, false);

        initView();
        initPresenter();
        intiListener();

        return root;
    }

    private void initView() {
        login_pb = root.findViewById(R.id.login_pb);
        email_et = root.findViewById(R.id.email_et);
        password_et = root.findViewById(R.id.password_et);

        log_in_btn = root.findViewById(R.id.log_in_btn);

        recover_password_tv = root.findViewById(R.id.recover_password_tv);
        signUp_tv = root.findViewById(R.id.signUp_tv);

        recover_password_tv.setText(Html.fromHtml(getResources().getString(R.string.forgot_password_tv)
                + "<font color=#FFFFFF>"
                +" "
                + getResources().getString(R.string.get_help) + "</font>"));

        signUp_tv.setText(Html.fromHtml(getResources().getString(R.string.already_have_an_account_tv)
                + "<font color=#FFFFFF>"
                +" "
                + getResources().getString(R.string.log_in) + "</font>"));

    }

    private void initPresenter() {
        signInPresenter = new SignInPresenter(this, getActivity());
    }

    private void intiListener() {

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_et.getText().toString();
                String password = password_et.getText().toString();

                signInPresenter.auth(email, password);
            }});

        signUp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInPresenter.showSignUp();
            }});

    }

    @Override
    public void isVisibleProgressBar(boolean s) {
        if(s){
            login_pb.setVisibility(View.VISIBLE);
        }else{
            login_pb.setVisibility(View.INVISIBLE);
        }
    }


//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser !=null){
//            showMainAct();
//        }
//    }

}
