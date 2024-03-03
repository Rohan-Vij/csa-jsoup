package com.csa.app;

import java.util.ArrayList;

public class ReviewList {
    private ArrayList<Review> reviews;
    private String itemID;

    public ReviewList(String itemID) {
        this.itemID = itemID;
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String getItemID() {
        return itemID;
    }

    @Override
    public String toString() {
        return "ReviewList{" +
                "reviews=" + reviews +
                ", itemID='" + itemID + '\'' +
                '}';
    }
}
