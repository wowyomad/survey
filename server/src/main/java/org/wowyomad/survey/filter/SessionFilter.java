package org.wowyomad.survey.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();

        if (uri.endsWith(".html")) {
            httpResponse.sendRedirect("/error");
            System.out.printf("Uri ends with .html (%s)%n", httpRequest.getRequestURL());
            Boolean redirected = (Boolean) httpRequest.getSession().getAttribute("redirectedFromTest");
            System.out.println("redirected = " + redirected);
            if (redirected != null && redirected) {
                httpRequest.getSession().removeAttribute("redirectedFromTest");
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect("/error");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
