package com.doan.pharcity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.pharcity.R;

import java.util.List;

public class FilterAdapter extends  RecyclerView.Adapter<FilterAdapter.ViewHolder>{
    private List<String> filterList;
    private OnFilterClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        String filter = filterList.get(position);
        holder.filterTextView.setText(filter);
        holder.itemView.setOnClickListener(v -> listener.onFilterClick(filter));

    }
    @Override
    public int getItemCount() {
        return filterList.size();
    }

    private interface  OnFilterClickListener{
        void onFilterClick(String filter);

    }
    public FilterAdapter(List<String> filterList, OnFilterClickListener listener){
        this.filterList = filterList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterTextView = itemView.findViewById(R.id.filterTextView);
        }
    }
}



