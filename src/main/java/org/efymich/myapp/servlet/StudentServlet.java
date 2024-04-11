package org.efymich.myapp.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.enums.Roles;
import org.efymich.myapp.service.StudentService;
import org.efymich.myapp.utils.PasswordUtils;
import org.efymich.myapp.utils.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = {"/students/*", "/admin/students/*"})
public class StudentServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private StudentService studentService;
    private Validator validator;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        studentService = (StudentService) getServletContext().getAttribute("studentService");
        validator = (Validator) getServletContext().getAttribute("validator");
    }

    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        String pathInfo = req.getPathInfo();
        String servletPath = req.getServletPath();

        Integer page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        String sortParameter = req.getParameter("sort");
        Set<String> columnNames = studentService.getColumnNames(Student.class);

        Map<String, Object> paginationMap = Util.calculatePagination(studentService.getAllCount(),page);
        req.getSession().setAttribute("sort", sortParameter);
        webContext.setVariables(paginationMap);
        webContext.setVariable("columnNames", columnNames);

        if (servletPath.contains("admin")) {
            List<Student> students = studentService.getAll(page,sortParameter);

            webContext.setVariable("students", students);
            templateEngine.process("admin/students", webContext, resp.getWriter());
        } else if (servletPath.equals("/students") && pathInfo.equals("/new")) {
            templateEngine.process("new", webContext, resp.getWriter());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(servletWebExchange);

        String studentName = req.getParameter("studentName");
        String password = req.getParameter("password");
        Student inputStudent = Student.builder()
                .studentName(studentName)
                .password(password)
                .role(Roles.USER)
                .build();
        Set<ConstraintViolation<Student>> violations = validator.validate(inputStudent);

        if (violations.isEmpty()) {
            inputStudent.setPassword(PasswordUtils.hashPassword(password));
            studentService.create(inputStudent);
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("errorMessage", violations.iterator().next().getMessage());
            templateEngine.process("new",webContext, resp.getWriter());
        }
    }
}
