package com.gdula.funTradeApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(100)")
    private String id;
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    private Float price;

    @NotBlank
    private Category category;
    public static enum Category {
        Book, Game, Music, Other
    }

    @NotBlank
    private Shape shape;
    public static enum Shape {
        NEW, USED
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
