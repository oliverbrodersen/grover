package com.example.grover.models.trefle.trefleSpeciesComplete;

import java.util.List;

public class Data {
    private int id;
    private String common_name;
    private String slug;
    private String scientific_name;
    private int main_species_id;
    private String image_url;
    private int year;
    private String bibliography;
    private String author;
    private String family_common_name;
    private int genus_id;
    private String observations;
    private boolean vegetable;
    private Links links;
    private MainSpecies main_species;
    private Genus genus;
    private Family family;
    private List<Species> species;
    private List<Object> subspecies;
    private List<Object> varieties;
    private List<Object> hybrids;
    private List<Object> forms;
    private List<Object> subvarieties;
    private List<Source> sources;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public int getMain_species_id() {
        return main_species_id;
    }

    public void setMain_species_id(int main_species_id) {
        this.main_species_id = main_species_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFamily_common_name() {
        return family_common_name;
    }

    public void setFamily_common_name(String family_common_name) {
        this.family_common_name = family_common_name;
    }

    public int getGenus_id() {
        return genus_id;
    }

    public void setGenus_id(int genus_id) {
        this.genus_id = genus_id;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public boolean isVegetable() {
        return vegetable;
    }

    public void setVegetable(boolean vegetable) {
        this.vegetable = vegetable;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public MainSpecies getMain_species() {
        return main_species;
    }

    public void setMain_species(MainSpecies main_species) {
        this.main_species = main_species;
    }

    public Genus getGenus() {
        return genus;
    }

    public void setGenus(Genus genus) {
        this.genus = genus;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public List<Species> getSpecies() {
        return species;
    }

    public void setSpecies(List<Species> species) {
        this.species = species;
    }

    public List<Object> getSubspecies() {
        return subspecies;
    }

    public void setSubspecies(List<Object> subspecies) {
        this.subspecies = subspecies;
    }

    public List<Object> getVarieties() {
        return varieties;
    }

    public void setVarieties(List<Object> varieties) {
        this.varieties = varieties;
    }

    public List<Object> getHybrids() {
        return hybrids;
    }

    public void setHybrids(List<Object> hybrids) {
        this.hybrids = hybrids;
    }

    public List<Object> getForms() {
        return forms;
    }

    public void setForms(List<Object> forms) {
        this.forms = forms;
    }

    public List<Object> getSubvarieties() {
        return subvarieties;
    }

    public void setSubvarieties(List<Object> subvarieties) {
        this.subvarieties = subvarieties;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
