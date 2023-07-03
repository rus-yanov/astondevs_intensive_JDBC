package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CartDto;
import org.example.model.Cart;
import org.example.model.Item;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class CartDao {

    private final DBConfig config;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public CartDao(DBConfig config) {
        this.config = config;
    }

    public List<CartDto> getAll() {

        List<CartDto> cartList = new ArrayList<>();

        Connection connection = config.getConnection();

        try {

            statement = connection.createStatement();
            String sql = "SELECT * FROM carts ORDER BY id;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                CartDto cartDto = new CartDto();
                cartDto.setId(resultSet.getInt("id"));
                cartDto.setDescription(resultSet.getString("description"));
                cartList.add(cartDto);
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
                throw new RuntimeException(
                        "SQLException during connection close: " + e.getMessage());
            }
        }
        return cartList;
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

            String sqlFindItem = "SELECT carts.description as cart_description, "
                    + "items.id AS item_id, items.name AS item_name, items.price as price \n"
                    + "FROM carts \n"
                    + "JOIN items_in_cart ON carts.id = items_in_cart.cart_id\n"
                    + "JOIN items ON items.id = items_in_cart.item_id\n"
                    + "WHERE carts.id = ?;";
            preparedStatement = connection.prepareStatement(sqlFindItem);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getLong("item_id"));
                item.setName(resultSet.getString("itwm_name"));
                item.setPrice(resultSet.getInt("price"));

                items.add(item);
            }

            if (!items.isEmpty()) {
                cart.setItemList(items);
            }

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
                throw new RuntimeException(
                        "SQLException during connection close: " + e.getMessage());
            }
        }
        return cart;
    }

    public void save(String description) {

        Connection connection = config.getConnection();

        try {
            String sql = "INSERT INTO carts (description) VALUES (?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, description);
            preparedStatement.execute();
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
                throw new RuntimeException(
                        "SQLException during connection close: " + e.getMessage());
            }
        }
    }

    public void delete(long id) {

        Connection connection = config.getConnection();

        try {
            connection.setAutoCommit(false);
            String sqlUpdate = "DELETE FROM items_in_cart WHERE cart_id = ?;";

            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            String sqlDelete = "DELETE FROM carts WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
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
                throw new RuntimeException(
                        "SQLException during connection close: " + e.getMessage());
            }
        }
    }

    public void update(long id, String description) {

        Connection connection = config.getConnection();

        try {
            String sql = "UPDATE carts SET description=? WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, description);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();

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
                throw new RuntimeException(
                        "SQLException during connection close: " + e.getMessage());
            }
        }
    }
}