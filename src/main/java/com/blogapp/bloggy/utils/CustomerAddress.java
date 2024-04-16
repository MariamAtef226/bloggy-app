package com.blogapp.bloggy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {
    private String block;
    private String street;
    private String houseBuildingNo;
    private String addressInstructions;

    // Getters and setters
}