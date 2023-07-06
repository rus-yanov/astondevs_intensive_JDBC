package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CategoryDao;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao;

    public CategoryService() {
        this.categoryDao = new CategoryDao(new DBConfig());
    }

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryDto> getAll() throws SQLException {
        return categoryDao.findAll();
    }

    public List<ItemDto> getByCategoryId(long id) throws SQLException {
        return categoryDao.findAllItemsByCategoryId(id);
    }

    public CategoryDto getById(long id) throws SQLException {
        return categoryDao.findById(id);
    }

    public void create(CategoryDto genre) throws SQLException {
        categoryDao.save(genre.getName());
    }

    public void update(long id, String name) throws SQLException {
        categoryDao.update(id, name);
    }

    public void delete(long id) throws SQLException {
        categoryDao.delete(id);
    }
}
