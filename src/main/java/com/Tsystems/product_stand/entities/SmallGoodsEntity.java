package com.Tsystems.product_stand.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "goods")
public class SmallGoodsEntity implements Serializable {
    @Id
    @Column (name = "id_goods")
    private int id;
    @Column (name = "name")
    private String name;
    @Column (name = "price")
    private float price;
    @Column (name = "img")
    private String img;
    @Column (name = "sales_counter")
    private int salesCounter;

    public SmallGoodsEntity(){

    }

    public SmallGoodsEntity(String name, float price, String img, int salesCounter) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.salesCounter = salesCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSalesCounter() {
        return salesCounter;
    }

    public void setSalesCounter(int salesCounter) {
        this.salesCounter = salesCounter;
    }
}
