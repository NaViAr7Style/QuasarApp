package bg.deliev.quasarapp.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class PaginationUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(PaginationUtils.class);

  public record PaginationParams(String size, String page) {}

  public static PaginationParams extractPaginationParams(HttpServletRequest request) {

    return Optional.ofNullable(request.getHeader("Referer"))
        .map(PaginationUtils::parsePaginationFromUri)
        .orElse(new PaginationParams(null, null));

  }

  private static PaginationParams parsePaginationFromUri(String refererUrl) {
    try {

      URI uri = new URI(refererUrl);
      MultiValueMap<String, String> queryParams = UriComponentsBuilder
          .fromUri(uri)
          .build()
          .getQueryParams();

      return new PaginationParams(

          queryParams.getFirst("size"),
          queryParams.getFirst("page")
      );

    } catch (URISyntaxException | IllegalArgumentException e) {

      LOGGER.error("Error parsing URI: {}", e.getMessage());
      return new PaginationParams(null, null);

    }
  }

}
