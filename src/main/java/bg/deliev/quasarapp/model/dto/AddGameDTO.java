package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.model.validation.UniqueGameName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddGameDTO {

    @NotEmpty(message = "Game name is required.")
    @UniqueGameName(message = "Game already exists!")
    private String name;

    @NotEmpty(message = "Description is required.")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "Genre is required")
    private GameGenreEnum genre;

    @NotEmpty(message = "Thumbnail is required.")
    private String thumbnailUrl;

    @NotEmpty(message = "Publisher is required.")
    private String publisherName;
}
