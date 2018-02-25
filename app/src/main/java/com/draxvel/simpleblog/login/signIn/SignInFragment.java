package com.draxvel.simpleblog.login.signIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.draxvel.simpleblog.MainActivity;
import com.draxvel.simpleblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    private View root;

    private ProgressBar login_pb;
    private EditText email_et;
    private EditText password_et;
    private Button log_in_btn;
    private TextView signUp_tv;
    private TextView recover_password_tv;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_signin, container, false);

        mAuth = FirebaseAuth.getInstance();
        initView();

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_et.getText().toString();
                String password = password_et.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    login_pb.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                showMainAct();
                            }else {
                                String e = task.getException().getMessage();
                                Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();
                            }

                            login_pb.setVisibility(View.INVISIBLE);

                        }
                    });
                }

            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser !=null){
            showMainAct();
        }
    }

    private void showMainAct(){
        startActivity(new Intent(getActivity(),MainActivity.class));
    }
}
