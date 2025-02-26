package bg.deliev.quasarapp.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPublisherDTO {

    @NotEmpty(message = "Publisher name is required")
    private String name;

    @NotEmpty(message = "Thumbnail is required")
    private String thumbnailUrl;

}
