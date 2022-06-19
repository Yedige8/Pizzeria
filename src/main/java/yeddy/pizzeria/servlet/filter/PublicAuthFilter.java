package yeddy.pizzeria.servlet.filter;

import yeddy.pizzeria.model.dto.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class PublicAuthFilter implements Filter {

  private static final String[] AUTH_REQUIRED_URI_PATTERNS = new String[]{
      "^/create_review$",
      "^/profile$",
      "^/cart$",
      "^/create_order$",
      "^/admin.*$"
  };

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String pathInfo = request.getPathInfo();
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    if (authUser == null) {
      boolean match = Arrays
          .stream(AUTH_REQUIRED_URI_PATTERNS)
          .anyMatch(pathInfo::matches);
      if (match) {
        response.sendRedirect(request.getContextPath() + "/main/login");
        return;
      }
    } else {
      if (pathInfo.equals("/login") || pathInfo.equals("/registration")) {
        response.sendRedirect(request.getContextPath() + "/main/profile");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
