package com.wilderpereira.reciclo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wilderpereira.reciclo.R;
import com.wilderpereira.reciclo.activities.MainActivity;
import com.wilderpereira.reciclo.utils.Utils;


public class LoginFragment extends Fragment {

    String email;
    String pass;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etEmail = (EditText) v.findViewById(R.id.et_email);
        final EditText etPassword = (EditText) v.findViewById(R.id.et_pass);

        Button btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean areEditTextsEmpty = false;
                email = etEmail.getText().toString();
                pass = etPassword.getText().toString();

                if(email.isEmpty()){
                    Utils.setTextInputLayoutError((TextInputLayout) v.findViewById(R.id.tip_email),getString(R.string.email_empty));
                    areEditTextsEmpty = true;
                }

                if(pass.isEmpty()){
                    Utils.setTextInputLayoutError((TextInputLayout) v.findViewById(R.id.tip_pass),getString(R.string.password_empty));
                    areEditTextsEmpty = true;
                }

                //TODO: Store login shared preferences
                if(!areEditTextsEmpty){
                    getContext().startActivity(new Intent(getContext(), MainActivity.class));
                }
            }
        });

        return v;
    }

}
