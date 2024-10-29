package com.doan.pharcity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private boolean passwordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final EditText UserEd = findViewById(R.id.userEd);
        final EditText PasswordEd = findViewById(R.id.passwordEdit);
        final ImageView PasswordIcon = findViewById(R.id.passwordIcon);
        final TextView ForgotPassword = findViewById(R.id.forgotPassword);
        final TextView SignnUpBnt = findViewById(R.id.signnUpBnt);


        PasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checking if password is showing or not
                if(passwordShowing){
                    passwordShowing = false;

                    PasswordEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.password_icon__eye_hide);
                } else {
                    passwordShowing = true;

                    PasswordEd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.password_icon_password_security_show);
                }
                //move the cursor at last of the text
                PasswordEd.setSelection(PasswordEd.length());
            }
        });


        SignnUpBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}