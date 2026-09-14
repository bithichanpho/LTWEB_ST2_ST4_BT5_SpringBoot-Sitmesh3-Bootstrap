package com.webprogramming.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSales {
    private String productId;
    private String productName;
    private long sold;
    private double revenue;
}
