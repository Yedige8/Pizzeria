package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.ReviewDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.Review;
import yeddy.pizzeria.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ViewProductController extends BaseController {

  private final ProductDao productDao;

  private final ReviewDao reviewDao;

  public ViewProductController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    productDao = daoFactory.getDao(ProductDao.class);
    reviewDao = daoFactory.getDao(ReviewDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("productId")));
    Product product = productDao.findById(productId).orElseThrow(() -> {
      throw new RuntimeException("Product not found");
    });
    List<Review> reviews = reviewDao.findAllByProduct(product);
    boolean reviewExists = false;
    if (authUser != null) {
      reviewExists = reviewDao.existsByUserAndProduct(authUser, product);
    }
    request.setAttribute("product", product);
    request.setAttribute("reviews", reviews);
    request.setAttribute("averageRating", reviews.stream().mapToInt(Review::getRating).average().orElse(0));
    request.setAttribute("reviewExists", reviewExists);
    request.getRequestDispatcher("/public/view_product_page.jsp").forward(request, response);
  }
}
