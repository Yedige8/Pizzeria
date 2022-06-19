package yeddy.pizzeria.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yeddy.pizzeria.Global;
import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.CategoryDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Category;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.validator.ProductValidator;
import yeddy.pizzeria.validator.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class UpdateProductController extends BaseController {

  private final Logger logger = LogManager.getLogger(UpdateProductController.class);

  private final Validator<Product> validator;

  private final CategoryDao categoryDao;

  private final ProductDao productDao;

  public UpdateProductController() {
    super(RequestMethod.GET, RequestMethod.POST);
    validator = new ProductValidator();
    DaoFactory daoFactory = DaoFactory.getInstance();
    categoryDao = daoFactory.getDao(CategoryDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("productId")));
    Product product = productDao.findById(productId).orElseThrow(() -> {
      throw new RuntimeException("Product not found");
    });
    request.setAttribute("categories", categoryDao.findAll());
    request.setAttribute("product", product);
    request.getRequestDispatcher("/admin/update_product_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    User authUser = (User) session.getAttribute("authUser");
    long productId = Long.parseLong(Objects.requireNonNull(request.getParameter("productId")));
    long categoryId = Long.parseLong(Objects.requireNonNull(request.getParameter("categoryId")));
    boolean active = Boolean.parseBoolean(Objects.requireNonNull(request.getParameter("active")));
    String name = Objects.requireNonNull(request.getParameter("name"));
    String description = Objects.requireNonNull(request.getParameter("description"));
    int cost = Integer.parseInt(Objects.requireNonNull(request.getParameter("cost")));
    Product product = productDao.findById(productId).orElseThrow(() -> {
      logger.error("Product with id {} wasn't found", productId);
      throw new RuntimeException("Product not found");
    });
    Category category = categoryDao.findById(categoryId).orElse(null);
    product.setCategory(category);
    product.setActive(active);
    product.setName(name);
    product.setDescription(description);
    product.setCost(cost);
    Map<String, String> errors = validator.validate(product, false);
    Part filePart = request.getPart("image");
    if (filePart != null && filePart.getSize() != 0 && !getFileType(filePart).equals("jpeg")) {
      errors.put("image", "Файл может быть только формата JPEG");
    }
    if (!errors.isEmpty()) {
      session.setAttribute("errors", errors);
      response.sendRedirect(request.getHeader("Referer"));
      return;
    }
    productDao.update(product);
    logger.info("Product with id {} was updated by user with id {}", productId, authUser.getId());
    String uploadPath = request.getServletContext().getRealPath("/upload");
    upload(uploadPath, product.getId().toString(), filePart);
    response.sendRedirect(request.getContextPath() + "/main/admin/products");
  }

  private void upload(String uploadPath, String fileName, Part filePart) throws IOException {
    File file = new File(uploadPath);
    if (!file.exists() && !file.mkdir()) {
      throw new RuntimeException("Failed to create a folder for uploads");
    }
    filePart.write(uploadPath + "/" + fileName + "." + getFileType(filePart));
  }

  private String getFileType(Part filePart) {
    String[] contentTypeFragments = filePart.getHeader("content-type").split("/");
    return contentTypeFragments[contentTypeFragments.length - 1];
  }
}
