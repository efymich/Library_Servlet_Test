package org.efymich.myapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.efymich.myapp.config.ThymeleafConfiguration;
import org.efymich.myapp.entity.Student;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebFilter(urlPatterns = "/")
public class AuthFilter implements Filter {

    private TemplateEngine templateEngine;

    @Override
    public void init(FilterConfig filterConfig){
        templateEngine = (TemplateEngine) filterConfig.getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain){

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        IServletWebExchange servletWebExchange = JakartaServletWebApplication.buildApplication(httpServletRequest.getServletContext()).buildExchange(httpServletRequest, httpServletResponse);
        WebContext webContext = new WebContext(servletWebExchange);

        Student student = (Student) httpServletRequest.getSession().getAttribute("student");
        if (student != null) {
            chain.doFilter(req,resp);
        } else {
            templateEngine.process("login",webContext,httpServletResponse.getWriter());
        }
    }
}
