package bg.deliev.quasarapp.model.entity;

import bg.deliev.quasarapp.model.enums.UserRoleEnum;
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
