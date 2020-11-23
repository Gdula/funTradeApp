package com.gdula.funTradeApp.dto;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateItem {
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    private Integer price;

    @NotBlank
    private Item.Category category;
    public static enum Category {
        Book, Game, Music, Other
    }

    @NotBlank
    private Item.Shape shape;
    public static enum Shape {
        NEW, USED
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
