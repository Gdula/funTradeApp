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
    @NotBlank
    private Float price;
    @NotBlank
    private Item.Category category;
    @NotBlank
    private Item.Shape shape;
    @NotBlank
    private User owner;
}
