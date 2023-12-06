package bg.deliev.quasarapp.model.entity;

import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameGenreEnum genre;

    @Column(name = "thumbnail_url", nullable = false, columnDefinition = "TEXT")
    private String thumbnailUrl;

    @ManyToOne(optional = false)
    private PublisherEntity publisher;

}
