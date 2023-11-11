package bg.deliev.quasarapp.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "manufacturers")
@Getter
@Setter
public class ManufacturerEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(targetEntity = GameEntity.class,
            mappedBy = "manufacturer",
            fetch = FetchType.LAZY)
    private List<GameEntity> games;
}
