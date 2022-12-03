package com.example.kr1;

import java.io.Serializable;

public class Product implements Serializable {
    public int imageSrc;
    public String name;
    public Integer cost;
    public String bitmapUrl;

    public Product(int image, String name, Integer cost) {
        imageSrc = image;
        this.name = name;
        this.cost = cost;
        bitmapUrl = null;
    }

    public Product(int src, String image, String name, Integer cost) {
        imageSrc = src;
        bitmapUrl = image;
        this.name = name;
        this.cost = cost;
    }
}
