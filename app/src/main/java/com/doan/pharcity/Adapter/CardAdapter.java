package com.doan.pharcity.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.pharcity.Fragment.Orders;
import com.doan.pharcity.Fragment.Product;
import com.doan.pharcity.R;
import com.doan.pharcity.SQLiteHelper;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Orders> ordersList;
    private Context context;
    private OnQuantityChangeListener listener;

    public CardAdapter(Context context, List<Orders> ordersList, OnQuantityChangeListener listener) {
        this.context = context;
        this.ordersList = ordersList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
       return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
        Orders orders = ordersList.get(position);


        //Hiển thị sản phẩm
        holder.textNameCard.setText(orders.getNameProduct());

        String formattedPrice = String.format("%,d VNĐ", (int) orders.getTotalPrice());

        holder.textPriceCard.setText(formattedPrice);

        holder.textNumberItemCard.setText(String.valueOf(orders.getQuantity()));

        holder.minusbuton.setOnClickListener(v -> {
            int currentQuantity = orders.getQuantity();
            SQLiteHelper db = new SQLiteHelper(holder.itemView.getContext());

            if(currentQuantity > 1){
                //Giảm số lượng nếu > 1
                currentQuantity--;
                orders.setQuantity(currentQuantity);

                double newTotalPrice = orders.getTotalPrice() * currentQuantity;

                //cập nhật csdl
                ContentValues values = new ContentValues();
                values.put(SQLiteHelper.QUANTITY_COL, currentQuantity);
                int productID = Integer.parseInt(orders.getProductId());
                db.updateOrder(productID, currentQuantity);

                holder.textNumberItemCard.setText(String.valueOf(currentQuantity));
                notifyQuantityChange();
            } else {
                int productID = Integer.parseInt(orders.getProductId());
                db.deleteProductFromOrder(productID);

                ordersList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, ordersList.size());
                notifyQuantityChange();
            }
        });

        holder.plusbuton.setOnClickListener(v ->{
            int currentQuantity = orders.getQuantity();
            SQLiteHelper db = new SQLiteHelper(holder.itemView.getContext());
            currentQuantity++;
            orders.setQuantity(currentQuantity);

            //Cap nhat csdl
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.QUANTITY_COL, currentQuantity);
            int productID = Integer.parseInt(orders.getProductId());
            db.updateOrder(productID, currentQuantity);

            holder.textNumberItemCard.setText(String.valueOf(currentQuantity));
            notifyQuantityChange();
        });

        holder.deletecard.setOnClickListener(v -> {
            SQLiteHelper db = new SQLiteHelper(holder.itemView.getContext());

            int productID = Integer.parseInt(orders.getProductId());
            db.deleteProductFromOrder(productID);

            ordersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, ordersList.size());

            if(listener != null) {
                notifyQuantityChange();
            }
        });
    }

    private void notifyQuantityChange() {
        if (listener != null) {
            int totalItems = 0;
            double totalAmount = 0.0;

            for (Orders orders : ordersList) {
                totalItems += orders.getQuantity();
                totalAmount += orders.getTotalPrice() * orders.getQuantity();
            }

            listener.onQuantityChanged(totalItems, totalAmount);
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static  class CardViewHolder extends RecyclerView.ViewHolder {
        TextView textNameCard, textPriceCard, textNumberItemCard;
        ImageButton minusbuton, plusbuton;
        ImageView imageItemCard, deletecard;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            textNameCard = itemView.findViewById(R.id.textNameCard);
            textPriceCard = itemView.findViewById(R.id.textPriceCard);
            textNumberItemCard = itemView.findViewById(R.id.textNumberItemCard);
            minusbuton = itemView.findViewById(R.id.minusbuton);
            plusbuton = itemView.findViewById(R.id.plusbuton);
            imageItemCard = itemView.findViewById(R.id.imageItemCard);
            deletecard = itemView.findViewById(R.id.deletecard);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int totalItems, double totalAmount);
    }


}
