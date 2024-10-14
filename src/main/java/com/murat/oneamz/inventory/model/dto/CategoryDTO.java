package com.murat.oneamz.inventory.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    @NotNull(message = "Please enter category name")
    private String name;
}
