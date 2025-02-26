package bg.deliev.quasarapp.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDetailsDTO {

    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles = new ArrayList<>();

}
