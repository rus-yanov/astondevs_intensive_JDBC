package org.example;

import org.example.config.DBConfig;
import org.example.controller.CategoryServlet;
import org.example.dao.CategoryDao;
import org.example.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CategoryServletTest {

    private CategoryServlet categoryServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void beforeEach() throws SQLException, IOException{
        Connection connection = new DBConfig("test").getConnection();
        DBConfig.initForTest(connection);
        CategoryDao categoryDao = new CategoryDao(new DBConfig("test"));
        CategoryService categoryService = new CategoryService(categoryDao);
        categoryServlet = new CategoryServlet(categoryService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void getAllCategories() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);

        verify(request, times(2)).getPathInfo();
        verify(response, times(1)).getWriter();
        assertThat(stringWriter.toString()).contains("Deli");
    }

    @Test
    public void getById() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("categories/1/");
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);

        assertThat(stringWriter.toString()).contains("Produce");
    }

    @Test
    public void getItemById() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("categories/2/item");
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);

        assertThat(stringWriter.toString()).contains("Pepperoni pizza");
        assertThat(stringWriter.toString()).contains("Sub sandwich");
    }

    @Test
    public void create() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Cosmetics");
        categoryServlet.doPost(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);
        assertThat(stringWriter.toString()).contains("Cosmetics");
    }

    @Test
    public void update() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Electronics");
        when(request.getPathInfo()).thenReturn("categories/5/");
        categoryServlet.doPut(request, response);

        when(request.getPathInfo()).thenReturn("categories/5/");
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);
        assertThat(stringWriter.toString()).contains("Electronics");
    }

    @Test
    public void delete() throws ServletException, IOException {

        when(request.getPathInfo()).thenReturn("categories/4/");
        categoryServlet.doDelete(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        categoryServlet.doGet(request, response);
        assertThat(stringWriter.toString()).doesNotContain("Beer&Wine");
    }
}
