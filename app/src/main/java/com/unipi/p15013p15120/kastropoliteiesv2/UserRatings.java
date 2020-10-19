package com.unipi.p15013p15120.kastropoliteiesv2;

//class pou antloume dedomena gia ton pinaka RATINGS
public class UserRatings {
    private int id;
    private int episkepsimotita;
    private int rating;
    private String topothesia_id;

    public UserRatings() {}

    public UserRatings(int id, int episkepsimotita, int rating, String topothesia_id)
    {
        this.episkepsimotita = episkepsimotita;
        //uid of user
        this.id = id;
        this.rating = rating;
        this.topothesia_id = topothesia_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEpiskepsimotita() {
        return episkepsimotita;
    }

    public void setEpiskepsimotita(int episkepsimotita) {
        this.episkepsimotita = episkepsimotita;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTopothesia_id() {
        return topothesia_id;
    }

    public void setTopothesia_id(String topothesia_id) {
        this.topothesia_id = topothesia_id;
    }
}
