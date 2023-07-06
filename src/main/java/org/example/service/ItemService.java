package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.ItemDao;
import org.example.dto.ItemDto;
import org.example.model.Item;

import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private final ItemDao itemDao;

    public ItemService() {
        this.itemDao = new ItemDao(new DBConfig());
    }

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<ItemDto> getAll() throws SQLException {
        return itemDao.findAll();
    }

    public Item getById(long id) throws SQLException {
        return itemDao.findById(id);
    }

    public void create(String name, String price) throws SQLException {
        itemDao.save(name, price);
    }

    public void update(long id, String name, String price) throws SQLException {
        itemDao.update(id, name, price);
    }

    public void delete(long id) throws SQLException {
        itemDao.delete(id);
    }
}
