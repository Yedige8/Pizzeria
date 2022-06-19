package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.RoleDao;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.validator.UserValidator;
import yeddy.pizzeria.validator.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class RegistrationController extends BaseController {

  private final Logger logger = LogManager.getLogger(RegistrationController.class);

  private final Validator<User> validator;

  private final RoleDao roleDao;

  private final UserDao userDao;

  public RegistrationController() {
    super(RequestMethod.GET, RequestMethod.POST);
    validator = new UserValidator();
    DaoFactory daoFactory = DaoFactory.getInstance();
    roleDao = daoFactory.getDao(RoleDao.class);
    userDao = daoFactory.getDao(UserDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/public/registration_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String login = Objects.requireNonNull(request.getParameter("login"));
    String password = Objects.requireNonNull(request.getParameter("password"));
    String email = Objects.requireNonNull(request.getParameter("email"));
    String phone = Objects.requireNonNull(request.getParameter("phone"));
    String firstName = Objects.requireNonNull(request.getParameter("firstName"));
    String lastName = Objects.requireNonNull(request.getParameter("lastName"));
    User user = new User();
    user.setRole(roleDao.findByServiceName("user").orElseThrow(() -> {
      logger.error("Role with service name `user` wasn't found");
      throw new RuntimeException("Role not found");
    }));
    user.setLogin(login);
    user.setPassword(password);
    user.setEmail(email);
    user.setPhone(phone);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    Map<String, String> errors = validator.validate(user, true);
    if (!errors.isEmpty()) {
      session.setAttribute("errors", errors);
      session.setAttribute("old", user);
      response.sendRedirect(request.getHeader("Referer"));
      return;
    }
    user.setPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
    userDao.save(user);
    session.setAttribute("authUser", user);
    logger.info("New user with id {} has been registered", user.getId());
    response.sendRedirect(request.getContextPath() + "/main/");
  }
}
