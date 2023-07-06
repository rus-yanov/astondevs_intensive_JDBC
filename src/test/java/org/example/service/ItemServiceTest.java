package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.ItemDao;
import org.example.dto.ItemDto;
import org.example.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemServiceTest {

    private static ItemDao itemDao;
    private static ItemService itemService;

    @BeforeEach
    public void beforeAll() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        itemDao = new ItemDao(new DBConfig());
        itemService = new ItemService(itemDao);
    }

    @Test
    public void getAllTest() throws SQLException {
        List<ItemDto> list = itemService.getAll();
        assertThat(list.size()).isEqualTo(12);
    }

    @Test
    public void getByIdTest() throws SQLException {
        Item item = itemService.getById(8);
        assertThat(item.getId()).isEqualTo(8);
        assertThat(item.getName()).isEqualTo("Beef");
        assertThat(item.getPrice()).isEqualTo("15");
        assertThat(item.getCategory().getName()).isEqualTo("Meat");
    }

    @Test
    public void createTest() throws SQLException {
        itemService.create("Bottled water", "2");

        List<ItemDto> list = itemDao.findAll();

        assertThat(list.size()).isEqualTo(13);
    }

    @Test
    public void updateTest() throws SQLException {
        itemService.update(12, "Yoghurt", "4");

        Item item = itemDao.findById(12);

        assertThat(item.getName()).isEqualTo("Yoghurt");
        assertThat(item.getPrice()).isEqualTo("4");
    }

    @Test
    public void deleteTest() throws SQLException {
        itemService.delete(1);

        List<ItemDto> list = itemDao.findAll();

        assertThat(list.size()).isEqualTo(11);
    }
}
