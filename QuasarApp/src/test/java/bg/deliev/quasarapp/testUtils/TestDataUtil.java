package bg.deliev.quasarapp.testUtils;

import bg.deliev.quasarapp.model.entity.GameEntity;
import bg.deliev.quasarapp.model.entity.PublisherEntity;
import bg.deliev.quasarapp.model.enums.GameGenreEnum;
import bg.deliev.quasarapp.model.validation.UniqueGameName;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.repository.PublisherRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataUtil {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public PublisherEntity createTestPublisher(String name, String thumbnailUrl) {
        PublisherEntity publisherEntity = new PublisherEntity();

        publisherEntity.setName(name);
        publisherEntity.setThumbnailUrl(thumbnailUrl);

        return publisherRepository.save(publisherEntity);
    }

    public GameEntity createTestGame(BigDecimal price, GameGenreEnum genre) {

        PublisherEntity testPublisher = createTestPublisher("TestPublisher", "Test Publisher URL");

        GameEntity gameEntity = new GameEntity();

        gameEntity.setPublisher(testPublisher);
        gameEntity.setName("Test Game Name");
        gameEntity.setDescription("Test Description");
        gameEntity.setPrice(price);
        gameEntity.setGenre(genre);
        gameEntity.setThumbnailUrl("Test Game URL");

        return gameRepository.save(gameEntity);
    }

    public void cleanUp() {
        gameRepository.deleteAll();
        publisherRepository.deleteAll();
    }
}
