package com.increff.pos.pojo;


import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "barcode"))
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientPojo client;

    private String name;
    private Double mrp;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public ClientPojo getClient() {
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

    public void setClient(ClientPojo client) {
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
