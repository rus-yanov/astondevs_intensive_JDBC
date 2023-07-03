package org.example.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfig {

    private String driver;

    private String url;

    private String username;

    private String password;

    public DBConfig() {
        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/supermarket";
        this.username = "rustam";
        this.password = "1234";
    }

    public DBConfig(String test) {
        if (test.equals("test")) {
            this.driver = "org.h2.Driver";
            this.url = "jdbc:h2:./database";
            this.username = "sa";
            this.password = "sa";
        }
    }

    public final Connection getConnection() {
        Connection connection = null;
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getFileContent(String fileName) throws IOException {
        Path pathToSolution = Paths.get(fileName).toAbsolutePath();
        return Files.readString(pathToSolution).trim();
    }

    public static void initForTest(Connection connection) throws SQLException, IOException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            String initSql = getFileContent("src/test/resources/seed.sql");
            statement.execute(initSql);
        }
    }
}
