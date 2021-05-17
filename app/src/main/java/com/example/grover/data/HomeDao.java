package com.example.grover.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.grover.models.FirebaseDatabaseUser;
import com.example.grover.models.FirebaseHome;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.R;
import com.example.grover.models.trefle.TrefleJSONRecived;
import com.example.grover.models.trefle.TreflePlant;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.example.grover.models.trefle.trefleSpeciesComplete.Data;
import com.example.grover.models.trefle.trefleSpeciesComplete.Root;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private MutableLiveData<Home> friendHome;
    private MutableLiveData<List<TrefleSearchQueryStripped>> searchQuery;
    private FirebaseUser user;
    private MutableLiveData<FirebaseDatabaseUser> firebaseDatabaseUser;
    private MutableLiveData<List<FirebaseDatabaseUser>> friendList;
    private static HomeDao instance;
    private FirebaseDatabase database;


    private HomeDao(){
        database = FirebaseDatabase.getInstance();

        home = new MutableLiveData<>();
        friendHome = new MutableLiveData<>();
        searchQuery = new MutableLiveData<>();
        firebaseDatabaseUser = new MutableLiveData<>();
        friendList = new MutableLiveData<>();
        friendList.setValue(new ArrayList<FirebaseDatabaseUser>());

        List<TrefleSearchQueryStripped> sq = new ArrayList<>();
        searchQuery.setValue(sq);

        home.observeForever(new Observer<Home>() {
            @Override
            public void onChanged(Home home) {
                DatabaseReference myRef = database.getReference("Homes").child(user.getUid());// Read from the database
                myRef.setValue(home.getAsFirebaseHome());
            }
        });
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
                    Home h1 = home.getValue();
                    int i = h1.getPlants().indexOf(plant);
                    h1.getPlants().get(i).setTreflePlantInfo(response.body().data);
                    home.setValue(h1);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.i("Retrofit", "Failed to get data from trefle.io: " + t.toString());
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
        Call<TrefleJSONRecived> call = trefleApi.search(query);
        call.enqueue(new Callback<TrefleJSONRecived>() {
            @Override
            public void onResponse(Call<TrefleJSONRecived> call, Response<TrefleJSONRecived> response) {
                if (response.code() == 200) {
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
                Log.i("Retrofit", "Failed to get search results from trefle.io: " + t.toString());
            }
        });
    }

    public void getHomeFromFirebase(String id){
        DatabaseReference myRef = database.getReference("Homes").child(id);// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue(FirebaseHome.class) != null)
                    home.setValue(new Home(dataSnapshot.getValue(FirebaseHome.class)));
                else
                    home.setValue(new Home(new FirebaseHome()));
                //Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void addPlant(Plant newPlant, Plant oldPlant) {
        Home h = home.getValue();
        if (h.getPlantById(newPlant.getPlantId()) == null)
            h.addPlant(newPlant);
        else{
            h.editPlant(newPlant, oldPlant);
        }
        home.setValue(h);
        updateDatabase();
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

    public void setUser(FirebaseUser user) {
        this.user = user;
        //This checks if user has been loged in before. If not they will be setup in the system
        getUserFromFirebase(user);
        getHomeFromFirebase(user.getUid());
    }

    public void getUserFromFirebase(FirebaseUser user){
        DatabaseReference myRef = database.getReference("Users").child(user.getUid());// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() == null){
                    myRef.setValue(new FirebaseDatabaseUser(user.getEmail(), user.getPhotoUrl().toString(), user.getUid(), user.getDisplayName()));
                    //TODO do some sort of tutorial here
                }
                else
                    firebaseDatabaseUser.setValue(dataSnapshot.getValue(FirebaseDatabaseUser.class));
                //Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
    public FirebaseUser getUser(){
        return user;
    }
    public LiveData<FirebaseDatabaseUser> getFireBaseDatabaseUser(){
        return firebaseDatabaseUser;
    }

    public void updateDatabase() {
        DatabaseReference myRef = database.getReference("Homes").child(user.getUid());// Read from the database
        myRef.setValue(home.getValue().getAsFirebaseHome());
    }

    public MutableLiveData<List<FirebaseDatabaseUser>> getFriendList() {
        return friendList;
    }

    public void getFriends() {
        if (firebaseDatabaseUser.getValue().getFriends() != null){
            friendList.setValue(new ArrayList<FirebaseDatabaseUser>());
            for (String friend:firebaseDatabaseUser.getValue().getFriends()) {
                DatabaseReference myRef = database.getReference("Users").child(friend);// Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Friends", dataSnapshot.getValue(FirebaseDatabaseUser.class).getEmail());
                        friendList.getValue().add(dataSnapshot.getValue(FirebaseDatabaseUser.class));
                        friendList.setValue(friendList.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("Friends", "Failed to read value.", error.toException());
                    }
                });
            }
        }
    }

    public void sendFriendRequest(String email) {
        DatabaseReference myRef = database.getReference("Users");// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (postSnapshot.getValue(FirebaseDatabaseUser.class).getEmail().toLowerCase().equals(email.toLowerCase())){
                        FirebaseDatabaseUser friendRef = postSnapshot.getValue(FirebaseDatabaseUser.class);
                        if (firebaseDatabaseUser.getValue().isNotAlreadyFriends(friendRef.getGreenhouseId())){
                            //Tell other user that you are friends now
                            friendRef.addFriend(firebaseDatabaseUser.getValue().getGreenhouseId());
                            DatabaseReference myRefFriend = database.getReference("Users").child(friendRef.getGreenhouseId());// Read from the database
                            myRefFriend.setValue(friendRef);

                            //Update your own friendlist
                            firebaseDatabaseUser.getValue().addFriend(friendRef.getGreenhouseId());
                            DatabaseReference myRefFriends = database.getReference("Users").child(firebaseDatabaseUser.getValue().getGreenhouseId());// Read from the database
                            myRefFriends.setValue(firebaseDatabaseUser.getValue());
                        }
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Friends", "Failed to read value.", error.toException());
            }
        });
    }

    public LiveData<Home> getHome(String homeId) {
        DatabaseReference myRef = database.getReference("Homes").child(homeId);// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue(FirebaseHome.class) != null)
                    friendHome.setValue(new Home(dataSnapshot.getValue(FirebaseHome.class)));
                else
                    friendHome.setValue(new Home(new FirebaseHome()));
                //Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        return friendHome;
    }

    public LiveData<Home> getFriendHome() {
        return friendHome;
    }

    public FirebaseDatabaseUser getFriend(String userId) {
        for (FirebaseDatabaseUser friend:friendList.getValue()) {
            if (friend.getGreenhouseId().equals(userId))
                return friend;
        }
        return null;
    }

    public void deletePlant(String plantId) {
        home.getValue().getPlants().remove(home.getValue().getPlantById(plantId));
        home.setValue(home.getValue());
    }
}
