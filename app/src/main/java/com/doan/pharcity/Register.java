package com.doan.pharcity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Calendar;
import java.util.Objects;

public class Register extends AppCompatActivity {


    private SQLiteHelper sqLiteHelperl;
    private FirebaseAuth auth;
    public Button SignUpBnt;
    private LinearLayout PasswordHintsLayout;
    private EditText PasswordEdit;
    private EditText UserEditRegisterEmail;
    private EditText ConPasswordEdit;
    private boolean passWordShow = false;
    private boolean isEmailValid = false;
    private boolean isPhoneValid = false;
    private TextView HintLength, HintUppercase, HintSpecial, PasswordMatchHint,
            EmailHint, PhoneHint, SingInTxt, ToastText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sqLiteHelperl = new SQLiteHelper(this);

        auth = FirebaseAuth.getInstance();
        SignUpBnt = findViewById(R.id.signUpBnt);
        final EditText UserEditRegisterFullName = findViewById(R.id.userEditRegisterFullName);
        UserEditRegisterEmail = findViewById(R.id.userEditRegisterEmail);
        PasswordEdit = findViewById(R.id.passwordEdit);
        SingInTxt = findViewById(R.id.signIpText);
        final boolean[] isPassworEditVisible = {false};
//   ToastText = findViewById(R.id.toast_message);


        //Setup Onclik eyeshowpas
        PasswordEdit.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (PasswordEdit.getRight() - PasswordEdit.getCompoundDrawables()[2].getBounds().width())) {
                    if (!PasswordEdit.getText().toString().isEmpty()) {
                        if (isPassworEditVisible[0]) {
//                            PasswordEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            PasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
                            PasswordEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.hidepass, 0);
                        } else {
//                            PasswordEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            PasswordEdit.setTransformationMethod(null);
                            PasswordEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.showpass, 0);
                        }
                        PasswordEdit.setSelection(PasswordEdit.getText().length());
                        isPassworEditVisible[0] = !isPassworEditVisible[0];
                    } else {
                        Toast.makeText(Register.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            }
            return false;
        });


//        //Onclick BntSignUp
        SignUpBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = UserEditRegisterFullName.getText().toString().trim();
                String userEmail = UserEditRegisterEmail.getText().toString().trim();
                String passWord = PasswordEdit.getText().toString().trim();


                if(userName.isEmpty()) {
                    UserEditRegisterFullName.setError("Không được để trống tên");
                }
                if(userEmail.isEmpty()) {
                    UserEditRegisterEmail.setError("Không được để trống Email");
                    UserEditRegisterEmail.requestFocus();
                    return;
                }
                if(passWord.isEmpty()){
                    PasswordEdit.setError("Vui lòng điền mật  khẩu");
                    PasswordEdit.requestFocus();
                    return;
                }
                if (sqLiteHelperl.isEmailExist(userEmail)) {
                    Toast.makeText(Register.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
               else {
                    auth.createUserWithEmailAndPassword(userEmail, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                sqLiteHelperl.addNewCustomer( userName, userEmail,null, null, null, null);
                                Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(Register.this, Login.class));
                                        finish();
                                    }
                                }, 3000);
                            } else {
                                String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                String localizedMessage = translateFirebaseErrorToVietnamese(errorMessage);
                                Toast.makeText(Register.this, "Đăng ký không thành công: " + localizedMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        PasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                showPasswordHint(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        UserEditRegisterEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                validateEmailOnce(UserEditRegisterEmail.getText().toString().trim());
            }
        });


        SingInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });



    }

    private String translateFirebaseErrorToVietnamese(String errorMessage) {
        if (errorMessage.contains("email address is already in use")) {
            return "Email đã được sử dụng.";
        } else if (errorMessage.contains("invalid email")) {
            return "Email không hợp lệ.";
        } else if (errorMessage.contains("weak password")) {
            return "Mật khẩu quá yếu. Vui lòng chọn mật khẩu mạnh hơn.";
        } else if (errorMessage.contains("network error")) {
            return "Lỗi mạng. Vui lòng kiểm tra kết nối Internet.";
        } else if (errorMessage.contains("The email address is badly formatted")) {
            return "Định dạng email không hợp lệ.";
        } else if (errorMessage.contains("Password should be at least 6 characters")) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }
        // Thêm các lỗi khác nếu cần
        return "Lỗi không xác định. Vui lòng thử lại.";
    }


//    //check Email
    private void validateEmailOnce(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (email.isEmpty()) {
               UserEditRegisterEmail.setError(null);
            } else if (!email.matches(emailRegex)) {
                UserEditRegisterEmail.setError("Email không hợp lệ");
            } else {
                UserEditRegisterEmail.setError(null);
            }
    }


    public boolean isEmailValid() {
        return isEmailValid;
    }

    public void setEmailValid(boolean emailValid) {
        isEmailValid = emailValid;
    }

    public boolean isPhoneValid() {
        return isPhoneValid;
    }

    public void setPhoneValid(boolean phoneValid) {
        isPhoneValid = phoneValid;
    }
}