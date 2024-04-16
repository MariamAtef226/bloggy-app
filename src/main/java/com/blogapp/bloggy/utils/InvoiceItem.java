package com.blogapp.bloggy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double weight;
    private double width;
    private double height;
    private double depth;

    // Getters and setters
}
