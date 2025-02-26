package bg.deliev.quasarapp.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateUserRolesDTO {

    private long id;
    private List<String> roles = new ArrayList<>();
}
