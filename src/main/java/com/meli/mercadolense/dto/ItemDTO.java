package com.meli.mercadolense.dto;

public class ItemDTO {
    private String picture;
    private String tag;

    public ItemDTO(String picture, String tag) {
        this.picture = picture;
        this.tag = tag;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
