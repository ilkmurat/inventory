package com.murat.oneamz.inventory.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    @NotNull(message = "Please enter product name")
    private String name;
    private String description;
    @Min(value = 0, message = "Price should be greater than 0")
    private double price;
    @Min(value = 0, message = "Quantity should be greater than 0")
    private int quantity;
    @NotNull(message = "Please enter category name")
    private String categoryName;

}
