package com.example.grover.ui.addplant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grover.data.HomeRepository;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AddPlantViewModel extends ViewModel {


    private HomeRepository repository;

    public AddPlantViewModel() {
        repository = HomeRepository.getInstance();
    }

    public LiveData<Home> getHome(){
        return repository.getHome();
    }

    public void addPlant(Plant plant){
        repository.addPlant(plant);
    }

    public void searchTrefle(String query) {
        repository.searchTrefle(query);
    }
    public LiveData<List<TrefleSearchQueryStripped>> getSearchResult(){
        return repository.getSearchResult();
    }

    public TrefleSearchQueryStripped selectSearchResult(int index) {
        return repository.selectSearchResult(index);
    }

    public void clearSearch() {
        repository.clearSearch();
    }
}