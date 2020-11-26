package com.gdula.funTradeApp.service.dto;

import com.gdula.funTradeApp.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    @Size(min = 3)
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
    @Size(min = 6)
    private String zip;
    @NotBlank
    @Pattern(regexp = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
            message = "Podany mail jest nieprawidłowy")
    private String mail;
    private List<Item> items;
}
