package yeddy.pizzeria.servlet;

import yeddy.pizzeria.Global;
import yeddy.pizzeria.controller.*;
import yeddy.pizzeria.controller.base.Controller;
import yeddy.pizzeria.dao.*;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.dao.impl.*;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainServlet extends HttpServlet {

  private Map<String, Controller> pathToController;

  @Override
  public void init(ServletConfig config) {
    ServletContext context = config.getServletContext();
    context.setAttribute("supportedLocales", Global.SUPPORTED_LOCALES);
    context.setAttribute("defaultLocale", Global.DEFAULT_LOCALE);
    initDaoFactory();
    initPathMappings();
  }

  public void initDaoFactory() {
    DaoFactory daoFactory = DaoFactory.getInstance();
    daoFactory.register(RoleDao.class, new RoleDaoImpl());
    daoFactory.register(UserDao.class, new UserDaoImpl());
    daoFactory.register(CategoryDao.class, new CategoryDaoImpl());
    daoFactory.register(ProductDao.class, new ProductDaoImpl());
    daoFactory.register(ReviewDao.class, new ReviewDaoImpl());
    daoFactory.register(CartItemDao.class, new CartItemDaoImpl());
    daoFactory.register(StatusDao.class, new StatusDaoImpl());
    daoFactory.register(OrderDao.class, new OrderDaoImpl());
    daoFactory.register(OrderProductDao.class, new OrderProductDaoImpl());
  }

  public void initPathMappings() {
    pathToController = new HashMap<>();
    pathToController.put("/", new MainController());
    pathToController.put("/view_product", new ViewProductController());
    pathToController.put("/create_review", new CreateReviewController());
    pathToController.put("/login", new LoginController());
    pathToController.put("/registration", new RegistrationController());
    pathToController.put("/profile", new ProfileController());
    pathToController.put("/cart", new CartController());
    pathToController.put("/create_order", new CreateOrderController());
    pathToController.put("/admin", new AdminController());
    pathToController.put("/admin/products", new ProductListController());
    pathToController.put("/admin/products/create", new CreateProductController());
    pathToController.put("/admin/products/update", new UpdateProductController());
    pathToController.put("/admin/orders", new OrderListController());
    pathToController.put("/admin/orders/update", new UpdateOrderController());
    pathToController.put("/logout", new LogoutController());
    pathToController.put("/set_locale", new SetLocaleController());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    if (request.getPathInfo() == null) {
      response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
      response.sendRedirect("/");
      return;
    }
    matchRequestToController(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    matchRequestToController(request, response);
  }

  private void matchRequestToController(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Optional<Controller> controller = pathToController
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().equals(request.getPathInfo()))
        .map(Map.Entry::getValue)
        .findFirst();
    if (controller.isPresent()) {
      controller.get().execute(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  public void destroy() {
    ConnectionPool connectionPool = SingleConnectionPool.getInstance();
    connectionPool.closeConnections();
  }
}
