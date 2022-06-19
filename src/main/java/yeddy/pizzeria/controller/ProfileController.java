package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ProfileController extends BaseController {

  private final OrderDao orderDao;

  private final OrderProductDao orderProductDao;

  public ProfileController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    orderDao = daoFactory.getDao(OrderDao.class);
    orderProductDao = daoFactory.getDao(OrderProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    List<Order> orders = orderDao.findAllByUser(authUser);
    orders.forEach(order -> order.setOrderProducts(orderProductDao.findAllByOrder(order)));
    request.setAttribute("orders", orders);
    request.getRequestDispatcher("/public/profile_page.jsp").forward(request, response);
  }
}
