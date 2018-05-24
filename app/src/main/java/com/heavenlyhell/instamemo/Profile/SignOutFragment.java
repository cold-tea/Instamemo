package com.heavenlyhell.instamemo.Profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.heavenlyhell.instamemo.Login.LoginActivity;
import com.heavenlyhell.instamemo.R;

public class SignOutFragment extends Fragment {


    private ProgressBar pbSignout;
    private TextView tvSignoutWait;
    private Button btnSignout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        pbSignout = view.findViewById(R.id.pbSignout);
        tvSignoutWait = view.findViewById(R.id.tvSignoutWait);
        btnSignout = view.findViewById(R.id.btnSignout);

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbSignout.setVisibility(View.VISIBLE);
                tvSignoutWait.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                        .FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLogin);
            }
        });
        return view;
    }

}
