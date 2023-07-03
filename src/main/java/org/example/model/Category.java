package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class Category {

    private long id;
    private String name;
    private List<Item> itemList;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
