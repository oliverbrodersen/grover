package com.example.grover.ui.myfriends;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.models.FirebaseDatabaseUser;
import com.example.grover.ui.home.HomeFragment;
import com.example.grover.ui.myfriends.rv.FriendAdapter;
import com.example.grover.ui.plantinfo.PlantInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsFragment extends Fragment implements FriendAdapter.OnListItemClickListener {

    private MyFriendsViewModel mViewModel;
    private RecyclerView friendList;
    private FriendAdapter friendAdapter;

    public static MyFriendsFragment newInstance() {
        return new MyFriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MyFriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_friends, container, false);

        Button addFriend = root.findViewById(R.id.button);
        EditText requestEmail = root.findViewById(R.id.editTextTextEmailAddress);

        friendList = root.findViewById(R.id.friendrv);
        friendList.hasFixedSize();
        friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getFriends();

        friendAdapter = new FriendAdapter(this);
        friendList.setAdapter(friendAdapter);

        mViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<List<FirebaseDatabaseUser>>() {
            @Override
            public void onChanged(List<FirebaseDatabaseUser> firebaseDatabaseUsers) {
                friendAdapter.setItemList((ArrayList<FirebaseDatabaseUser>) firebaseDatabaseUsers);
                friendList.setAdapter(friendAdapter);
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmailAddress(requestEmail.getText().toString())){
                    Toast.makeText(getContext(), "Request sent to: " + requestEmail.getText(), Toast.LENGTH_SHORT).show();
                    mViewModel.sendFriendRequest(requestEmail.getText().toString());
                    requestEmail.setText("");
                }
                else {
                    Toast.makeText(getContext(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
                closeKeyboard();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyFriendsViewModel.class);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //Toast.makeText(getContext(), "Visiting " + mViewModel.getFriendList().getValue().get(clickedItemIndex).getName(), Toast.LENGTH_SHORT).show();
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("HomeId", mViewModel.getFriendList().getValue().get(clickedItemIndex).getGreenhouseId());
        bundle.putString("HomeOwner", mViewModel.getFriendList().getValue().get(clickedItemIndex).getName());
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("tag").commit();
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = getActivity().getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

}