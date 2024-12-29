package com.doan.pharcity.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.pharcity.Activity.MainActivity;
import com.doan.pharcity.Fragment.Product;
import com.doan.pharcity.R;
import com.doan.pharcity.SQLiteHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList =productList;
    }
    public void updateData(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //Set data to the views in the ViewHolder
        holder.nameProduct.setText(product.getName());
       String fullDescription = product.getDescription();
       String shortDescription;

       String[] words = fullDescription.split(" ");

       if(words.length > 3) {
          shortDescription = words[0] + " " + words[1] + " " + words[2] + " ...";
       } else {
           shortDescription = fullDescription;
       }
       holder.descriptionProduct.setText(shortDescription);
       String formattedPrice = String.format("%,d VNĐ", (int) product.getPrice());
       String quantityProduc = String.format("SL: %d", product.getQuantity());
       holder.quantityProduct.setText(quantityProduc);
       holder.priceProduct.setText(formattedPrice);
        //setimae
        holder.imageProduct.setImageResource(R.drawable.banner1);

        Integer quantity = 1;
        if(quantity < 1){
         Toast.makeText(holder.itemView.getContext(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        //Sự kiện thêm vào giỏ hàng
        holder.TextAddToCart.setOnClickListener(v -> {
            SQLiteHelper dbHelper = new SQLiteHelper(holder.itemView.getContext());
            String totalDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Lấy thông tin người dùng từ SharedPreferences
            SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("id", null);  // Lấy ID người dùng từ SharedPreferences

            if (userId != null) {
                // Người dùng đã đăng nhập, thêm vào giỏ hàng
                dbHelper.addOders(userId, product.getId(), product.getName(), totalDate, product.getPrice(), 1, quantity, product.getImageResId());
                Toast.makeText(holder.itemView.getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                // Người dùng chưa đăng nhập, không cho phép thêm vào giỏ hàng
                Toast.makeText(holder.itemView.getContext(), "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct, descriptionProduct, priceProduct, TextAddToCart, quantityProduct;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            nameProduct = itemView.findViewById(R.id.tvProductName);
            descriptionProduct = itemView.findViewById(R.id.tvProductDescription);
            priceProduct = itemView.findViewById(R.id.tvProductPrice);
            imageProduct = itemView.findViewById(R.id.imageViewProduct);
            TextAddToCart = itemView.findViewById(R.id.textAddToCart);
            quantityProduct = itemView.findViewById(R.id.quantityProduct);
        }
    }
}
