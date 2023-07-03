package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class Cart {

    private long id;
    private String description;
    private List<Item> itemList;

    public Cart(long id, String description) {
        this.id = id;
        this.description = description;
    }
}
