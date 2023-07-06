package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ItemDto;
import org.example.model.Item;
import org.example.service.ItemService;
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

@WebServlet(name = "ItemServlet", urlPatterns = "/items/*")
public class ItemServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ItemService itemService;

    public ItemServlet(ItemService itemService) {
        this.itemService = itemService;
    }

    public ItemServlet() {
        this.itemService = new ItemService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        String id = Parser.getId(req);
        String json;

        if (id == null) {
            List<ItemDto> list = null;
            try {
                list = itemService.getAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(list);
        } else {
            Item item = null;
            try {
                item = itemService.getById(Long.parseLong(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(item);
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        try {
            itemService.create(name, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.sendRedirect("/items");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        try {
            itemService.delete(Long.parseLong(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/items");
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        try {
            itemService.update(Long.parseLong(id), name, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/items/" + id);
    }
}
