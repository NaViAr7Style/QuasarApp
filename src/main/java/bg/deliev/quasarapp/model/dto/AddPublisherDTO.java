package bg.deliev.quasarapp.model.dto;

import bg.deliev.quasarapp.model.validation.UniquePublisherName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPublisherDTO {

    @NotEmpty(message = "Publisher name is required")
    @UniquePublisherName(message = "Publisher already exists!")
    private String name;

    @NotEmpty(message = "Thumbnail is required")
    private String thumbnailUrl;

}
