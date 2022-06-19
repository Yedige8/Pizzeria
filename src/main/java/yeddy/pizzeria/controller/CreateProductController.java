package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.CategoryDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Category;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.validator.ProductValidator;
import yeddy.pizzeria.validator.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CreateProductController extends BaseController {

  private final Validator<Product> validator;

  private final CategoryDao categoryDao;

  private final ProductDao productDao;

  public CreateProductController() {
    super(RequestMethod.GET, RequestMethod.POST);
    validator = new ProductValidator();
    DaoFactory daoFactory = DaoFactory.getInstance();
    categoryDao = daoFactory.getDao(CategoryDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("categories", categoryDao.findAll());
    request.getRequestDispatcher("/admin/create_product_page.jsp").forward(request, response);
  }

  @Override
  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    long categoryId = Long.parseLong(Objects.requireNonNull(request.getParameter("categoryId")));
    boolean active = Boolean.parseBoolean(Objects.requireNonNull(request.getParameter("active")));
    String name = Objects.requireNonNull(request.getParameter("name"));
    String description = Objects.requireNonNull(request.getParameter("description"));
    int cost = Integer.parseInt(Objects.requireNonNull(request.getParameter("cost")));
    Product product = new Product();
    Category category = categoryDao.findById(categoryId).orElse(null);
    product.setCategory(category);
    product.setActive(active);
    product.setName(name);
    product.setDescription(description);
    product.setCost(cost);
    Map<String, String> errors = validator.validate(product, true);
    Part filePart = request.getPart("image");
    if (filePart != null && filePart.getSize() != 0 && !getFileType(filePart).equals("jpeg")) {
      errors.put("image", "Файл может быть только формата JPEG");
    }
    if (!errors.isEmpty()) {
      session.setAttribute("errors", errors);
      session.setAttribute("old", product);
      response.sendRedirect(request.getHeader("Referer"));
      return;
    }
    productDao.save(product);
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
