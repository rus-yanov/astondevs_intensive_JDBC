package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CategoryDao;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoryServiceTest {

    private static CategoryDao categoryDao;

    private static CategoryService categoryService;

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        categoryDao = new CategoryDao(new DBConfig());
        categoryService = new CategoryService(categoryDao);
    }

    @Test
    public void getAllTest() throws SQLException {
        List<CategoryDto> list = categoryService.getAll();

        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    public void getByIdTest() throws SQLException {
        CategoryDto byId = categoryService.getById(5);

        assertThat(byId.getName()).isEqualTo("Dairy");
    }

    @Test
    public void getByCategoryIdTest() throws SQLException {
        List<ItemDto> genreId = categoryService.getByCategoryId(1);

        ItemDto item = genreId.get(0);

        assertThat(genreId.size()).isEqualTo(3);
        assertThat(item.getName()).isEqualTo("Mellon");
        assertThat(item.getPrice()).isEqualTo("4");
    }

    @Test
    public void createTest() throws SQLException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Health&beauty");
        categoryService.create(categoryDto);

        List<CategoryDto> list = categoryDao.findAll();

        assertThat(list.size()).isEqualTo(6);
        assertThat(list.get(5).getName()).isEqualTo("Health&beauty");
    }

    @Test
    public void deleteTest() throws SQLException {
        categoryService.delete(3);

        List<CategoryDto> list = categoryDao.findAll();

        assertThat(list.size()).isEqualTo(4);
    }
    @Test
    public void updateTest() throws SQLException {
        categoryService.update(1, "Seafood");

        CategoryDto daoById = categoryDao.findById(1);

        assertThat(daoById.getName()).isEqualTo("Seafood");
    }
}
