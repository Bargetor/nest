package com.bargetor.other.bean;

/**
 * Created by Bargetor on 16/3/17.
 */
public class Food {
    private String description;
    private int food_id;
    private String food_name;
    private int has_activity;
    private int is_featured;
    private int is_gum;
    private int is_new;
    private int is_spicy;
    private int is_valid;
    private String[] num_ratings;
    private double price;
    private int recent_popularity;
    private double recent_rating;
    private String restaurant_id; 
    private String restaurant_name; 
    private int stock;
    private String image_url;
    private double packing_fee;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getHas_activity() {
        return has_activity;
    }

    public void setHas_activity(int has_activity) {
        this.has_activity = has_activity;
    }

    public int getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(int is_featured) {
        this.is_featured = is_featured;
    }

    public int getIs_gum() {
        return is_gum;
    }

    public void setIs_gum(int is_gum) {
        this.is_gum = is_gum;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getIs_spicy() {
        return is_spicy;
    }

    public void setIs_spicy(int is_spicy) {
        this.is_spicy = is_spicy;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }

    public String[] getNum_ratings() {
        return num_ratings;
    }

    public void setNum_ratings(String[] num_ratings) {
        this.num_ratings = num_ratings;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRecent_popularity() {
        return recent_popularity;
    }

    public void setRecent_popularity(int recent_popularity) {
        this.recent_popularity = recent_popularity;
    }

    public double getRecent_rating() {
        return recent_rating;
    }

    public void setRecent_rating(double recent_rating) {
        this.recent_rating = recent_rating;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getPacking_fee() {
        return packing_fee;
    }

    public void setPacking_fee(double packing_fee) {
        this.packing_fee = packing_fee;
    }
}
