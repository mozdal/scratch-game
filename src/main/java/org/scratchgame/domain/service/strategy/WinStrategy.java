package org.scratchgame.domain.service.strategy;

import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.service.WinEvaluator;

public interface WinStrategy {
    void evaluate(Game game, WinEvaluator.EvaluationResult evaluationResult);
}
