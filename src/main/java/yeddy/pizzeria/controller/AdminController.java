package yeddy.pizzeria.controller;

import yeddy.pizzeria.controller.base.BaseController;
import yeddy.pizzeria.controller.base.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminController extends BaseController {

  public AdminController() {
    super(RequestMethod.GET);
  }

  @Override
  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/admin/main_page.jsp").forward(request, response);
  }
}
