package org.efymich.myapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.util.Locale;

@WebFilter(filterName = "HiddenHttpMethodFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST})
public class HiddenHttpMethodFilter implements Filter {

    public static final String DEFAULT_METHOD_PARAM = "_method";

    private String methodParam = DEFAULT_METHOD_PARAM;

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String paramValue = request.getParameter(this.methodParam);

        if ("POST".equals(request.getMethod()) && paramValue != null && !paramValue.trim().isEmpty()) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            HttpServletRequest wrapper = new HttpMethodRequestWrapper(request, method);
            filterChain.doFilter(wrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }

}