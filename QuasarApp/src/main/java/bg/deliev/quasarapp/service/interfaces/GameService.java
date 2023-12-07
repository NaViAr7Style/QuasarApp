package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    Page<GameSummaryDTO> getAllGames(Pageable pageable);

    Page<GameSummaryDTO> getAllGamesByPublisherId(long publisherId, Pageable pageable);

    GameDetailsDTO getGameDetails(Long id);

    void deleteOffer(Long id);
}
