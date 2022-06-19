package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.CartItemDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.CartItem;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CartController extends BaseController {

  private final Logger logger = LogManager.getLogger(CartController.class);

  private final CartItemDao cartItemDao;

  private final ProductDao productDao;

  public CartController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    cartItemDao = daoFactory.getDao(CartItemDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action == null) {
      cartPage(request, response);
      return;
    }
    switch (action) {
      case "add":
        addToCartAction(request, response);
        break;
      case "delete":
        deleteFromCartAction(request, response);
        break;
      case "clear":
        clearCartAction(request, response);
        break;
      default:
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
  }

  public void cartPage(HttpServletRequest request, HttpServletResponse response)
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
    request.getRequestDispatcher("/public/cart_page.jsp").forward(request, response);
  }

  public void addToCartAction(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("product_id")));
    Product product = productDao.findById(productId).orElseThrow(() -> {
      logger.error("Product with id {} wasn't found for to be added to cart", productId);
      throw new RuntimeException("Product not found");
    });
    Optional<CartItem> cartItem = cartItemDao.findByUserAndProduct(authUser, product);
    if (cartItem.isPresent()) {
      CartItem cartItemValue = cartItem.get();
      cartItemValue.setAmount(cartItemValue.getAmount() + 1);
      cartItemDao.update(cartItemValue);
      logger.info("Product with id {} was successfully added to cart with amount {}", productId, cartItemValue.getAmount());
    } else {
      CartItem cartItemValue = new CartItem();
      cartItemValue.setUser(authUser);
      cartItemValue.setProduct(product);
      cartItemValue.setAmount(1);
      cartItemDao.save(cartItemValue);
      logger.info("Product with id {} was successfully added to cart with amount 1", productId);
    }
    response.sendRedirect(request.getHeader("Referer"));
  }

  public void deleteFromCartAction(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("product_id")));
    Product product = productDao.findById(productId).orElseThrow(() -> {
      logger.error("Product with id {} wasn't found for to be deleted from cart", productId);
      throw new RuntimeException("Product not found");
    });
    cartItemDao.deleteByUserAndProduct(authUser, product);
    logger.info("Product with id {} was successfully deleted from cart", productId);
    response.sendRedirect(request.getHeader("Referer"));
  }

  public void clearCartAction(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    cartItemDao.deleteByUser(authUser);
    logger.info("User with id {} was deleted all products from cart", authUser.getId());
    response.sendRedirect(request.getHeader("Referer"));
  }
}
