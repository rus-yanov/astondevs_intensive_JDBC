package org.example.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private long id;
    private String name;
    private int price;
    private Category category;
    private List<Cart> cart;

}
