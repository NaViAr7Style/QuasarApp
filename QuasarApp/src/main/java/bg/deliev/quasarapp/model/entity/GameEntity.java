package bg.deliev.quasarapp.model.entity;

import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "games")
@Getter
@Setter
public class GameEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameGenreEnum genre;

    @ManyToOne(optional = false)
    private ManufacturerEntity manufacturer;

}
