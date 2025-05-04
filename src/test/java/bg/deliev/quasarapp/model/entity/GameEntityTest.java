package bg.deliev.quasarapp.model.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static bg.deliev.quasarapp.testUtils.TestUtils.createValidGame;
import static bg.deliev.quasarapp.testUtils.TestUtils.createValidPublisher;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class GameEntityTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  void testWhenGameIsValidThenItPersists() {
    PublisherEntity publisher = createValidPublisher("Test Publisher");
    entityManager.persist(publisher);

    GameEntity game = createValidGame(publisher);
    entityManager.persist(game);
    entityManager.flush();

    assertNotNull(game.getId());
  }

  @Test
  void testWhenPriceIsNegativeValidationFails() {
    PublisherEntity publisher = createValidPublisher("Test Publisher");
    entityManager.persist(publisher);

    GameEntity game = createValidGame(publisher);
    game.setPrice(new BigDecimal("-10.00"));

    assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
      entityManager.persist(game);
      entityManager.flush();
    });
  }

  @Test
  void testWhenNameIsNullThrowsConstraintViolation() {
    PublisherEntity publisher = createValidPublisher("Test Publisher");
    entityManager.persist(publisher);

    GameEntity game = createValidGame(publisher);
    game.setName(null);

    assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
      entityManager.persist(game);
      entityManager.flush();
    });
  }

  @Test
  void testWhenNameIsDuplicateThenFails() {
    PublisherEntity publisher = createValidPublisher("Test Publisher");
    entityManager.persist(publisher);

    GameEntity game1 = createValidGame(publisher);
    GameEntity game2 = createValidGame(publisher);

    entityManager.persist(game1);
    entityManager.flush();

    assertThrows(ConstraintViolationException.class, () -> {
      entityManager.persist(game2);
      entityManager.flush();
    });
  }

  @Test
  void testWhenPublisherIsNullThrowsConstraintViolation() {
    GameEntity game = createValidGame(null);

    assertThrows(ConstraintViolationException.class, () -> {
      entityManager.persist(game);
      entityManager.flush();
    });
  }

  @Test
  void testWhenNameIsBlankValidationFails() {
    PublisherEntity publisher = createValidPublisher("Test Publisher");
    entityManager.persist(publisher);

    GameEntity game = createValidGame(publisher);
    game.setName("   "); // or ""

    assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
      entityManager.persist(game);
      entityManager.flush();
    });
  }
}
