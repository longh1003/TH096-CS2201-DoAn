package com.doan.pharcity.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doan.pharcity.Adapter.CardAdapter;
import com.doan.pharcity.Customers.InforCustomers;
import com.doan.pharcity.R;
import com.doan.pharcity.SQLiteHelper;

import java.util.List;


public class Shoping_cartFragment extends Fragment implements CardAdapter.OnQuantityChangeListener {
    private RecyclerView cardrecyclerView;
    private CardAdapter cardAdapter;
    private List<Orders> ordersList;
    private Button buttonAddShoping;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shoping_cart, container, false);

        SQLiteHelper dbHelper = new SQLiteHelper(getActivity());
        ordersList = dbHelper.getAllOrders();

        // Khởi tạo RecyclerView và Adapter
        cardrecyclerView = view.findViewById(R.id.cardRecycelview);
        cardrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardAdapter = new CardAdapter(getContext(), ordersList, this);
        cardrecyclerView.setAdapter(cardAdapter);
        buttonAddShoping = view.findViewById(R.id.buttonAddShoping);

        //Sự kiện đặt hàng
        buttonAddShoping.setOnClickListener(v -> {
           SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
           String userId = sharedPreferences.getString("id", null);

            if (userId != null) {
                SQLiteHelper dbHelpers = new SQLiteHelper(getActivity());
                boolean isOrderAdded = dbHelpers.updateOrderStatus(userId, 2);
                if (isOrderAdded) {
                    Toast.makeText(getActivity(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getActivity(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });


        //Tính tổng tiền sản phẩm
        int totalIteam = 0;
        double totalAmount = 0.0;
        for (Orders orders : ordersList) {
            totalIteam += orders.getQuantity();
            totalAmount += orders.getTotalPrice() * orders.getQuantity();
        }

        TextView totalItems = view.findViewById(R.id.totalItems);
        totalItems.setText("Tổng số sản phẩm: " + totalIteam);

        TextView totalPrice = view.findViewById(R.id.totalPrice);
        String formattedPrice = String.format("%,d VNĐ", (int) totalAmount);
        totalPrice.setText("Tổng tiền: " + formattedPrice);

        return view;
    }

    @Override
    public void onQuantityChanged(int totalItems, double totalAmount) {
        TextView totalItemsTextView = getView().findViewById(R.id.totalItems);
        totalItemsTextView.setText("Tổng số sản phẩm: " + totalItems);
        TextView totalPriceTextView = getView().findViewById(R.id.totalPrice);
        String formattedPrice = String.format("%,d VNĐ", (int) totalAmount);
        totalPriceTextView.setText("Tổng tiền: " + formattedPrice);

    }


}