package org.efymich.myapp.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.service.BookService;
import org.efymich.myapp.utils.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.*;

@WebServlet(urlPatterns = {"/books/*", "/admin/books/*"})
public class BookServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private BookService bookService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        Integer page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        String servletPath = req.getServletPath();
        String sortParameter = req.getParameter("sort");
        Set<String> columnNames = bookService.getColumnNames(Book.class);

        Map<String, Object> paginationMap = Util.calculatePagination(bookService.getAllCount(),page);
        req.getSession().setAttribute("sort", sortParameter);
        webContext.setVariables(paginationMap);
        webContext.setVariable("columnNames", columnNames);

        if (servletPath.contains("admin")) {
            List<Book> books = bookService.getAll(page, sortParameter);
            webContext.setVariable("books", books);
            templateEngine.process("admin/books", webContext, resp.getWriter());
        } else {
            List<Book> freeBooks = bookService.getFreeBooks(page, sortParameter);
            webContext.setVariable("freeBooks", freeBooks);
            templateEngine.process("books", webContext, resp.getWriter());
        }
    }
}
