package com.increff.pos.model.data;

public class ProductData {
    private Long id;
    private String barcode;
    private String client;
    private String name;
    private Double mrp;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getClient() {
        return client;
    }

    public String getName() {
        return name;
    }

    public Double getMrp() {
        return mrp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
