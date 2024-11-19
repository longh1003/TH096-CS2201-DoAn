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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Register extends AppCompatActivity {

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

        final EditText UserEditRegisterFullName = findViewById(R.id.userEditRegisterFullName);
        final EditText UserEditRegisterPhone = findViewById(R.id.userEditRegisterPhone);
        UserEditRegisterEmail = findViewById(R.id.userEditRegisterEmail);
        PasswordEdit = findViewById(R.id.passwordEdit);
        ConPasswordEdit = findViewById(R.id.conPasswordEdit);
        final EditText BirthEdit = findViewById(R.id.birthEdit);
        final TextView SignUpBnt = findViewById(R.id.signIpBnt);
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

        //Back to Login
        SignUpBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        //Add TextWather
        BirthEdit.addTextChangedListener(new TextWatcher() {
            private String current = " ";
            private final String ddmmyyyy = "DDMMYYYY";
            private final Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(current)){
                    //Xóa các ký tự không phải là số
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanCurrent = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i+= 2){
                        sel++;
                    }


                    // Sửa lỗi khi người dùng xóa ký tự
                    if (clean.equals(cleanCurrent)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        // Định dạng ngày
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        // Kiểm tra ngày tháng hợp lệ
                        if (month > 12) month = 12;
                        cal.set(Calendar.MONTH, month - 1);
                        year = (year < 1900) ? 1900 : (
                                year > cal.get(Calendar.YEAR) ?
                                cal.get(Calendar.YEAR) :
                                year
                        );
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ?
                                cal.getActualMaximum(Calendar.DATE) :
                                day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    BirthEdit.setText(current);
                    BirthEdit.setSelection(
                            sel < current.length() ?
                            sel :
                            current.length()
                    );
                }
            }
        });

        //Hiển thị lịch
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //
                month += 1;

                @SuppressLint("DefaultLocale") String selectedDate =
                        String.format(
                                "%02d/%02d/%04d",
                                dayOfMonth,
                                month,
                                year
                        );

                BirthEdit.setText(selectedDate);
            }
        }, 2024,01,01);

        IconDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    //Check phone
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

}