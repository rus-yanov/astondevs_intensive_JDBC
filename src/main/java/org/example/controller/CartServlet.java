package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CartDto;
import org.example.model.Cart;
import org.example.service.CartService;
import org.example.config.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = "/carts/*")
public class CartServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CartService cartService;

    public CartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public CartServlet() {
        this.cartService = new CartService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        String id = Parser.getId(req);
        String json = null;

        if (id == null) {
            List<CartDto> list = null;
            try {
                list = cartService.getAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(list);
        } else {
            Cart cart = null;
            try {
                cart = cartService.getById(Long.parseLong(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(cart);
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(json);
        resp.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String description = req.getParameter("description");
        try {
            cartService.create(description);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.sendRedirect("/carts");
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        String newDesc = req.getParameter("description");
        try {
            cartService.update(Long.parseLong(id), newDesc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/carts/" + id);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        try {
            cartService.delete(Long.parseLong(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/carts");
    }
}
