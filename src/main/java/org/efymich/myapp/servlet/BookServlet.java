package org.efymich.myapp.servlet;

import jakarta.persistence.criteria.CriteriaBuilder;
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
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.BookService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.*;

@WebServlet(urlPatterns = {"/books/*", "/admin/books/*"})
public class BookServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    private BookService bookService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        HttpSession session = req.getSession();
        Integer page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1 ;

        Map<String, Object> calculatePagination = calculatePagination(page);
        webContext.setVariables(calculatePagination);
        String servletPath = req.getServletPath();
        String sortParameter = req.getParameter("sort");
        session.setAttribute("sort",sortParameter);
        Set<String> columnNames = bookService.getColumnNames(Book.class);
        webContext.setVariable("columnNames", columnNames);

        if (servletPath.contains("admin")) {
            List<Book> books = bookService.getAll(sortParameter,page);
            webContext.setVariable("books", books);
            templateEngine.process("admin/books", webContext, resp.getWriter());
        } else {
            List<Book> freeBooks = bookService.getFreeBooks(sortParameter,page);
            webContext.setVariable("freeBooks", freeBooks);
            templateEngine.process("books", webContext, resp.getWriter());
        }
    }

    private Map<String,Object> calculatePagination(Integer currentPage){
        Integer countOfPages = bookService.getCountOfPages();
        Integer current = currentPage + 3;
        Integer begin = Math.max(1,current - 5);
        Integer end = Math.min(begin + 6,countOfPages);
        return Map.of("beginIndex",begin,"endIndex",end);
    }
}
