package com.doan.pharcity;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClientl;
    private SignInClient oneTapClient;
    private Button SignBnt;
    private EditText UserLoginPhone;
    private FirebaseAuth auth;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 100;
    TextView ForgotPassword;
    private long backPressedTime;
    private Toast backToast;



    private boolean passwordShowing = false;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
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
        final EditText PasswordEd = findViewById(R.id.passwordEdit);
        final TextView SignnUpBnt = findViewById(R.id.signnUpBnt);
        SignBnt = findViewById(R.id.signBnt);
        ForgotPassword = findViewById(R.id.forgotPassword);
        UserLoginPhone = findViewById(R.id.userLoginPhone);
        sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        auth  = FirebaseAuth.getInstance();

        final boolean[] isPassworVisible = {false};

        //Setup Onclik eyeshowpas
        PasswordEd.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (PasswordEd.getRight() - PasswordEd.getCompoundDrawables()[2].getBounds().width())) {
                    if (!PasswordEd.getText().toString().isEmpty()) {
                        if (isPassworVisible[0]) {
//                            PasswordEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            PasswordEd.setTransformationMethod(new PasswordTransformationMethod());
                            PasswordEd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.hidepass, 0);
                        } else {
//                            PasswordEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            PasswordEd.setTransformationMethod(null);
                            PasswordEd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.showpass, 0);
                        }
                        PasswordEd.setSelection(PasswordEd.getText().length());
                        isPassworVisible[0] = !isPassworVisible[0];
                    } else {
                        Toast.makeText(Login.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            }
            return false;
        });
        


        //Onlcik Loginbnt
        SignBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneOrEmail = UserLoginPhone.getText().toString();
                String pass = PasswordEd.getText().toString();


                if (Patterns.PHONE.matcher(phoneOrEmail).matches()) {  // Nếu người dùng nhập số điện thoại
                    if (sqLiteHelper.isPhoneExist(phoneOrEmail)) {  // Kiểm tra số điện thoại có tồn tại không
                        String email = sqLiteHelper.getEmailByPhone(phoneOrEmail);  // Lấy email tương ứng với số điện thoại

                        // Tiến hành đăng nhập với email và mật khẩu
                        if (!pass.isEmpty()) {
                            auth.signInWithEmailAndPassword(email, pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this, MainActivity.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Login.this, "Sai mật khẩu hoặc SĐT!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            PasswordEd.setError("Vui lòng nhập mật khẩu!");
                        }
                    } else {
                        UserLoginPhone.setError("Số điện thoại chưa được đăng ký!");
                    }
                } else if (Patterns.EMAIL_ADDRESS.matcher(phoneOrEmail).matches()) {  // Nếu người dùng nhập email
                    // Kiểm tra email có tồn tại trong hệ thống hay không (tìm theo email)
                    if (sqLiteHelper.isEmailExist(phoneOrEmail)) {
                        // Tiến hành đăng nhập với email và mật khẩu
                        if (!pass.isEmpty()) {
                            auth.signInWithEmailAndPassword(phoneOrEmail, pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this, MainActivity.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Login.this, "Sai mật khẩu hoặc email!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            PasswordEd.setError("Vui lòng nhập mật khẩu!");
                        }
                    } else {
                        UserLoginPhone.setError("Email chưa được đăng ký!");
                    }
                } else {
                    UserLoginPhone.setError("Vui lòng nhập đúng số điện thoại hoặc email!");
                }
            }
        });

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("894445638172-vjp78se20gm91b6skobo0u9fkpppqfc4.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();
        // ...






        //Dialog forgotPassword
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogview = getLayoutInflater().inflate(R.layout.dialog_forgot, null);

                EditText emailBox = dialogview.findViewById(R.id.emailBox);

                builder.setView(dialogview);
                AlertDialog dialog = builder.create();

                dialogview.findViewById(R.id.bntReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail = emailBox.getText().toString();

                        if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this,"Enter", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Đã gỡi mã OTP", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this, "Sai địa chỉ Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogview.findViewById(R.id.bntCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });




        SignnUpBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    @Override
    public void onBackPressed(){

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            if(backToast != null){
                backToast.cancel();
            }
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }



    public void butonSignnUpBnt(View view){
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

}