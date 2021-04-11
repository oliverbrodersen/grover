package com.example.grover.ui.plantinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.grover.Home;
import com.example.grover.data.HomeRepository;

public class PlantInfoViewModel extends ViewModel {

    private HomeRepository repository;

    public PlantInfoViewModel() {
        repository = HomeRepository.getInstance();
    }

    public LiveData<Home> getHome(){
        return repository.getHome();
    }
    public void updateHome(Home home){
        repository.updateHome(home);
    }
}
