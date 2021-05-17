package com.example.grover.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.grover.models.FirebaseDatabaseUser;
import com.example.grover.models.Home;
import com.example.grover.data.HomeRepository;

import javax.net.ssl.SSLSession;

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

    public void updateDatabase() {
        repository.updateDatabase();
    }

    public LiveData<Home> getHome(String homeId) {
        return repository.getHome(homeId);
    }

    public LiveData<Home> getFriendHome() {
        return repository.getFriendHome();
    }

    public FirebaseDatabaseUser getFriend(String userId) {
        return repository.getFriend(userId);
    }
}