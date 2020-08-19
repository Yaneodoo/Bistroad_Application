package com.example.yaneodoo.Info;

public class Review {
    private String orderId;
    private String contents;
    private String id;
    private String stars;
    private String writerId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    @Override
    public String toString() {
        return "[orderId = " + orderId + ", contents = " + contents + ", id = " + id + ", stars = " + stars + ", writerId = " + writerId + "]";
    }
}