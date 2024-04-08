package org.efymich.myapp.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.dto.ValidationStudentDTO;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.AuthService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class AuthServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private AuthService authService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        templateEngine.process("login", webContext, resp.getWriter());
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(servletWebExchange);

        String studentName = req.getParameter("studentName");
        String password = req.getParameter("password");

        Student inputStudent = Student.builder()
                .studentName(studentName)
                .password(password)
                .build();

        ValidationStudentDTO validationDTO = authService.checkPassword(inputStudent);

        if (validationDTO.isValid()) {
            req.getSession().setAttribute("student", validationDTO.getStudent());
            templateEngine.process("index", webContext, resp.getWriter());
        } else {
            req.setAttribute("errorMessage", validationDTO.getMessage());
            templateEngine.process("login", webContext, resp.getWriter());
        }
    }
}
