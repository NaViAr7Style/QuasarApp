package bg.deliev.quasarapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "publishers")
@Getter
@Setter
public class PublisherEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "thumbnail_url", nullable = false, columnDefinition = "TEXT")
    private String thumbnailUrl;

    @OneToMany(targetEntity = GameEntity.class,
            mappedBy = "publisher",
            fetch = FetchType.LAZY)
    private List<GameEntity> games;
}
