package com.example.grover.data;

import androidx.lifecycle.LiveData;

import com.example.grover.Home;

public class HomeRepository {
    private HomeDao homeDao;
    private static HomeRepository instance;

    private HomeRepository(){
        homeDao = HomeDao.getInstance();
    }

    public static HomeRepository getInstance(){
        if (instance == null)
            instance = new HomeRepository();
        return instance;
    }
    public LiveData<Home> getHome(){
        return homeDao.getHome();
    }

    public void updateHome(Home home) {
        homeDao.updateHome(home);
    }
}
