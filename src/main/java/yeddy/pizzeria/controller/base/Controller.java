package yeddy.pizzeria.controller.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

  boolean matchRequestMethod(String requestMethodToMatch);

  void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
