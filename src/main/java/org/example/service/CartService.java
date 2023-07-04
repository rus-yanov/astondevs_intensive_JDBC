package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CartDao;
import org.example.dto.CartDto;
import org.example.model.Cart;

import java.util.List;

public class CartService {

    private final CartDao cartDao;

    public CartService() {
        this.cartDao = new CartDao(new DBConfig());
    }

    public CartService(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public List<CartDto> getAll() {
        return cartDao.getAll();
    }

    public Cart getById(long id) {
        return cartDao.getById(id);
    }

    public void create(String name) {
        cartDao.save(name);
    }

    public void update(long id, String name) {
        cartDao.update(id, name);
    }

    public void delete(long id) {
        cartDao.delete(id);
    }
}
