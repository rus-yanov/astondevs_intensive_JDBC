package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoryDaoTest {

    private static CategoryDao categoryDao;

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        categoryDao = new CategoryDao(new DBConfig());
    }

    @Test
    public void findAllTest() throws SQLException {
        List<CategoryDto> list = categoryDao.findAll();
        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    public void findByIdTest() throws SQLException {
        CategoryDto daoById = categoryDao.findById(2);
        assertThat(daoById.getName()).isEqualTo("Deli");
        assertThat(daoById.getId()).isEqualTo(2);
    }

    @Test
    public void findAllItemsByCategoryIdTest() throws SQLException {
        List<ItemDto> itemsByCategoryId = categoryDao.findAllItemsByCategoryId(3);

        assertThat(itemsByCategoryId.size()).isEqualTo(2);
        ItemDto item = itemsByCategoryId.get(0);
        assertThat(item.getName()).isEqualTo("Turkey");
        assertThat(item.getPrice()).isEqualTo("6");
    }

    @Test
    public void saveTest() throws SQLException {
        categoryDao.save("Grocery");

        List<CategoryDto> allCategories = categoryDao.findAll();

        assertThat(allCategories.size()).isEqualTo(6);
        assertThat(allCategories.get(allCategories.size() - 1).getName()).isEqualTo("Grocery");
    }

    @Test
    public void deleteTest() throws SQLException {
        categoryDao.delete(5);

        List<CategoryDto> list = categoryDao.findAll();
        CategoryDto daoById = categoryDao.findById(5);

        assertThat(list.size()).isEqualTo(4);
        assertThat(daoById.getName()).isNull();
    }

    @Test
    public void updateTest() throws SQLException {
        categoryDao.update(1, "Seafood");

        CategoryDto daoById = categoryDao.findById(1);

        assertThat(daoById.getName()).isNotEqualTo("Produce");
        assertThat(daoById.getName()).isEqualTo("Seafood");
    }
}
