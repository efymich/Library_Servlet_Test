package org.efymich.myapp.servlet;

import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.ReportService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/reports","/admin/reports"})
public class ReportServlet extends HttpServlet {

    private ReportService reportService;

    private TemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        reportService = (ReportService) getServletContext().getAttribute("reportService");
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        List<Report> reports = reportService.getAll();

        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        webContext.setVariable("reports",reports);
        templateEngine.process("admin/reports",webContext,resp.getWriter());
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        HttpSession session = req.getSession();

        Student student = (Student) session.getAttribute("student");
        String bookId = req.getParameter("bookId");

        reportService.create(Long.valueOf(bookId),student);
//        templateEngine.process("books",webContext,resp.getWriter());
        resp.sendRedirect(req.getContextPath() + "/books");
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        reportService.deleteOldRecords();
        resp.sendRedirect(req.getContextPath() + "/reports");
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();

        Student student = (Student) session.getAttribute("student");
        String bookId = req.getParameter("bookId");

        reportService.giveBookBack(Long.valueOf(bookId),student);
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
