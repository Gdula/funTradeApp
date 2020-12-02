package com.gdula.funTradeApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(100)")
    private String id;
    @NotBlank
    @Size(min = 3)
    private String login;
    @NotBlank
    @Size(min = 2)
    private String name;
    @NotBlank
    @Size(min = 2)
    private String surname;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    @Size(min = 3)
    private String address;
    @NotBlank
    @Size(min = 3)
    private String city;
    @NotBlank
    @Size(min = 3)
    private String zip;
    @NotBlank
    private String mail;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Item> items;



}
