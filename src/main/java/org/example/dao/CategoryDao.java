package org.example.dao;

import org.example.config.DBConfig;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private final DBConfig config;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public CategoryDao(DBConfig config) {
        this.config = config;
    }

    public List<CategoryDto> findAll() {
        List<CategoryDto> categoryList = new ArrayList<>();

        Connection connection = config.getConnection();

        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM categories ORDER BY id;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(resultSet.getInt("id"));
                categoryDto.setName(resultSet.getString("name"));
                categoryList.add(categoryDto);
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
        return categoryList;
    }

    public CategoryDto findById(long id) {

        CategoryDto categoryDto = new CategoryDto();

        Connection connection = config.getConnection();

        try {

            String sql = "SELECT * FROM categories WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categoryDto.setId(resultSet.getInt("id"));
                categoryDto.setName(resultSet.getString("name"));
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
        return categoryDto;
    }

    public List<ItemDto> findAllItemsByCategoryId(long id) {
        List<ItemDto> itemList = new ArrayList<>();

        Connection connection = config.getConnection();

        try {

            String sql = "SELECT items.id AS item_id, items.name AS item_name, items.price AS price\n"
                    + "FROM categories AS c \n"
                    + "JOIN items ON c.id = items.category_id \n"
                    + "WHERE c.id = ?;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ItemDto itemDto = new ItemDto();
                itemDto.setId(resultSet.getInt("item_id"));
                itemDto.setName(resultSet.getString("item_name"));
                itemDto.setPrice(resultSet.getString("price"));
                itemList.add(itemDto);
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
        return itemList;
    }

    public void save(String name) {

        Connection connection = config.getConnection();

        try {

            String sql = "INSERT INTO categories (name) VALUES (?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
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
            String sqlUpdate = "UPDATE items SET category_id = NULL WHERE category_id = ?;";

            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            String sqlDelete = "DELETE FROM categories WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
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

    public void update(long id, String name) {

        Connection connection = config.getConnection();

        try {
            String sql = "UPDATE categories SET name=? WHERE id = ?;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
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
