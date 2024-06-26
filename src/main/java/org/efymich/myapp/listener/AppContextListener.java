package org.efymich.myapp.listener;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import org.efymich.myapp.config.HibernateConfig;
import org.efymich.myapp.config.LiquibaseConfig;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.dao.AuthorDAO;
import org.efymich.myapp.dao.BookDAO;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.service.*;
import org.hibernate.SessionFactory;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    @SneakyThrows
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(context);
        ThymeleafConfiguration thymeleafConfiguration = new ThymeleafConfiguration();
        ITemplateEngine templateEngine = thymeleafConfiguration.getTemplateEngine(application);

        LiquibaseConfig liquibaseConfig = new LiquibaseConfig();
        Liquibase liquibase = new Liquibase(LiquibaseConfig.CHANGELOGFILE, new ClassLoaderResourceAccessor(), new JdbcConnection(liquibaseConfig.getConnection()));
        liquibase.update(new Contexts(), new LabelExpression());

        // Config beans
        SessionFactory sessionFactory = new HibernateConfig().buildSessionFactory();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // DAO beans
        StudentDAO studentDAO = new StudentDAO(sessionFactory);
        AuthorDAO authorDAO = new AuthorDAO(sessionFactory);
        BookDAO bookDAO = new BookDAO(sessionFactory);
        ReportDAO reportDAO = new ReportDAO(sessionFactory);

        // Service beans
        StudentService studentService = new StudentService(studentDAO);
        BookService bookService = new BookService(bookDAO,reportDAO);
        AuthService authService = new AuthService(studentDAO);
        ReportService reportService = new ReportService(reportDAO,bookDAO);

        // Setting attributes
        context.setAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR,templateEngine);
        context.setAttribute("sessionFactory",sessionFactory);
        context.setAttribute("validator",validator);

        context.setAttribute("reportService",reportService);
        context.setAttribute("bookService",bookService);
        context.setAttribute("studentService",studentService);
        context.setAttribute("authService",authService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SessionFactory sessionFactory = (SessionFactory) servletContextEvent
                .getServletContext()
                .getAttribute("sessionFactory");
        sessionFactory.close();
    }
}
