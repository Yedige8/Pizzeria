package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.StatusDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class UpdateOrderController extends BaseController {

  private final Logger logger = LogManager.getLogger(UpdateOrderController.class);

  private final StatusDao statusDao;

  private final OrderDao orderDao;

  private final OrderProductDao orderProductDao;

  public UpdateOrderController() {
    super(RequestMethod.GET, RequestMethod.POST);
    DaoFactory daoFactory = DaoFactory.getInstance();
    statusDao = daoFactory.getDao(StatusDao.class);
    orderDao = daoFactory.getDao(OrderDao.class);
    orderProductDao = daoFactory.getDao(OrderProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    long orderId = Long.parseLong(Objects.requireNonNull(request.getParameter("orderId")));
    Order order = orderDao.findById(orderId).orElseThrow(() -> {
      throw new RuntimeException("Order not found");
    });
    request.setAttribute("statuses", statusDao.findAll());
    request.setAttribute("order", order);
    request.setAttribute("orderProducts", orderProductDao.findAllByOrder(order));
    request.getRequestDispatcher("/admin/update_order_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    logger.info(request.getParameter("orderId"));
    logger.info(request.getParameter("statusId"));
    long orderId = Long.parseLong(Objects.requireNonNull(request.getParameter("orderId")));
    long statusId = Long.parseLong(Objects.requireNonNull(request.getParameter("statusId")));
    logger.info(orderId);
    logger.info(statusId);
    Order order = orderDao.findById(orderId).orElseThrow(() -> {
      logger.error("Order with id {} wasn't found for status update", orderId);
      throw new RuntimeException("Status not found");
    });
    Status status = statusDao.findById(statusId).orElseThrow(() -> {
      logger.error("Status with id {} wasn't found", statusId);
      throw new RuntimeException("Status not found");
    });
    order.setStatus(status);
    orderDao.update(order);
    logger.info("Order with id {} was successfully updated", order.getId());
    response.sendRedirect(request.getContextPath() + "/main/admin/orders");
  }
}
