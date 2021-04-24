package com.example.grover.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.R;
import com.example.grover.models.trefle.TrefleJSONRecived;
import com.example.grover.models.trefle.TreflePlant;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.example.grover.models.trefle.trefleSpeciesComplete.Data;
import com.example.grover.models.trefle.trefleSpeciesComplete.Root;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDao {
    private MutableLiveData<Home> home;
    private MutableLiveData<List<TrefleSearchQueryStripped>> searchQuery;
    private static HomeDao instance;


    private HomeDao(){
        home = new MutableLiveData<>();
        searchQuery = new MutableLiveData<>();

        ArrayList<Plant> plants = new ArrayList<>();
        Plant p1 = new Plant("Fredslilje", "Spathiphyllum", R.drawable.p2, 5, "19-04-2021", 3, 223320, 1, "Fik den af min farmor");
        p1.log("Water");
        p1.log("Water");
        p1.log("Fertilizer");
        p1.log("Water");
        p1.log("Fertilizer");
        p1.log("Repotting");
        p1.log("Water");
        p1.log("Water");
        plants.add(p1);
        plants.add(new Plant("Philodendron","Philodendron Red Beauty", R.drawable.p1, 14, "29-03-2021", 3, 0, 1, "Aflæs på vandmåleren om den skal vandes inden vanding"));
        plants.add(new Plant("Cocospalme", "Cocos nucifera", R.drawable.p3, 8, "17-04-2021", 1,122263, 2, "Ikke en kokos plamle"));
        plants.add(new Plant("Banantræ", "Bananus fantomium", R.drawable.p4, 15,"17-04-2021", 1,0, 1, "Kan ikke dø"));
        plants.add(new Plant("Trailing Jade", "Bananus fantomium", R.drawable.p5, 15,"17-04-2021", 2,0,1,""));
        plants.add(new Plant("Nerve plante", "Fittonia", R.drawable.p6, 5,"19-04-2021", 2,196579,1,"Viser når den skal vandes vd at dø"));
        plants.add(new Plant("Guldranke", "Epipremnum Aureum", R.drawable.p7, 10,"17-04-2021", 2,132809, 1,""));

        Home _home = new Home(420, plants);
        _home.addRoom("Living room", 1);
        _home.addRoom("Kitchen", 2);
        home.setValue(_home);

        List<TrefleSearchQueryStripped> sq = new ArrayList<TrefleSearchQueryStripped>();
        searchQuery.setValue(sq);
    }

    public static HomeDao getInstance(){
        if (instance == null)
            instance = new HomeDao();
        return instance;
    }
    public LiveData<Home> getHome(){
        return home;
    }

    public void updateHome(Home home) {
        this.home.setValue(home);
    }

    public LiveData<List<TrefleSearchQueryStripped>> getSearchResult(){
        return searchQuery;
    }

    public void updateTrefleData(Plant plant){
        TrefleApi trefleApi = ServiceGenerator.getTrefleApi();
        Call<Root> call = trefleApi.getDataFromId(plant.getTrefleId());
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.code() == 200) {
                    Log.e("Trefle", response.toString());
                    Home h1 = home.getValue();
                    int i = h1.getPlants().indexOf(plant);
                    h1.getPlants().get(i).setTreflePlantInfo(response.body().data);
                    home.setValue(h1);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }

    public void searchTrefle(String query) {
        TrefleSearchQueryStripped tp = null;
        if (searchQuery.getValue().size() > 0)
            tp = searchQuery.getValue().get(0);

        if (tp != null){
            if (tp.species.equals(query)){
                return;
            }
        }

        TrefleApi trefleApi = ServiceGenerator.getTrefleApi();
        Log.d("Trefle", query);
        Call<TrefleJSONRecived> call = trefleApi.search(query);
        call.enqueue(new Callback<TrefleJSONRecived>() {
            @Override
            public void onResponse(Call<TrefleJSONRecived> call, Response<TrefleJSONRecived> response) {
                if (response.code() == 200) {
                    Log.e("Trefle", response.toString());
                    List<TrefleSearchQueryStripped> sq = new ArrayList<TrefleSearchQueryStripped>();
                    for (TreflePlant tp:response.body().data) {
                        if (sq.size() > 4)
                            break;
                        sq.add(new TrefleSearchQueryStripped(tp.common_name,tp.scientific_name,tp.id,tp.image_url));
                    }
                    searchQuery.setValue(sq);
                }
            }

            @Override
            public void onFailure(Call<TrefleJSONRecived> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong with search :(");
            }
        });
    }

    public void addPlant(Plant plant) {
        Home h = home.getValue();
        h.addPlant(plant);
        home.setValue(h);
    }

    public Plant getPlantFromName(String name) {
        ArrayList<Plant> plantList = home.getValue().getPlants();
        for (Plant p:
             plantList) {
            if (p.getName().equals(name))
                return p;
        }
        return null;
    }

    public TrefleSearchQueryStripped selectSearchResult(int index) {
        TrefleSearchQueryStripped tp = searchQuery.getValue().get(index);
        tp.selected = true;
        ArrayList<TrefleSearchQueryStripped> tpList = new ArrayList<>();
        tpList.add(tp);
        searchQuery.setValue(tpList);
        return tp;
    }

    public void clearSearch() {
        ArrayList<TrefleSearchQueryStripped> tpList = new ArrayList<>();
        searchQuery.setValue(tpList);
    }
}
