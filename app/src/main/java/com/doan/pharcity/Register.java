package com.doan.pharcity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
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

    private FirebaseAuth auth;
    public Button SignUpBnt;
    private LinearLayout PasswordHintsLayout;
    private EditText PasswordEdit;
    private EditText UserEditRegisterEmail;
    private EditText ConPasswordEdit;
    private boolean passWordShow = false;
    private boolean isEmailValid = false;
    private boolean isPhoneValid = false;
    private TextView HintLength, HintUppercase, HintSpecial, PasswordMatchHint, EmailHint, PhoneHint;
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

        auth = FirebaseAuth.getInstance();
        SignUpBnt = findViewById(R.id.signUpBnt);
        final EditText UserEditRegisterFullName = findViewById(R.id.userEditRegisterFullName);
        final EditText UserEditRegisterPhone = findViewById(R.id.userEditRegisterPhone);
        UserEditRegisterEmail = findViewById(R.id.userEditRegisterEmail);
        PasswordEdit = findViewById(R.id.passwordEdit);
        ConPasswordEdit = findViewById(R.id.conPasswordEdit);
        final EditText BirthEdit = findViewById(R.id.birthEdit);
        final ImageView IconDateOfBirth = findViewById(R.id.iconDateOfBirth);
        final RadioButton RadNam = findViewById(R.id.radNam);
        final RadioButton RadNu = findViewById(R.id.radNu);
        final ImageView PasswordIcon = findViewById(R.id.passwordIcon);
        final ImageView ConPasswordIcon = findViewById(R.id.conPasswordIcon);
        HintLength = findViewById(R.id.hint_length);
        HintSpecial = findViewById(R.id.hint_special);
        HintUppercase = findViewById(R.id.hint_uppercase);
        PasswordHintsLayout = findViewById(R.id.passwordHintsLayout);
        PasswordMatchHint = findViewById(R.id.passwordMatchHint);
        EmailHint = findViewById(R.id.emailHint);
        PhoneHint = findViewById(R.id.phoneHint);


//        //Onclick BntSignUp
        SignUpBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = UserEditRegisterEmail.getText().toString().trim();
                String passWord = PasswordEdit.getText().toString().trim();


                if(userEmail.isEmpty()) {
                    UserEditRegisterEmail.setError("Không được để trống Email");
                }
                if(passWord.isEmpty()){
                    PasswordEdit.setError("Vui lòng điền mật  khẩu");
                } else {
                    auth.createUserWithEmailAndPassword(userEmail, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                            } else {
                                Toast.makeText(Register.this, "Đăng ký không thành công" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });






        PhoneHint.setOnFocusChangeListener((v , hashFocus) -> {
            if (hashFocus) {
                PhoneHint.setVisibility(View.VISIBLE);
            } else {
                PhoneHint.setVisibility(View.GONE);
            }
        });

        UserEditRegisterPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneOne(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        UserEditRegisterEmail.setOnFocusChangeListener((v , hashFocus) -> {
            if (hashFocus) {
                EmailHint.setVisibility(View.VISIBLE);
            } else {
                EmailHint.setVisibility(View.GONE);
            }
        });

        //Email input content
        UserEditRegisterEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmailOnce(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Check conform password
        ConPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordMatch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ConPasswordEdit.setOnFocusChangeListener((v, hashFoCus) -> {
            if(hashFoCus) {
                PasswordMatchHint.setVisibility(View.VISIBLE);
            } else {
                PasswordMatchHint.setVisibility(View.GONE);
            }
        });

        PasswordEdit.setOnFocusChangeListener((v,hasFocus) -> {
            if(hasFocus) {
                PasswordHintsLayout.setVisibility(View.VISIBLE);
            } else {
                PasswordHintsLayout.setVisibility(View.GONE);
            }
        });

        //Check passWord
        PasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordHints(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Show conPassWord
        ConPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(passWordShow){
                    passWordShow = false;

                    ConPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ConPasswordIcon.setImageResource(R.drawable.password_icon__eye_hide);
                } else {
                    passWordShow = true;

                    ConPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ConPasswordIcon.setImageResource(R.drawable.password_icon_password_security_show);
                }
                ConPasswordEdit.setSelection(ConPasswordEdit.length());
            }
        });

        //Show passWord
        PasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if password is showing or not
                if(passWordShow){
                    passWordShow = false;

                    PasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.password_icon__eye_hide);
                } else {
                    passWordShow = true;

                    PasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    PasswordIcon.setImageResource(R.drawable.password_icon_password_security_show);
                }
                //move the cursor at last of the text
                PasswordEdit.setSelection(PasswordEdit.length());
            }
        });


        //

//        //Back to Login
//        SignUpBnt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Register.this, Login.class));
//            }
//        });


        //Add TextWather
        BirthEdit.addTextChangedListener(new TextWatcher() {
            private String current = " ";
            private final Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(current)){
                    String clean = s.toString().replaceAll("[^\\d]", "");//Xóa các ký tự không phải là số
                    String cleanCurrent = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i+= 2){
                        sel++;
                    }


                    // Sửa lỗi khi người dùng xóa ký tự
                    if (clean.equals(cleanCurrent)) sel--;

                    if (clean.length() < 8) {
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        // Định dạng ngày
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        // Kiểm tra ngày tháng hợp lệ
                        if (month > 12) month = 12;
                        cal.set(Calendar.MONTH, month - 1);
                        year = (year < 1900) ? 1900 : (Math.min(year, cal.get(Calendar.YEAR)));
                        cal.set(Calendar.YEAR, year);

                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    BirthEdit.setText(current);
                    BirthEdit.setSelection(Math.min(sel, current.length()));
                }
            }
        });

        //Hiển thị lịch
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //
                month += 1;

                @SuppressLint("DefaultLocale") String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year);

                BirthEdit.setText(selectedDate);
            }
        }, 2024, 1, 1);

        IconDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    //Check phone
    @SuppressLint("SetTextI18n")
    private void validatePhoneOne(String phone) {
        String phoneRegex = "^(03|05|07|08|09)[0-9]{8}$";

            if (phone.isEmpty()) {
                PhoneHint.setVisibility(View.GONE); // Ẩn thông báo nếu chưa nhập gì
            } else if (phone.matches(phoneRegex)) {
                PhoneHint.setVisibility(View.GONE); // Hiện thông báo hợp lệ
            } else {
                PhoneHint.setText("Số điện thoại không hợp lệ");
                PhoneHint.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                PhoneHint.setVisibility(View.VISIBLE); // Hiện thông báo không hợp lệ
            }

    }

    //check Email
    @SuppressLint("SetTextI18n")
    private void validateEmailOnce(String email) {
        String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
            if (email.isEmpty()) {
                EmailHint.setVisibility(View.GONE);
            } else if (email.matches(gmailRegex)) {
                EmailHint.setText("Email hợp lệ.");
                EmailHint.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                EmailHint.setVisibility(View.VISIBLE);
            } else {
                EmailHint.setText("Email không đúng định dạng.");
                EmailHint.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                EmailHint.setVisibility(View.VISIBLE);
            }
    }

    @SuppressLint("SetTextI18n")
    private void checkPasswordMatch() {
        String password = PasswordEdit.getText().toString();
        String confirmPassword = ConPasswordEdit.getText().toString();

        if(confirmPassword.isEmpty()) {
            PasswordMatchHint.setVisibility(View.GONE);
        } else if (password.equals(confirmPassword)) {
            PasswordMatchHint.setText("Mật khẩu trùng khớp.");
            PasswordMatchHint.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            PasswordMatchHint.setVisibility(View.VISIBLE);
        } else {
            PasswordMatchHint.setText("Mật khẩu không trùng khớp.");
            PasswordMatchHint.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            PasswordMatchHint.setVisibility(View.VISIBLE);
        }
    }

    private void updatePasswordHints(String password) {
        // Kiểm tra độ dài mật khẩu
        if (password.length() >= 8) {
            HintLength.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            HintLength.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Kiểm tra chữ cái viết hoa
        if (password.matches(".*[A-Z].*")) {
            HintUppercase.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            HintUppercase.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Kiểm tra ký tự đặc biệt
        if (password.matches(".*[!@#$%^&*+=?-].*")) {
            HintSpecial.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            HintSpecial.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
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