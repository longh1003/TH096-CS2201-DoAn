package com.doan.pharcity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.pharcity.Fragment.AccountSettings;
import com.doan.pharcity.Fragment.Option;
import com.doan.pharcity.Fragment.Shoping_cartFragment;
import com.doan.pharcity.Login;
import com.doan.pharcity.R;

import java.util.List;

public class AccountOptionsAdapter extends RecyclerView.Adapter<AccountOptionsAdapter.ViewHolder> {
    private List<Option> optionList;
    private Context context;

    public AccountOptionsAdapter(Context context, List<Option> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_option_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Option option = optionList.get(position);
        holder.optionText.setText(option.getTitle());
        holder.optionIcon.setImageResource(option.getIconResId());

        // Thêm sự kiện khi nhấn vào mục
        holder.itemView.setOnClickListener(v -> {
            switch (option.getTitle()) {
                case "Cài đặt tài khoản":
                    // Chuyển đến màn hình cài đặt tài khoản
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new AccountSettings());
                    transaction.addToBackStack(null); // Để quay lại được màn hình trước đó
                    transaction.commit();
                    break;
                case "Giỏ hàng":
                    FragmentTransaction transaction1 = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragmentContainer, new Shoping_cartFragment());
                    transaction1.addToBackStack(null); // Để quay lại được màn hình trước đó
                    transaction1.commit();
                    // Chuyển đến màn hình giỏ hàng
                case "Đơn hàng đã đặt":
                    // Chuyển đến màn hình đơn hàng đã đặt
                    Toast.makeText(context, "Chuyển đến Đơn hàng đã đặt", Toast.LENGTH_SHORT).show();
                    break;
                case "Đăng xuất":
                    // Xử lý đăng xuất
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    Toast.makeText(context, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView optionIcon;
        TextView optionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            optionIcon = itemView.findViewById(R.id.optionIcon);
            optionText = itemView.findViewById(R.id.optionText);
        }
    }
}
