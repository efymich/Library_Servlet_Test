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
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.BookService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = {"/books", "/admin/books"})
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
        String servletPath = req.getServletPath();
        String sortParameter = req.getParameter("sort");

        if (servletPath.contains("admin")) {

            Set<String> columnNames = bookService.getColumnNames(Book.class);
            List<Book> books = bookService.getAll(sortParameter);

            webContext.setVariable("books", books);
            webContext.setVariable("columnNames", columnNames);
            templateEngine.process("admin/books", webContext, resp.getWriter());
        } else {
            List<Book> freeBooks = bookService.getFreeBooks();
            webContext.setVariable("freeBooks", freeBooks);
//            webContext.setVariable("columnNames",columnNames);
            templateEngine.process("books", webContext, resp.getWriter());
        }

    }
}
