package yeddy.pizzeria.controller.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public abstract class BaseController implements Controller {

  private final RequestMethod[] requestMethods;

  public BaseController(RequestMethod... requestMethods) {
    this.requestMethods = requestMethods;
  }

  @Override
  public boolean matchRequestMethod(String requestMethodToMatch) {
    return Arrays
        .stream(requestMethods)
        .map(RequestMethod::name)
        .anyMatch(requestMethod -> requestMethod.equals(requestMethodToMatch));
  }

  @Override
  public final void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!matchRequestMethod(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    switch (RequestMethod.valueOf(request.getMethod())) {
      case GET:
        executeGet(request, response);
        break;
      case POST:
        executePost(request, response);
        break;
    }
  }

  protected void executeGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  protected void executePost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}
