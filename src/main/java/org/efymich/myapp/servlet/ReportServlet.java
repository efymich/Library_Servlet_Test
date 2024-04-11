package org.efymich.myapp.servlet;

import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.ReportService;
import org.efymich.myapp.utils.Util;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = {"/reports/*","/admin/reports/*"})
public class ReportServlet extends HttpServlet {
    private ReportService reportService;
    private TemplateEngine templateEngine;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        reportService = (ReportService) getServletContext().getAttribute("reportService");
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        Integer page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        String sortParameter = req.getParameter("sort");
        Set<String> columnNames = reportService.getColumnNames(Report.class);

        Map<String, Object> paginationMap = Util.calculatePagination(reportService.getAllCount(),page);
        req.getSession().setAttribute("sort", sortParameter);
        webContext.setVariables(paginationMap);
        webContext.setVariable("columnNames", columnNames);

        List<Report> reports = reportService.getAll(page,sortParameter);
        webContext.setVariable("reports",reports);
        templateEngine.process("admin/reports",webContext,resp.getWriter());
    }

    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        Student student = (Student) req.getSession().getAttribute("student");
        String bookId = req.getParameter("bookId");

        reportService.create(Long.valueOf(bookId),student);
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
        Student student = (Student) req.getSession().getAttribute("student");
        String bookId = req.getParameter("bookId");

        reportService.giveBookBack(Long.valueOf(bookId),student);
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
