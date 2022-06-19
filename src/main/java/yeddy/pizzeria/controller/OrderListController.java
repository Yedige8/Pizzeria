package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.StatusDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.OrderProduct;
import yeddy.pizzeria.model.dto.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderListController extends BaseController {

  private final StatusDao statusDao;

  private final OrderDao orderDao;

  private final OrderProductDao orderProductDao;

  public OrderListController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    statusDao = daoFactory.getDao(StatusDao.class);
    orderDao = daoFactory.getDao(OrderDao.class);
    orderProductDao = daoFactory.getDao(OrderProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String statusIdParam = request.getParameter("status_id");
    List<Order> orders;
    if (statusIdParam != null) {
      long statusId = Long.parseLong(statusIdParam);
      Status status = statusDao.findById(statusId).orElseThrow(() -> {
        throw new RuntimeException("Status not found");
      });
      orders = orderDao.findAllByStatus(status);
    } else {
      orders = orderDao.findAll();
    }
    orders.forEach(order -> order.setOrderProducts(orderProductDao.findAllByOrder(order)));
    request.setAttribute("orders", orders);
    request.getRequestDispatcher("/admin/order_list_page.jsp").forward(request, response);
  }
}
