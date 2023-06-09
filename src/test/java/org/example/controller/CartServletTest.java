package org.example.controller;

import org.example.config.DBConfig;
import org.example.dao.CartDao;
import org.example.service.CartService;

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
import static org.mockito.Mockito.times;

public class CartServletTest {

    private CartServlet cartServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void beforeEach() {
        CartDao cartDao = new CartDao(new DBConfig());
        CartService cartService = new CartService(cartDao);
        cartServlet = new CartServlet(cartService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void getAllCartsTest() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        cartServlet.doGet(request, response);

        verify(request, times(1)).getPathInfo();
        verify(response, times(1)).getWriter();
        assertThat(stringWriter.toString()).contains("For Anton birthday party");
    }

    @Test
    public void getByIdTest() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("carts/1/");
        when(response.getWriter()).thenReturn(printWriter);
        cartServlet.doGet(request, response);

        assertThat(stringWriter.toString()).contains("Home");
    }

    @Test
    public void createTest() throws ServletException, IOException {
        when(request.getParameter("description")).thenReturn("For picnic");
        cartServlet.doPost(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        cartServlet.doGet(request, response);

        assertThat(stringWriter.toString()).contains("For picnic");
    }

    @Test
    public void updateTest() throws ServletException, IOException {
        when(request.getParameter("description")).thenReturn("Stuff not needed");
        when(request.getPathInfo()).thenReturn("carts/2/");
        cartServlet.doPut(request, response);

        when(request.getPathInfo()).thenReturn("carts/2/");
        when(response.getWriter()).thenReturn(printWriter);
        cartServlet.doGet(request, response);
        assertThat(stringWriter.toString()).doesNotContain("For Anton birthday party");
        assertThat(stringWriter.toString()).contains("Stuff not needed");
    }

    @Test
    public void deleteTest() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("carts/1/");
        cartServlet.doDelete(request, response);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        cartServlet.doGet(request, response);
        assertThat(stringWriter.toString()).doesNotContain("Home");
    }
}
