package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutController extends BaseController {

  private final Logger logger = LogManager.getLogger(LogoutController.class);

  public LogoutController() {
    super(RequestMethod.GET);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    session.removeAttribute("authUser");
    logger.info("User with id {} logged out", authUser.getId());
    response.sendRedirect(request.getContextPath() + "/main/");
  }
}
