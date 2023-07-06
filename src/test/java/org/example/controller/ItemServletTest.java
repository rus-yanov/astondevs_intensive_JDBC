package org.example.controller;

import org.example.config.DBConfig;
import org.example.controller.ItemServlet;
import org.example.dao.ItemDao;
import org.example.service.ItemService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ItemServletTest {

    private ItemServlet itemServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private StringWriter stringWriter;


    @BeforeEach
    public void beforeEach() {
        ItemDao itemDao = new ItemDao(new DBConfig());
        ItemService itemService = new ItemService(itemDao);
        itemServlet = new ItemServlet(itemService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void getAllItemsTest() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        itemServlet.doGet(request, response);

        verify(request, times(1)).getPathInfo();
        verify(response, times(1)).getWriter();
        assertThat(stringWriter.toString()).contains("Milk");
    }

    @Test
    public void getByIdTest() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("items/7/");
        when(response.getWriter()).thenReturn(printWriter);
        itemServlet.doGet(request, response);

        verify(request, times(1)).getPathInfo();
        verify(response, times(1)).getWriter();
        assertThat(stringWriter.toString()).contains("Turkey");
    }

    @Test
    public void createTest() throws IOException, ServletException {
        when(request.getParameter("name")).thenReturn("Beer");
        when(request.getParameter("price")).thenReturn("9");
        itemServlet.doPost(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        itemServlet.doGet(request, response);
        assertThat(stringWriter.toString()).contains("9");
        assertThat(stringWriter.toString()).contains("Beer");
    }

    @Test
    public void deleteTest() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("items/4/");
        itemServlet.doDelete(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        itemServlet.doGet(request, response);
        assertThat(stringWriter.toString()).doesNotContain("Tuna salad");
    }

    @Test
    public void updateTest() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Coconut");
        when(request.getParameter("price")).thenReturn("9");
        when(request.getPathInfo()).thenReturn("items/12/");
        itemServlet.doPut(request, response);

        when(request.getPathInfo()).thenReturn("items/12/");
        when(response.getWriter()).thenReturn(printWriter);
        itemServlet.doGet(request, response);
        assertThat(stringWriter.toString()).contains("Coconut");
    }
}
