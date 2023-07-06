package org.example.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConfig {

    private static final Properties PROPERTIES = new Properties();
    private static final String DATABASE_URL;

    static {
        try {
            PROPERTIES.load(new FileReader(getFileFromResource("database.properties")));
            String driverName = (String) PROPERTIES.get("db.driver");
            Class.forName(driverName);
        } catch (ClassNotFoundException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        DATABASE_URL = (String) PROPERTIES.get("db.url");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, PROPERTIES);
    }

    public static File getFileFromResource(final String fileName)
            throws URISyntaxException {
        ClassLoader classLoader = DBConfig.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource != null) {
            return new File(resource.toURI());
        } else {
            throw new URISyntaxException(fileName, ": couldn't be parsed.");
        }
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
