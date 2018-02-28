package com.draxvel.simpleblog.login.signUp;

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
import android.widget.Toast;

import com.draxvel.simpleblog.R;

public class SignUpFragment extends Fragment implements ISignUpView{

    private View root;
    private SingUpPresenter singUpPresenter;

    private ProgressBar sign_up_pb;
    private EditText signUp_email_et;
    private EditText signUp_password_et;
    private EditText signUp_confirm_password_et;
    private Button sign_up_btn;
    private TextView back_to_singIn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_signup, container, false);

        initView();
        initPresenter();
        initListener();

        return root;
    }

    private void initView() {
        sign_up_pb = root.findViewById(R.id.signUp_pb);
        signUp_email_et = root.findViewById(R.id.signUp_email_et);
        signUp_password_et = root.findViewById(R.id.signUp_password_et);
        signUp_confirm_password_et = root.findViewById(R.id.signUp_confirm_password_et);

        sign_up_btn = root.findViewById(R.id.signUp_btn);

        back_to_singIn = root.findViewById(R.id.back_to_signIn);

        back_to_singIn.setText(Html.fromHtml(getResources().getString(R.string.already_have_an_account)
                + "<font color=#FFFFFF>"
                +" "
                + getResources().getString(R.string.log_in) + "</font>"));
    }

    private void initPresenter() {
        singUpPresenter = new SingUpPresenter(this, getActivity());
    }

    private void initListener() {
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUp_email_et.getText().toString();
                String password = signUp_password_et.getText().toString();
                String confirm_password = signUp_confirm_password_et.getText().toString();

                singUpPresenter.registration(email, password, confirm_password);
            }
        });

        back_to_singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUpPresenter.showSignIn();
            }
        });
    }

    @Override
    public void showError(String e) {
        //
    }

    @Override
    public void setVisibleProgressBar(boolean s) {
        if(s){
            sign_up_pb.setVisibility(View.VISIBLE);
        }else {
            sign_up_pb.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void passwordsDoesntMatch() {
        Toast.makeText(getActivity(), "passwords field doesn't match", Toast.LENGTH_SHORT).show();
    }
}
