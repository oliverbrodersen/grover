package com.example.grover.ui.myfriends;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grover.data.HomeRepository;
import com.example.grover.models.FirebaseDatabaseUser;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsViewModel extends ViewModel {

    private HomeRepository repository;

    public MyFriendsViewModel() {
        repository = HomeRepository.getInstance();
    }


    public LiveData<FirebaseDatabaseUser> getFireBaseDatabaseUser(){
        return repository.getFireBaseDatabaseUser();
    }

    public void sendFriendRequest(String email) {
        repository.sendFriendRequest(email);
    }

    public void getFriends() {
        repository.getFriends();
    }

    public LiveData<List<FirebaseDatabaseUser>> getFriendList() {
        return repository.getFriendList();
    }
}