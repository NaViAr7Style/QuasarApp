package bg.deliev.quasarapp.model.entities;

import bg.deliev.quasarapp.model.enums.GenreEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
public class GameEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenreEnum genre;

    @ManyToOne(optional = false)
    private ManufacturerEntity manufacturer;
}
