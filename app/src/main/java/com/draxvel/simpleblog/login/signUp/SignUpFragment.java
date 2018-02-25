package com.draxvel.simpleblog.login.signUp;

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

import com.draxvel.simpleblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by draxvel on 25.02.2018.
 */

public class SignUpFragment extends Fragment{

    private View root;

    private ProgressBar sign_up_pb;
    private EditText signUp_email_et;
    private EditText signUp_password_et;
    private EditText signUp_confirm_password_et;
    private Button sign_up_btn;
    private TextView back_to_singIn;

    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        initView();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUp_email_et.getText().toString();
                String password = signUp_password_et.getText().toString();
                String confirm_password = signUp_confirm_password_et.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)){

                    if(TextUtils.equals(password, confirm_password)){

                        sign_up_pb.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "reg success", Toast.LENGTH_SHORT).show();
                                }else {
                                    String e = task.getException().getMessage();
                                    Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();
                                }

                                    sign_up_pb.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "passwords field doesnt match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }

    private void initView() {
        sign_up_pb = root.findViewById(R.id.signUp_pb);
        signUp_email_et = root.findViewById(R.id.signUp_email_et);
        signUp_password_et = root.findViewById(R.id.signUp_password_et);
        signUp_confirm_password_et = root.findViewById(R.id.signUp_confirm_password_et);

        sign_up_btn = root.findViewById(R.id.signUp_btn);

        back_to_singIn = root.findViewById(R.id.back_to_signIn);

        back_to_singIn.setText(Html.fromHtml(getResources().getString(R.string.already_have_an_account_tv)
                + "<font color=#FFFFFF>"
                +" "
                + getResources().getString(R.string.log_in) + "</font>"));
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){
//            sendToMain();
//        }
//    }
}
