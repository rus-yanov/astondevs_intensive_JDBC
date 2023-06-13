package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CartDto;
import org.example.model.Cart;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class CartDao {

    private DBConfig config;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public CartDao(DBConfig config) {
        this.config = config;
    }

    public List<CartDto> getAll() {

        List<CartDto> list = new ArrayList<>();

        Connection connection = config.getConnection();

        try {

            statement = connection.createStatement();
            String sql = "SELECT * FROM carts ORDER BY id;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                CartDto cartDto = new CartDto();
                cartDto.setId(resultSet.getInt("id"));
                cartDto.setDescription(resultSet.getString("description"));
                list.add(cartDto);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    DBConfig.closeConnection(connection);
                }
            } catch (SQLException e) {
                System.out.println("SQLException");
            }
        }
        return list;
    }

    public Cart getById(long id) {
        Cart cart = new Cart();

        Connection connection = config.getConnection();

        try {

            String sqlFindPCart = "SELECT * FROM carts WHERE id = ?;";


            preparedStatement = connection.prepareStatement(sqlFindPCart);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cart = new Cart();
                cart.setId(resultSet.getInt("id"));
                cart.setDescription((resultSet.getString("description")));
            }



    public void save(String name) {

        Connection connection = config.getConnection();

        try {

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DBConfig.closeConnection(connection);
                }
            } catch (SQLException e) {
                System.out.println("SQLException during close(): " + e.getMessage());
            }
        }
    }

    public void delete(long id) {

        Connection connection = config.getConnection();

        try {


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }

    public void update(long id, String name) {

        Connection connection = config.getConnection();

        try {


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }
}