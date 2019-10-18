package com.meli.mercadolense.dto;

public class ProductSearchDTO {
    private String id;
    private double scoring;
    private String name;
    private String url;
    private String imageUrl;

    public ProductSearchDTO() {
    }

    public ProductSearchDTO(String id, double scoring, String name, String url, String imageUlr) {
        this.id = id;
        this.scoring = scoring;
        this.name = name;
        this.url = url;
        this.imageUrl = imageUlr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScoring() {
        return scoring;
    }

    public void setScoring(double scoring) {
        this.scoring = scoring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
