package org.scratchgame.application;

import org.scratchgame.domain.service.WinEvaluator;
import org.scratchgame.infrastructure.config.ConfigLoader;
import org.scratchgame.infrastructure.config.GameConfig;

import org.scratchgame.domain.model.Game;

import java.io.IOException;

public class GameService {


    public GameService() { }

    public WinEvaluator.EvaluationResult playGame(String configPath, double betAmount) throws IOException {
        ConfigLoader configLoader = new ConfigLoader();
        GameConfig gameConfig = configLoader.loadConfig(configPath);
        Game game = new Game(gameConfig).createFromConfig(0.5);
        WinEvaluator evaluator = new WinEvaluator(game, betAmount);
        return evaluator.evaluate();
    }
}
