package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.ItemDto;
import org.example.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemDaoTest {

    private static ItemDao itemDao;

    @BeforeEach
    public void beforeAll() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        itemDao = new ItemDao(new DBConfig());
    }

    @Test
    public void findAllTest() throws SQLException {
        List<ItemDto> list = itemDao.findAll();
        assertThat(list.size()).isEqualTo(12);
    }

    @Test
    public void findByIdTest() throws SQLException {
        Item item = itemDao.findById(1);

        String name = item.getName();
        String price = item.getPrice();
        String category = item.getCategory().getName();

        assertThat(name).isEqualTo("Mellon");
        assertThat(price).isEqualTo("4");
        assertThat(category).isEqualTo("Produce");
    }

    @Test
    public void saveTest() throws SQLException {
        itemDao.save("Zucchini", "5");

        List<ItemDto> list = itemDao.findAll();
        assertThat(list.size()).isEqualTo(13);

        Item item = itemDao.findById(13);

        assertThat(item.getName()).isEqualTo("Zucchini");
        assertThat(item.getPrice()).isEqualTo("5");
    }

    @Test
    public void deleteTest() throws SQLException {
        itemDao.delete(5);

        List<ItemDto> list = itemDao.findAll();
        assertThat(list.size()).isEqualTo(11);

        Item item = itemDao.findById(5);

        assertThat(item.getName()).isNull();
        assertThat(item.getPrice()).isNull();
    }

    @Test
    public void updateTest() throws SQLException {
        itemDao.update(7, "Bull's balls", "10");

        Item item = itemDao.findById(7);
        assertThat(item.getName()).isNotEqualTo("Turkey");
        assertThat(item.getName()).isEqualTo("Bull's balls");
        assertThat(item.getPrice()).isEqualTo("10");
    }
}
