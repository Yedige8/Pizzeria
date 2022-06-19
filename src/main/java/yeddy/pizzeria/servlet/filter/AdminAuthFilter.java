package yeddy.pizzeria.servlet.filter;

import yeddy.pizzeria.model.dto.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class AdminAuthFilter implements Filter {

  private static final String[] ALLOWED_ROLES = new String[]{"admin"};

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    if (!Arrays.asList(ALLOWED_ROLES).contains(authUser.getRole().getServiceName())) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    filterChain.doFilter(request, response);
  }
}






