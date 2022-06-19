package yeddy.pizzeria.servlet.filter;

import yeddy.pizzeria.Global;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocaleFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpSession session = request.getSession();
    if (session.getAttribute("currentLocale") == null) {
      session.setAttribute("currentLocale", Global.DEFAULT_LOCALE.getLanguage());
    }
    filterChain.doFilter(request, response);
  }
}
