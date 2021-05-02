package com.example.grover.data;

import androidx.lifecycle.LiveData;

import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

    public void updateTrefleData(Plant plant){
        homeDao.updateTrefleData(plant);
    };

    public void setUser(FirebaseUser user){
        homeDao.setUser(user);
    }
    public FirebaseUser getUser(){
        return homeDao.getUser();
    }

    public void addPlant(Plant newPlant, Plant oldPlant) {
        homeDao.addPlant(newPlant, oldPlant);
    }
    public Plant getPlantFromName(String name){
        return homeDao.getPlantFromName(name);
    }
    public void searchTrefle(String query) {
        homeDao.searchTrefle(query);
    }
    public LiveData<List<TrefleSearchQueryStripped>> getSearchResult(){
        return homeDao.getSearchResult();
    }

    public TrefleSearchQueryStripped selectSearchResult(int index) {
        return homeDao.selectSearchResult(index);
    }

    public void clearSearch() {
        homeDao.clearSearch();
    }

    public void updateDatabase() {
        homeDao.updateDatabase();
    }
}
