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
        ToastText = findViewById(R.id.toast_message);


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
                                sqLiteHelperl.addNewCustomer( userName, userEmail,null, null, null);
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

                showPasswordHint(s.toString());
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

        //Hiển thị lịch
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                //
//                month += 1;
//
//                @SuppressLint("DefaultLocale") String selectedDate =
//                        String.format(
//                                "%02d/%02d/%04d",
//                                dayOfMonth,
//                                month,
//                                year
//                        );
//
//                BirthEdit.setText(selectedDate);
//            }
//        }, 2024, 1, 1);
//
//        IconDateOfBirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePickerDialog.show();
//            }
//        });

    }

    private void showPasswordHint(String password) {
        // Kiểm tra các yếu tố mật khẩu và hiển thị gợi ý qua Toast

            showToast("Mật khẩu phải có ít nhất 8 ký tự\n"
                   +"Mật khẩu cần có ít nhất 1 chữ cái viết hoa\n"
                    +"Mật khẩu cần có ít nhất 1 ký tự đặc biệt");
    }


    private void showToast(String message) {
        // Tạo layout cho custom Toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout_root));

        // Tạo TextView trong custom Toast
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        // Tạo Toast với layout custom
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT); // Thời gian hiển thị Toast
        toast.setView(layout);

        // Đặt vị trí Toast ở đầu màn hình
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200); // Y=200 sẽ đẩy lên trên một chút
        toast.show();
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

//    //Check phone
//    @SuppressLint("SetTextI18n")
//    private void validatePhoneOne(String phone) {
//        String phoneRegex = "^(03|05|07|08|09)[0-9]{8}$";
//
//            if (phone.isEmpty()) {
//                PhoneHint.setVisibility(View.GONE); // Ẩn thông báo nếu chưa nhập gì
//            } else if (phone.matches(phoneRegex)) {
//                PhoneHint.setVisibility(View.GONE); // Hiện thông báo hợp lệ
//            } else {
//                PhoneHint.setText("Số điện thoại không hợp lệ");
//                PhoneHint.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                PhoneHint.setVisibility(View.VISIBLE); // Hiện thông báo không hợp lệ
//            }
//
//    }

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

//    @SuppressLint("SetTextI18n")
//    private void checkPasswordMatch() {
//        String password = PasswordEdit.getText().toString();
//        String confirmPassword = ConPasswordEdit.getText().toString();
//
//        if(confirmPassword.isEmpty()) {
//            PasswordMatchHint.setVisibility(View.GONE);
//        } else if (password.equals(confirmPassword)) {
//            PasswordMatchHint.setText("Mật khẩu trùng khớp.");
//            PasswordMatchHint.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//            PasswordMatchHint.setVisibility(View.VISIBLE);
//        } else {
//            PasswordMatchHint.setText("Mật khẩu không trùng khớp.");
//            PasswordMatchHint.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//            PasswordMatchHint.setVisibility(View.VISIBLE);
//        }
//    }

//    private void updatePasswordHints(String password) {
//        // Kiểm tra độ dài mật khẩu
//        if (password.length() >= 8) {
//            HintLength.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//        } else {
//            HintLength.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//        }
//
//        // Kiểm tra chữ cái viết hoa
//        if (password.matches(".*[A-Z].*")) {
//            HintUppercase.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//        } else {
//            HintUppercase.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//        }
//
//        // Kiểm tra ký tự đặc biệt
//        if (password.matches(".*[!@#$%^&*+=?-].*")) {
//            HintSpecial.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//        } else {
//            HintSpecial.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//        }
//    }

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