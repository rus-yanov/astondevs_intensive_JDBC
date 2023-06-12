package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    private int id;
    private String name;
    private int price;
    private CategoryDto category;

}
