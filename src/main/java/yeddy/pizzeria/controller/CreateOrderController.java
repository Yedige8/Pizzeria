package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.CartItemDao;
import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.StatusDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CreateOrderController extends BaseController {

  private final Logger logger = LogManager.getLogger(CreateOrderController.class);

  private final CartItemDao cartItemDao;

  private final StatusDao statusDao;

  private final OrderDao orderDao;

  private final OrderProductDao orderProductDao;

  public CreateOrderController() {
    super(RequestMethod.GET, RequestMethod.POST);
    DaoFactory daoFactory = DaoFactory.getInstance();
    cartItemDao = daoFactory.getDao(CartItemDao.class);
    statusDao = daoFactory.getDao(StatusDao.class);
    orderDao = daoFactory.getDao(OrderDao.class);
    orderProductDao = daoFactory.getDao(OrderProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    List<CartItem> cartItems = cartItemDao.findAllByUser(authUser);
    int cartTotal = cartItems
        .stream()
        .mapToInt(cartItem -> cartItem.getProduct().getCost() * cartItem.getAmount())
        .sum();
    request.setAttribute("cartItems", cartItems);
    request.setAttribute("cartTotal", cartTotal);
    request.getRequestDispatcher("/public/create_order_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    String street = Objects.requireNonNull(request.getParameter("street"));
    String house = Objects.requireNonNull(request.getParameter("house"));
    String apartment = Objects.requireNonNull(request.getParameter("apartment"));
    Order order = new Order();
    order.setUser(authUser);
    Status status = statusDao.findByServiceName("awaiting_confirmation").orElseThrow(() -> {
      logger.error("Status with service name `awaiting_confirmation` wasn't found");
      throw new RuntimeException("Status not found");
    });
    order.setStatus(status);
    order.setStreet(street);
    order.setHouse(house);
    order.setApartment(apartment);
    orderDao.save(order);
    logger.info("Order with id {} was successfully created by user with id {}", order.getId(), authUser.getId());
    List<CartItem> cartItems = cartItemDao.findAllByUser(authUser);
    for (CartItem cartItem : cartItems) {
      OrderProduct orderProduct = new OrderProduct();
      orderProduct.setOrder(order);
      orderProduct.setProduct(cartItem.getProduct());
      orderProduct.setAmount(cartItem.getAmount());
      orderProductDao.save(orderProduct);
    }
    cartItemDao.deleteByUser(authUser);
    logger.info("User with id {} was deleted all products from cart after creating order", authUser.getId());
    response.sendRedirect(request.getContextPath() + "/main/profile");
  }
}
