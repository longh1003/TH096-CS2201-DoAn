package com.doan.pharcity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doan.pharcity.Adapter.AccountOptionsAdapter;
import com.doan.pharcity.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ImageView profileImage;
    private RecyclerView accountOptionsRecyclerView;



//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public AccountFragment() {
//        // Required empty public constructor
//    }
@Override
public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    Log.d("AccountFragment", "Fragment attached to Activity");
}




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);
        profileImage = view.findViewById(R.id.profileImage);
        accountOptionsRecyclerView = view.findViewById(R.id.accountOptionsRecyclerView);
        accountOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Option> optionList = new ArrayList<>();
        optionList.add(new Option("Cài đặt tài khoản", R.drawable.ic_settings));
        optionList.add(new Option("Giỏ hàng", R.drawable._025885_shopping_cart_icon));
        optionList.add(new Option("Đơn hàng đã đặt", R.drawable.ic_transport));
        optionList.add(new Option("Đăng xuất", R.drawable.ic_logout));

        AccountOptionsAdapter adapter = new AccountOptionsAdapter(requireContext(), optionList);
        accountOptionsRecyclerView.setAdapter(adapter);


        //set name user
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nameValue = sharedPreferences.getString("username", "Xin chào");
        String emailValue = sharedPreferences.getString("email", "@gmail");


        userName.setText(nameValue);
        userEmail.setText(emailValue);

       profileImage.setImageResource(R.drawable.account);



        return view;
    }
}