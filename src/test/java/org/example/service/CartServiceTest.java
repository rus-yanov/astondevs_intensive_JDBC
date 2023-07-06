package org.example.service;

import org.example.config.DBConfig;
import org.example.dao.CartDao;
import org.example.dto.CartDto;
import org.example.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CartServiceTest {

    private static CartDao cartDao;
    private static CartService cartService;

    @BeforeEach
    public void beforeAll() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        cartDao = new CartDao(new DBConfig());
        cartService = new CartService(cartDao);
    }

    @Test
    public void getAllTest() throws SQLException {
        List<CartDto> list = cartService.getAll();

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void getByIdTest() throws SQLException {
        Cart cart = cartService.getById(1);

        assertThat(cart.getDescription()).isEqualTo("Home");
    }

    @Test
    public void createTest() throws SQLException {
        cartService.create("For picnic");

        List<CartDto> list = cartDao.findAll();

        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void updateTest() throws SQLException {
        cartService.update(1, "For homeless");

        Cart cart = cartDao.findById(1);
        assertThat(cart.getDescription()).isEqualTo("For homeless");
    }

    @Test
    public void deleteTest() throws SQLException {
        cartService.delete(1);

        List<CartDto> list = cartDao.findAll();
        assertThat(list.size()).isEqualTo(1);
    }
}
