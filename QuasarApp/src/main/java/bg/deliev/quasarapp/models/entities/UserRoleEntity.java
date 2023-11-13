package bg.deliev.quasarapp.models.entities;

import bg.deliev.quasarapp.models.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class UserRoleEntity extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
