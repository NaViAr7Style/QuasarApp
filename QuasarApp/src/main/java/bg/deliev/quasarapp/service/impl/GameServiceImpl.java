package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.GameSummaryDTO;
import bg.deliev.quasarapp.repository.GameRepository;
import bg.deliev.quasarapp.service.interfaces.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<GameSummaryDTO> getAllGames(Pageable pageable) {
        return gameRepository
                .findAll(pageable)
                .map(gameEntity -> modelMapper.map(gameEntity, GameSummaryDTO.class));
    }
}
