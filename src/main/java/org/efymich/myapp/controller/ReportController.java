package org.efymich.myapp.controller;

import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.entity.Report;
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

@WebServlet(urlPatterns = {"/reports"})
public class ReportController extends HttpServlet {

    private ReportDAO reportDAO;

    private TemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        reportDAO = (ReportDAO) getServletContext().getAttribute("reportDAO");
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        List<Report> reports = reportDAO.getReports();

        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(servletWebExchange);

        webContext.setVariable("reports",reports);
        templateEngine.process("reports",webContext,resp.getWriter());
    }
}
