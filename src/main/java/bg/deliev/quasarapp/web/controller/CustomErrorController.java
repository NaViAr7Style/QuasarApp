package bg.deliev.quasarapp.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

    // Optional: Log everything
    System.out.println("Error status: " + status);
    if (exception != null) {
      System.out.println("Exception: " + exception);
    }

    return "error"; // return a Thymeleaf/JSP error view
  }
}