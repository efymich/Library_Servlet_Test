package org.efymich.myapp.listener;

import lombok.SneakyThrows;
import org.efymich.myapp.config.HibernateConfig;
import org.efymich.myapp.dao.AuthorDAO;
import org.efymich.myapp.dao.BookDAO;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.dao.StudentDAO;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    @SneakyThrows
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        SessionFactory sessionFactory = new HibernateConfig().buildSessionFactory();
        StudentDAO studentDAO = new StudentDAO(sessionFactory);
        AuthorDAO authorDAO = new AuthorDAO(sessionFactory);
        BookDAO bookDAO = new BookDAO(sessionFactory);
        ReportDAO reportDAO = new ReportDAO(sessionFactory);

        context.setAttribute("sessionFactory",sessionFactory);
        context.setAttribute("studentDAO",studentDAO);
        context.setAttribute("authorDAO",authorDAO);
        context.setAttribute("bookDAO",bookDAO);
        context.setAttribute("reportDAO",reportDAO);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
