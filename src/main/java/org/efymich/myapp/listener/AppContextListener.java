package org.efymich.myapp.listener;

import lombok.SneakyThrows;
import org.efymich.myapp.config.HibernateConfig;
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

        context.setAttribute("sessionFactory",sessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
