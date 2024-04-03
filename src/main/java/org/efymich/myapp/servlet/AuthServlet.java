package org.efymich.myapp.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
import java.util.Set;

@WebServlet(urlPatterns = "/login")
public class AuthServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private AuthService authService;

    private Validator validator;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        authService = (AuthService) getServletContext().getAttribute("authService");
        validator = (Validator) getServletContext().getAttribute("validator");
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(servletWebExchange);

        String studentName = req.getParameter("studentName");
        String password = req.getParameter("password");

        Student inputStudent = Student.builder()
                .studentName(studentName)
                .password(password)
                .build();
        Set<ConstraintViolation<Student>> violations = validator.validate(inputStudent);
        if (violations.isEmpty()) {
            ValidationStudentDTO validationDTO = authService.checkPassword(inputStudent);

            if (validationDTO.isValid()) {
                req.getSession().setAttribute("student",validationDTO.getStudent());
                templateEngine.process("index",webContext, resp.getWriter());
            } else {
                req.setAttribute("errorMessage",validationDTO.getMessage());
                templateEngine.process("login",webContext, resp.getWriter());
            }
        } else {
            req.setAttribute("errorMessage",violations.iterator().next().getMessage());
            templateEngine.process("login",webContext, resp.getWriter());
        }

    }
}
