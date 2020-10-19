package com.unipi.p15013p15120.kastropoliteiesv2;

//class pou antloume dedomena apo pinaka TOPOTHESIES
public class Topothesia {

    private String attraction_name;
    private int alternative_tourism;
    private int attraction;
    private String climate;
    private String distance_athens;
    private String distance_kalamata;
    private int flat;
    private int history;
    private String image;
    private double latitude;
    private double longitude;
    private int mountain;
    private int night_life;
    private int popularity;
    private String region_name_gr;
    private String region_name_grkl;
    private int seaside;
    private int significant_people;
    private int stay;
    private String synopsis;
    private String type_alternative_tourism;
    private int wildlife;

    public Topothesia() {}
    public Topothesia(String attraction_name, int alternative_tourism, int attraction, String climate, String distance_athens, String distance_kalamata,
                      int flat, int history, String image, Double latitude, Double longitude, int mountain, int night_life, int popularity, String region_name_gr, String region_name_grkl,
                      int seaside, int significant_people, int stay, String synopsis, String type_alternative_tourism, int wildlife)
    {
        this.attraction_name = attraction_name;
        this.alternative_tourism = alternative_tourism;
        this.attraction = attraction;
        this.climate = climate;
        this.distance_athens = distance_athens;
        this.distance_kalamata = distance_kalamata;
        this.flat = flat;
        this.history = history;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mountain = mountain;
        this.night_life = night_life;
        this.popularity = popularity;
        this.region_name_gr = region_name_gr;
        this.region_name_grkl = region_name_grkl;
        this.seaside = seaside;
        this.significant_people = significant_people;
        this.stay = stay;
        this.synopsis = synopsis;
        this.type_alternative_tourism = type_alternative_tourism;
        this.wildlife = wildlife;
    }

    public String getAttraction_name() {
        return attraction_name;
    }

    public void setAttraction_name(String attraction_name) {
        this.attraction_name = attraction_name;
    }

    public int getAlternative_tourism() {
        return alternative_tourism;
    }

    public void setAlternative_tourism(int alternative_tourism) {
        this.alternative_tourism = alternative_tourism;
    }

    public int getAttraction() {
        return attraction;
    }

    public void setAttraction(int attraction) {
        this.attraction = attraction;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getDistance_athens() {
        return distance_athens;
    }

    public void setDistance_athens(String distance_athens) {
        this.distance_athens = distance_athens;
    }

    public String getDistance_kalamata() {
        return distance_kalamata;
    }

    public void setDistance_kalamata(String distance_kalamata) {
        this.distance_kalamata = distance_kalamata;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMountain() {
        return mountain;
    }

    public void setMountain(int mountain) {
        this.mountain = mountain;
    }

    public int getNight_life() {
        return night_life;
    }

    public void setNight_life(int night_life) {
        this.night_life = night_life;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getRegion_name_gr() {
        return region_name_gr;
    }

    public void setRegion_name_gr(String region_name_gr) {
        this.region_name_gr = region_name_gr;
    }

    public String getRegion_name_grkl() {
        return region_name_grkl;
    }

    public void setRegion_name_grkl(String region_name_grkl) {
        this.region_name_grkl = region_name_grkl;
    }

    public int getSignificant_people() {
        return significant_people;
    }

    public void setSignificant_people(int significant_people) {
        this.significant_people = significant_people;
    }

    public int getSeaside() {
        return seaside;
    }

    public void setSeaside(int seaside) {
        this.seaside = seaside;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getType_alternative_tourism() {
        return type_alternative_tourism;
    }

    public void setType_alternative_tourism(String type_alternative_tourism) {
        this.type_alternative_tourism = type_alternative_tourism;
    }

    public int getWildlife() {
        return wildlife;
    }

    public void setWildlife(int wildlife) {
        this.wildlife = wildlife;
    }
}
