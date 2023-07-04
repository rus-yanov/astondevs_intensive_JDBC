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
            List<CategoryDto> list = categoryService.getAll();
            json = objectMapper.writeValueAsString(list);
        } else if (pathVariable != null && pathVariable.equals("items")) {
            List<ItemDto> itemDtoList = categoryService.getByCategoryId(Long.parseLong(id));
            json = objectMapper.writeValueAsString(itemDtoList);
        } else {
            CategoryDto genre = categoryService.getById(Long.parseLong(id));
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
        categoryService.create(categoryDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.sendRedirect("/categories");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        categoryService.delete(Long.parseLong(id));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/categories");
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = Parser.getId(req);
        String name = req.getParameter("name");
        categoryService.update(Long.parseLong(id), name);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/categories/" + id);
    }
}
