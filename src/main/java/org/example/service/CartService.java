package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CartDao;
import org.example.dto.CartDto;
import org.example.model.Cart;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDao cartDao;

    public CartService() {
        this.cartDao = new CartDao(new DBConfig());
    }

    public CartService(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public List<CartDto> getAll() throws SQLException {
        return cartDao.findAll();
    }

    public Cart getById(long id) throws SQLException {
        return cartDao.findById(id);
    }

    public void create(String name) throws SQLException {
        cartDao.save(name);
    }

    public void update(long id, String name) throws SQLException {
        cartDao.update(id, name);
    }

    public void delete(long id) throws SQLException {
        cartDao.delete(id);
    }
}
