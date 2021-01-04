package com.gdula.funTradeApp.service.dto;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateItemDto {
    @NotBlank
    @Size(min = 3)
    private String name;

    private Float price;

    @NotBlank
    @Size(min = 3)
    private String description;

    private Item.Category category;

    private Item.Shape shape;

    private User owner;
}
