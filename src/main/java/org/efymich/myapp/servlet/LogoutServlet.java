package org.efymich.myapp.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);

        WebContext webContext = new WebContext(servletWebExchange);

        HttpSession session = req.getSession();
        session.removeAttribute("student");
        templateEngine.process("login",webContext,resp.getWriter());
    }
}
