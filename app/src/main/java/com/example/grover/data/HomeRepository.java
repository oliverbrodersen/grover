package com.example.grover.data;

import androidx.lifecycle.LiveData;

import com.example.grover.models.FirebaseDatabaseUser;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    public void getFriends() {
        homeDao.getFriends();
    }

    public LiveData<FirebaseDatabaseUser> getFireBaseDatabaseUser(){
        return homeDao.getFireBaseDatabaseUser();
    }
    public void sendFriendRequest(String email) {
        homeDao.sendFriendRequest(email);
    }

    public LiveData<List<FirebaseDatabaseUser>> getFriendList() {
        return homeDao.getFriendList();
    }

    public LiveData<Home> getHome(String homeId) {
        return homeDao.getHome(homeId);
    }

    public LiveData<Home> getFriendHome() {
        return homeDao.getFriendHome();
    }

    public FirebaseDatabaseUser getFriend(String userId) {
        return homeDao.getFriend(userId);
    }

    public void deletePlant(String plantId) {
        homeDao.deletePlant(plantId);
    }
}
