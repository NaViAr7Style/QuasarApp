package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.GameDetailsDTO;
import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    Page<GameSummaryDTO> getAllGames(Pageable pageable);

    GameDetailsDTO getGameDetails(Long id);
}
