package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;
import org.example.model.Category;
import org.example.model.Item;
import org.example.model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    private final DBConfig config;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public ItemDao(DBConfig config) {
        this.config = config;
    }

    public List<ItemDto> findAll() {
        List<ItemDto> itemList = new ArrayList<>();

        Connection connection = config.getConnection();

        try {
            statement = connection.createStatement();
            String sql = "SELECT items.id, items.name, items.price, "
                    + "items.category_id, c.name AS category_name \n"
                    + "FROM items \n"
                    + "LEFT JOIN categories AS c \n"
                    + "ON items.category_id = c.id ORDER BY items.id;";

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ItemDto item = new ItemDto();
                item.setId(resultSet.getInt("id"));
                item.setName((resultSet.getString("name")));
                item.setPrice((resultSet.getString("price")));
                item.setCategory(new CategoryDto(resultSet.getInt("category_id"),
                        resultSet.getString("category_name")));
                itemList.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
        return itemList;
    }

    public Item findById(long id) {
        Item item = new Item();

        Connection connection = config.getConnection();

        try {
            String sqlFindItem = "SELECT items.id, items.name, items.price, "
                    + "items.category_id, c.name AS category_name \n"
                    + "FROM items \n"
                    + "LEFT JOIN categories AS c \n"
                    + "ON items.category_id = c.id \n"
                    + "WHERE items.id = ?;";


            preparedStatement = connection.prepareStatement(sqlFindItem);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setName((resultSet.getString("name")));
                item.setPrice((resultSet.getString("price")));
                item.setCategory(new Category(resultSet.getInt("category_id"),
                        resultSet.getString("category_name")));
            }

            String sqlFindCart = "SELECT carts.id, carts.description \n"
                    + "FROM items_in_cart as ic \n"
                    + "JOIN carts \n"
                    + "ON carts.id = ic.cart_id \n"
                    + "JOIN items\n"
                    + "ON items.id = items_in_cart.item_id\n"
                    + "WHERE items.id = ?;";
            preparedStatement = connection.prepareStatement(sqlFindCart);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            List<Cart> carts = new ArrayList<>();
            while (resultSet.next()) {
                Cart cart = new Cart(resultSet.getLong("id"),
                        resultSet.getString("description"));
                carts.add(cart);
            }

            if (!carts.isEmpty()) {
                item.setCart(carts);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
        return item;
    }

    public void save(String name, String price) {

        Connection connection = config.getConnection();

        try {
            String sql = "INSERT INTO items (name, price) VALUES (?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, price);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
            String sqlUpdate = "DELETE FROM items_in_cart WHERE item_id = ?;";

            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            String sqlDelete = "DELETE FROM items WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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

    public void update(long id, String name, String price) {

        Connection connection = config.getConnection();
        try {
            if (name != null) {
                String sql = "UPDATE items SET name=? WHERE id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setLong(2, id);
                preparedStatement.execute();
            }

            String sql = "UPDATE items SET price=? WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, price);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
