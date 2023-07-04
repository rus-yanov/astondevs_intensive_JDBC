package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.ItemDao;
import org.example.dto.ItemDto;
import org.example.model.Item;

import java.util.List;

public class ItemService {

    private final ItemDao itemDao;

    public ItemService() {
        this.itemDao = new ItemDao(new DBConfig());
    }

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<ItemDto> getAll() {
        return itemDao.findAll();
    }

    public Item getById(long id) {
        return itemDao.findById(id);
    }

    public void create(String name, String price) {
        itemDao.save(name, price);
    }

    public void update(long id, String name, String price) {
        itemDao.update(id, name, price);
    }

    public void delete(long id) {
        itemDao.delete(id);
    }
}
