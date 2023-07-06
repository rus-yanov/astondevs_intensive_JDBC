package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CartDto;
import org.example.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CartDaoTest {

    private static CartDao cartDao;

    @BeforeEach
    public void beforeAll() throws SQLException, IOException {
        Connection connection = DBConfig.getConnection();
        DBConfig.initForTest(connection);
        cartDao = new CartDao(new DBConfig());
    }

    @Test
    public void findAllTest() throws SQLException {
        List<CartDto> list = cartDao.findAll();

        CartDto cartDto = list.get(0);

        assertThat(list.size()).isEqualTo(2);
        assertThat(cartDto.getId()).isEqualTo(1);
        assertThat(cartDto.getDescription()).isEqualTo("Home");
    }

    @Test
    public void findByIdTest() throws SQLException {
        Cart cart = cartDao.findById(2);
        assertThat(cart.getDescription()).isEqualTo("For Anton birthday party");
    }

    @Test
    public void saveTest() throws SQLException {
        cartDao.save("For picnic");

        List<CartDto> list = cartDao.findAll();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void deleteTest() throws SQLException {
        cartDao.delete(1);

        List<CartDto> list = cartDao.findAll();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void updateTest() throws SQLException {
        cartDao.update(1, "For homeless people");

        Cart cart = cartDao.findById(1);
        assertThat(cart.getDescription()).isNotEqualTo("Home");
        assertThat(cart.getDescription()).isEqualTo("For homeless people");
    }
}
