package com.example.grover.models.trefle;

public class TrefleSearchQueryStripped {
    public String common_name;
    public String species;
    public int id;
    public boolean selected;
    public String image_url;

    public TrefleSearchQueryStripped(String common_name, String species, int id, String image_url) {
        this.common_name = common_name;
        this.species = species;
        this.id = id;
        this.image_url = image_url;
        selected = false;
    }
}
