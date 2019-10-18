package com.meli.mercadolense.dto;

public class ProductSearchDTO {
    private String id;
    private double scoring;
    private String name;

    public ProductSearchDTO() {
    }

    public ProductSearchDTO(String id, double scoring, String name) {
        this.id = id;
        this.scoring = scoring;
        this.name = name;
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
}
