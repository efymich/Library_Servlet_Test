package org.efymich.myapp.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.service.StudentService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.List;

@WebServlet(urlPatterns = {"/students"})
public class StudentController extends HttpServlet {
    private TemplateEngine templateEngine;

    private StudentService studentService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        studentService = (StudentService) getServletContext().getAttribute("studentService");
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Student> students = studentService.getAll();

        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(servletWebExchange);

        webContext.setVariable("students",students);
        templateEngine.process("students",webContext,resp.getWriter());
    }

    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        String studentName = req.getParameter("studentName");
        String studentMajor = req.getParameter("studentMajor");
        Student student = Student.builder()
                .studentName(studentName)
                .major(studentMajor).build();

        studentService.create(student);

        resp.sendRedirect(req.getContextPath() + "/students");
    }
}
