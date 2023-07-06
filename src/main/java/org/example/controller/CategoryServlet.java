package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CategoryDto;
import org.example.dto.ItemDto;
import org.example.service.CategoryService;
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

@WebServlet(name = "CategoryServlet", urlPatterns = "/categories/*")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CategoryServlet(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryServlet() {
        this.categoryService = new CategoryService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        String id = Parser.getId(req);
        String pathVariable = Parser.getPathVariable(req);

        String json;
        if (id == null) {
            List<CategoryDto> list = null;
            try {
                list = categoryService.getAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(list);
        } else if (pathVariable != null && pathVariable.equals("items")) {
            List<ItemDto> itemDtoList = null;
            try {
                itemDtoList = categoryService.getByCategoryId(Long.parseLong(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(itemDtoList);
        } else {
            CategoryDto genre = null;
            try {
                genre = categoryService.getById(Long.parseLong(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            json = objectMapper.writeValueAsString(genre);
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        try {
            categoryService.create(categoryDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.sendRedirect("/categories");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        try {
            categoryService.delete(Long.parseLong(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/categories");
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        String name = req.getParameter("name");
        try {
            categoryService.update(Long.parseLong(id), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/categories/" + id);
    }
}
