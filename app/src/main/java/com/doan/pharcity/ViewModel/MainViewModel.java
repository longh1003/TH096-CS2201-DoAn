package com.doan.pharcity.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doan.pharcity.Model.SliderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private MutableLiveData<List<SliderModel>> slider = new MutableLiveData<>();

    public LiveData<List<SliderModel>> getSlider() {
        return slider;
    }

    public void loadSlider() {
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SliderModel> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SliderModel sliderModel = dataSnapshot.getValue(SliderModel.class);
                    if (list!=null){
                        list.add(sliderModel);
                    }
                    slider.setValue(list);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
