package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.CategoryDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Category;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MainController extends BaseController {

  private final CategoryDao categoryDao;

  private final ProductDao productDao;

  public MainController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    categoryDao = daoFactory.getDao(CategoryDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    List<Category> categories = categoryDao.findAll();
    categories.forEach(category -> category.setProducts(productDao.findAllByCategoryAndActive(category, true)));
    request.setAttribute("categories", categories);
    request.getRequestDispatcher("/public/main_page.jsp").forward(request, response);
  }
}
