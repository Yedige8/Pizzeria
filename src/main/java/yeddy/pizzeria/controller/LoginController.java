package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.dao.impl.UserDaoImpl;
import yeddy.pizzeria.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class LoginController extends BaseController {

  private final Logger logger = LogManager.getLogger(LoginController.class);

  private final UserDao userDao;

  public LoginController() {
    super(RequestMethod.GET, RequestMethod.POST);
    DaoFactory daoFactory = DaoFactory.getInstance();
    userDao = daoFactory.getDao(UserDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/public/login_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    String login = Objects.requireNonNull(request.getParameter("login"));
    String password = Objects.requireNonNull(request.getParameter("password"));
    User user = userDao.findByLogin(login).orElse(null);
    if (user != null) {
      String decodedPassword = new String(Base64.getDecoder().decode(user.getPassword()));
      if (decodedPassword.equals(password)) {
        session.setAttribute("authUser", user);
        logger.info("User with id {} has successfully signed in", user.getId());
        response.sendRedirect(request.getContextPath() + "/main/");
        return;
      }
    }
    session.setAttribute("authError", "Неверный логин или пароль");
    logger.info("User with login {} entered incorrect authorization data", login);
    response.sendRedirect(request.getContextPath() + "/main/login");
  }
}
