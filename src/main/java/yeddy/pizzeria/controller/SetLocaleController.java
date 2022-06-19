package yeddy.pizzeria.controller;

import yeddy.pizzeria.Global;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class SetLocaleController extends BaseController {

  public SetLocaleController() {
    super(RequestMethod.GET);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String locale = Objects.requireNonNull(request.getParameter("locale"));
    boolean match = Arrays
        .stream(Global.SUPPORTED_LOCALES)
        .anyMatch(supportedLocale -> supportedLocale.getLanguage().equals(locale));
    if (match) {
      session.setAttribute("currentLocale", locale);
      response.sendRedirect(request.getHeader("Referer"));
    } else {
      throw new RuntimeException("Locale not supported");
    }
  }
}
