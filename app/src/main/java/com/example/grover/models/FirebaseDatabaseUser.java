package com.example.grover.models;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseUser {
    private String email;
    private String photoUrl;
    private String greenhouseId;
    private String name;
    private List<String> friendRequests;
    private List<String> friends;

    public FirebaseDatabaseUser() {
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    public FirebaseDatabaseUser(String email, String photoUrl, String greenhouseId, String name) {
        this.email = email;
        this.photoUrl = photoUrl;
        this.greenhouseId = greenhouseId;
        this.name = name;
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(String greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String friendId) {
        friends.add(friendId);
    }

    public boolean isNotAlreadyFriends(String id) {
        for (String friendId : friends){
            if (friendId.equals(id))
                return false;
        }
        return true;
    }
}
