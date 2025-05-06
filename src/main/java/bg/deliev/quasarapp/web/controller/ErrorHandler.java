package bg.deliev.quasarapp.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleErrors(HttpServletResponse response, Exception e) {

        LOGGER.error("Unhandled exception occurred: {}", e.getMessage(), e);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500

        return new ModelAndView("error");
    }
}