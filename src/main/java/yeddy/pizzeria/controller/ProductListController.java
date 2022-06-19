package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListController extends BaseController {

  private final ProductDao productDao;

  public ProductListController() {
    super(RequestMethod.GET);
    DaoFactory daoFactory = DaoFactory.getInstance();
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("products", productDao.findAll());
    request.getRequestDispatcher("/admin/product_list_page.jsp").forward(request, response);
  }
}
