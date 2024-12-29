package com.doan.pharcity.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doan.pharcity.Customers.InforCustomers;
import com.doan.pharcity.Login;
import com.doan.pharcity.R;
import com.doan.pharcity.SQLiteHelper;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettings extends Fragment {
    private FirebaseAuth mAuth;
    private EditText valueEdUserName, valueEdUserEmail, valueEdUserPhone, valueEdUserBirdth, valueEdUserGender, valueEdUserAddress;
    private Button btnChangePassword, btnDeleteAccount, btnUpdateInfor;
    private boolean isDataChanged = false;
    // Khởi tạo SharedPreferences



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettings newInstance(String param1, String param2) {
        AccountSettings fragment = new AccountSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        valueEdUserName = view.findViewById(R.id.valueEdUserName);
        valueEdUserEmail = view.findViewById(R.id.valueEdUserEmail);
        valueEdUserPhone = view.findViewById(R.id.valueEdUserPhone);
        valueEdUserBirdth = view.findViewById(R.id.valueEdUserBirdth);
        RadioGroup genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        RadioButton maleRadioButton = view.findViewById(R.id.maleRadioButton);
        RadioButton femaleRadioButton = view.findViewById(R.id.femaleRadioButton);
        btnUpdateInfor = view.findViewById(R.id.bntUdateInforCus);
        valueEdUserAddress = view.findViewById(R.id.valueEdUserAddress);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);



        String emailValue = sharedPreferences.getString("email", "@gmail");

        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        InforCustomers inforCustomers = dbHelper.getInforCusByEmail(emailValue);



        //Chỉnh sửa thông tin khách hàng
        if (inforCustomers != null) {
            // Cài đặt RadioButton dựa trên giới tính


            // Gán các trường khác như trước
            valueEdUserName.setText(inforCustomers.getName() != null ? inforCustomers.getName() : "");
            valueEdUserName.setHint(inforCustomers.getName() == null ? "Chưa cập nhật" : "");

            valueEdUserEmail.setText(inforCustomers.getEmail() != null ? inforCustomers.getEmail() : "");
            valueEdUserEmail.setHint(inforCustomers.getEmail() == null ? "Chưa cập nhật" : "");

            valueEdUserPhone.setText(inforCustomers.getPhone() != null ? inforCustomers.getPhone() : "");
            valueEdUserPhone.setHint(inforCustomers.getPhone() == null ? "Chưa cập nhật" : "");

            valueEdUserBirdth.setText(inforCustomers.getDateOfBirth() != null ? inforCustomers.getDateOfBirth() : "");
            valueEdUserBirdth.setHint(inforCustomers.getDateOfBirth() == null ? "dd/MM/yyyy" : "");
            String gender = inforCustomers.getGender() != null ? inforCustomers.getGender() : "Chưa cập nhật";

            if (gender.equalsIgnoreCase("Nam")) {
                maleRadioButton.setChecked(true);
            } else if (gender.equalsIgnoreCase("Nữ")) {
                femaleRadioButton.setChecked(true);
            } else {
                genderRadioGroup.clearCheck(); // Không chọn gì nếu không có dữ liệu
            }

            valueEdUserAddress.setText(inforCustomers.getAddress() != null ? inforCustomers.getAddress() : "");
            valueEdUserAddress.setHint(inforCustomers.getAddress() == null ? "Chưa cập nhật" : "");
        } else {
            Log.e("AccountSettings", "Customer data is null");
            Toast.makeText(getContext(), "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
        }


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDataChanged = true;
                btnUpdateInfor.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        valueEdUserName.addTextChangedListener(textWatcher);
        valueEdUserEmail.addTextChangedListener(textWatcher);
        valueEdUserPhone.addTextChangedListener(textWatcher);
        valueEdUserBirdth.addTextChangedListener(textWatcher);
        valueEdUserAddress.addTextChangedListener(textWatcher);


        genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            isDataChanged = true;
            btnUpdateInfor.setEnabled(true);
        });


        btnUpdateInfor.setOnClickListener(v -> {
            boolean isDataChange;
            if(isDataChanged){
                // Lấy dữ liệu từ các trường
                String updatedName = valueEdUserName.getText().toString().trim();
                String updatedEmail = valueEdUserEmail.getText().toString().trim();
                String updatedPhone = valueEdUserPhone.getText().toString().trim();
                String updatedBirth = valueEdUserBirdth.getText().toString().trim();
                String updatedGender = maleRadioButton.isChecked() ? "Nam" : "Nữ";
                String updatedAddress = valueEdUserAddress.getText().toString().trim();


                // Thực hiện lưu dữ liệu
                dbHelper.saveUpdatedData(updatedName, updatedEmail, updatedPhone, updatedBirth, updatedGender, updatedAddress);
                // Lưu vào SharedPreferences
                SharedPreferences sharedPreference = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString("username", updatedName);
                editor.putString("email", updatedEmail);
                editor.putString("phone", updatedPhone);
                editor.putString("birth", updatedBirth);
                editor.putString("gender", updatedGender);
                editor.putString("address", updatedAddress);
                editor.apply();

                // Đặt lại trạng thái
                isDataChanged = false;
                btnUpdateInfor.setEnabled(false);

                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Không có thay đổi nào!", Toast.LENGTH_SHORT).show();
            }
        });



        // Change Password Button
        btnChangePassword.setOnClickListener(v -> {
            Fragment changePasswordFragment = new ChangePasswordFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, changePasswordFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnDeleteAccount.setOnClickListener(v -> {
            showPasswordConfirmationDialog();
        });

        valueEdUserBirdth.addTextChangedListener(new TextWatcher() {
            private boolean isEditing; // Để tránh vòng lặp TextWatcher
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return; // Tránh vòng lặp
                isEditing = true;

                String input = s.toString().replaceAll("[^\\d]", ""); // Xóa các ký tự không phải số
                StringBuilder formatted = new StringBuilder();

                // Tự động thêm dấu `/` sau ngày và tháng
                if (input.length() >= 2) {
                    formatted.append(input.substring(0, 2)).append("/");
                    if (input.length() >= 4) {
                        formatted.append(input.substring(2, 4)).append("/");
                        formatted.append(input.substring(4));
                    } else {
                        formatted.append(input.substring(2));
                    }
                } else {
                    formatted.append(input);
                }

                // Cập nhật lại giá trị đã định dạng
                valueEdUserBirdth.removeTextChangedListener(this);
                valueEdUserBirdth.setText(formatted.toString());
                valueEdUserBirdth.setSelection(formatted.length());
                valueEdUserBirdth.addTextChangedListener(this);

                isEditing = false;
            }
        });

        valueEdUserBirdth.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // Khi người dùng rời khỏi EditText
                String inputDate = valueEdUserBirdth.getText().toString().trim();

                if (!isValidDate(inputDate)) {
                    valueEdUserBirdth.setError("Ngày sinh không hợp lệ (dd/MM/yyyy)");
                } else {
                    valueEdUserBirdth.setError(null); // Xóa lỗi nếu hợp lệ
                }
            }
        });



        return view;

    }

    //Kiem tra date
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false); // Không cho phép ngày không hợp lệ như 31/02/2023
        try {
            sdf.parse(date); // Kiểm tra ngày
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void deleteAccountWithPassword(String password) {
        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (user != null) {
            String userEmail = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, password);

            // Xác thực lại
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Xóa tài khoản Firebase
                    user.delete().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            // Xóa tài khoản khỏi SQLite
                            boolean isDeleteCusData = dbHelper.deleteCustomer(userEmail);
                            if (isDeleteCusData) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                Toast.makeText(getContext(), "Tài khoản đã được xóa!", Toast.LENGTH_SHORT).show();

                                // Chuyển đến màn hình đăng nhập
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "Không thể xóa tài khoản SQLite", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không thể xóa tài khoản Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Xác thực thất bại. Mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Không có người dùng hiện tại", Toast.LENGTH_SHORT).show();
        }
    }


    private void showPasswordConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận mật khẩu");
        builder.setMessage("Vui lòng nhập mật khẩu của bạn để xác nhận xóa tài khoản:");

        // Thêm EditText vào dialog
        final EditText inputPassword = new EditText(getContext());
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputPassword);

        // Thiết lập các nút
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String password = inputPassword.getText().toString().trim();
            if (!password.isEmpty()) {
                deleteAccountWithPassword(password); // Gọi hàm xóa tài khoản với mật khẩu
            } else {
                Toast.makeText(getContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


}