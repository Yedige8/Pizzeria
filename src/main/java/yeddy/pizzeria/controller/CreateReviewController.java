package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;

public class CreateReviewController extends BaseController {

  private final Logger logger = LogManager.getLogger(CreateReviewController.class);

  private final ProductDao productDao;

  private final ReviewDao reviewDao;

  public CreateReviewController() {
    super(RequestMethod.POST);
    DaoFactory daoFactory = DaoFactory.getInstance();
    productDao = daoFactory.getDao(ProductDao.class);
    reviewDao = daoFactory.getDao(ReviewDao.class);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("productId")));
    int rating = Integer.parseInt(Objects.requireNonNull(request.getParameter("rating")));
    String comment = Objects.requireNonNull(request.getParameter("comment"));
    Review review = new Review();
    review.setUser(authUser);
    Product product = productDao.findById(productId).orElseThrow(() -> {
      logger.error("Product with id {} wasn't found for writing a review", productId);
      throw new RuntimeException("Product not found");
    });
    review.setProduct(product);
    review.setRating(rating);
    review.setComment(comment);
    reviewDao.save(review);
    logger.info("Review with id {} was created for product with id {} by user with id {}", review.getId(), product.getId(), authUser.getId());
    response.sendRedirect(request.getHeader("Referer"));
  }
}
