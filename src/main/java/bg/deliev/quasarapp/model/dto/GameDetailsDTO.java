package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GameDetailsDTO {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private GameGenreEnum genre;
    private String thumbnailUrl;
    private String publisherName;
}
