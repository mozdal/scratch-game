package org.scratchgame.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scratchgame.infrastructure.config.ConfigLoader;
import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.service.WinEvaluator;
import org.scratchgame.infrastructure.config.ProbabilitiesConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameService gameService;
    private ConfigLoader configLoader;
    private GameConfig gameConfig;
    private Game game;
    private WinEvaluator evaluator;

    @BeforeEach
    void setUp() {
        configLoader = mock(ConfigLoader.class);
        gameService = new GameService();
        gameConfig = mock(GameConfig.class);
        game = mock(Game.class);
        evaluator = mock(WinEvaluator.class);
    }

    @Test
    void playGame_validConfigPathAndBetAmount_returnsEvaluationResult() throws IOException {
        String configPath = "config.json";
        double betAmount = 10.0;
        when(configLoader.loadConfig(configPath)).thenReturn(gameConfig);
        when(gameConfig.getProbabilities()).thenReturn(mock(ProbabilitiesConfig.class));
        when(game.createFromConfig(0)).thenReturn(game);
        WinEvaluator.EvaluationResult evaluationResult = mock(WinEvaluator.EvaluationResult.class);
        when(evaluator.evaluate()).thenReturn(evaluationResult);

        WinEvaluator.EvaluationResult result = gameService.playGame(configPath, betAmount);

        assertNotNull(result);
    }

    @Test
    void playGame_invalidConfigPath_throwsIOException() throws IOException {
        String configPath = "invalid/path/to/config";
        double betAmount = 10.0;
        when(configLoader.loadConfig(configPath)).thenThrow(new IOException());

        assertThrows(IOException.class, () -> gameService.playGame(configPath, betAmount));
    }

    @Test
    void playGame_zeroBetAmount_returnsEvaluationResult() throws IOException {
        String configPath = "config.json";
        double betAmount = 0.0;
        when(configLoader.loadConfig(configPath)).thenReturn(gameConfig);
        when(gameConfig.getProbabilities()).thenReturn(mock(ProbabilitiesConfig.class));
        when(game.createFromConfig(0)).thenReturn(game);
        WinEvaluator.EvaluationResult evaluationResult = mock(WinEvaluator.EvaluationResult.class);
        when(evaluator.evaluate()).thenReturn(evaluationResult);

        WinEvaluator.EvaluationResult result = gameService.playGame(configPath, betAmount);

        assertNotNull(result);
    }
}