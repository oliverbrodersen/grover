package com.example.grover.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grover.Home;
import com.example.grover.data.HomeRepository;

public class HomeViewModel extends ViewModel {

    private HomeRepository repository;

    public HomeViewModel() {
         repository = HomeRepository.getInstance();
    }

    public LiveData<Home> getHome(){
        return repository.getHome();
    }
    public void updateHome(Home home){
        repository.updateHome(home);
    }
}