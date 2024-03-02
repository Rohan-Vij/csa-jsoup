package com.csa.app;

public class Review {
    private int rating;
    private boolean verified;
    private String date;
    private String content;
    private String author;

    public Review(int rating, boolean verified, String date, String content, String author) {
        this.rating = rating;
        this.verified = verified;
        this.date = date;
        this.content = content;
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", verified=" + verified +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
