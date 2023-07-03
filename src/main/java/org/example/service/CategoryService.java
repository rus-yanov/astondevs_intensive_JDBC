package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CategoryDao;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;

import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao;

    public CategoryService() {
        this.categoryDao = new CategoryDao(new DBConfig());
    }

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryDto> getAll() {
        return categoryDao.findAll();
    }

    public List<ItemDto> getByCategoryId(long id) {
        return categoryDao.findAllItemsByCategoryId(id);
    }

    public CategoryDto getById(long id) {
        return categoryDao.findById(id);
    }

    public void create(CategoryDto genre) {
        categoryDao.save(genre.getName());
    }

    public void update(long id, String name) {
        categoryDao.update(id, name);
    }

    public void delete(long id) {
        categoryDao.delete(id);
    }
}
