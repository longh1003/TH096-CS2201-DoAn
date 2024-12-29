package com.doan.pharcity.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.doan.pharcity.Adapter.ProductAdapter;
import com.doan.pharcity.Adapter.ProductDropdownAdapter;
import com.doan.pharcity.R;
import com.doan.pharcity.SQLiteHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;
    private SearchView searchView;
    private List<Integer> imageList;
    private Handler slideHandler;
    private ViewPager2 slideViewPager;
    private PopupWindow popupWindow;
    private RecyclerView popupRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        slideViewPager = rootView.findViewById(R.id.slideViewPager);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        androidx.appcompat.widget.SearchView searchView = rootView.findViewById(R.id.searchView);
        Spinner filterSpinner = rootView.findViewById(R.id.filterSpinner);


        //Load product


        //Spiner
        List<String> filterList = Arrays.asList("Tất cả", "Mới nhất", "Giá tăng dần", "Giá giảm dần");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter1);


        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                filteredList.clear();
                if (selectedFilter.equals("Tất cả")) {
                    filteredList.addAll(productList);
                } else if (selectedFilter.equals("Mới nhất")) {
                    List<Product> sortedList = productList.stream()
                            .sorted((p1, p2) -> p2.getId() - p1.getId())
                            .collect(Collectors.toList());
                    Log.d("Filter", "Sorted by Newest: " + sortedList.size());
                    filteredList.addAll(sortedList);
                } else if (selectedFilter.equals("Giá tăng dần")) {
                    List<Product> sortedList = productList.stream()
                            .sorted((p1, p2) -> (int) (p1.getPrice() - p2.getPrice()))
                            .collect(Collectors.toList());
                    Log.d("Filter", "Sorted by Price Ascending: " + sortedList.size());
                    filteredList.addAll(sortedList);
                } else if (selectedFilter.equals("Giá giảm dần")) {
                    List<Product> sortedList = productList.stream()
                            .sorted((p1, p2) -> (int) (p2.getPrice() - p1.getPrice()))
                            .collect(Collectors.toList());
                    Log.d("Filter", "Sorted by Price Descending: " + sortedList.size());
                    filteredList.addAll(sortedList);
                }
                adapter.notifyDataSetChanged();
                adapter.updateData(filteredList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có lựa chọn được chọn
            }
        });



        slideViewPager = rootView.findViewById(R.id.slideViewPager);


        SQLiteHelper dbHelper = new SQLiteHelper(getActivity());
        productList = dbHelper.getAllProducts();


        //Nếu không có dữ liệu hiển thị thông báo mặc định

        if(productList.isEmpty()){
            productList.add(new Product(getId(),"Không có sản phẩm nào", "Hiện tại chưa có dữ lệuh"
                    , 0, R.drawable.banner1, null));
        }

        //setup adapter
        filteredList = new ArrayList<>(productList);
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        //Setup searchView
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Product> filteredProducts = new ArrayList<>();
                for (Product product : productList) {
                    if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredProducts.add(product);
                    }
                }

                if (!filteredProducts.isEmpty() && !newText.isEmpty()) {
                    showDropDown(searchView, filteredProducts); // Hiển thị popup khi có sản phẩm phù hợp
                } else if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss(); // Đóng popup nếu không có sản phẩm phù hợp
                }
                return true;
            }
        });


        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
        searchView.clearFocus();
        return rootView;
    }

    private void showDropDown(View anchorView, List<Product> productList){
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.dropdown_popup, null);
        popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProductDropdownAdapter dropdownAdapter = new ProductDropdownAdapter(productList);
        popupRecyclerView.setAdapter(dropdownAdapter);

        popupWindow = new PopupWindow(popupView, anchorView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(5);

        popupWindow.showAsDropDown(anchorView, 0, 5);

    }

    private void setupAutoSlide() {
        slideHandler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPosition = 0;

            @Override
            public void run() {
                if (currentPosition == imageList.size()) {
                    currentPosition = 0;
                }
                slideViewPager.setCurrentItem(currentPosition++, true);
                slideHandler.postDelayed(this, 3000); // Slide mỗi 3 giây
            }
        };
        slideHandler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (slideHandler != null) {
            slideHandler.removeCallbacksAndMessages(null);
        }
    }


    public static class AccountFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public AccountFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static AccountFragment newInstance(String param1, String param2) {
            AccountFragment fragment = new AccountFragment();
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_account, container, false);
        }
    }
}
