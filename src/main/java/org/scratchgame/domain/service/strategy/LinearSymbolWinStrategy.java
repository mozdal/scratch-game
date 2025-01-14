package org.scratchgame.domain.service.strategy;

import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.model.Symbol;
import org.scratchgame.domain.model.WinCombination;
import org.scratchgame.domain.model.value.Position;
import org.scratchgame.domain.service.WinEvaluator;

import java.util.List;

public class LinearSymbolWinStrategy implements WinStrategy {

    private double bestSelectedRewardMultiplier = 0.0;

    @Override
    public void evaluate(Game game, WinEvaluator.EvaluationResult evaluationResult) {
        for (WinCombination combo : game.getWinCombinations()) {
            if ("linear_symbols".equalsIgnoreCase(combo.getWhen()) && combo.getCoveredAreas() != null) {
                for (List<Position> area : combo.getCoveredAreas()) {
                    evaluateSingleLinearArea(game, evaluationResult, combo, area);
                }
            }
            bestSelectedRewardMultiplier = 0.0;
        }
    }

    private void evaluateSingleLinearArea(Game game, WinEvaluator.EvaluationResult evaluationResult, WinCombination combo, List<Position> area) {
        String firstSymbol = null;
        boolean allMatch = true;

        for (Position pos : area) {
            String symbolName = null;
            Symbol symbol = null;

            if (pos.getRow() < game.getRows() && pos.getColumn() < game.getColumns()) {
                symbolName = game.getMatrix().getSymbolNameOfPosition(pos.getRow(), pos.getColumn());
                symbol = game.getSymbols().get(symbolName);
            }

            if (symbol == null || symbol.getType() != Symbol.SymbolType.STANDARD) {
                allMatch = false;
                break;
            }

            if (firstSymbol == null) {
                firstSymbol = symbolName;
            } else if (!firstSymbol.equals(symbolName)) {
                allMatch = false;
                break;
            }

        }

        if (allMatch && firstSymbol != null) {
            if (bestSelectedRewardMultiplier == 0.0 || bestSelectedRewardMultiplier < combo.getRewardMultiplier()) {
                bestSelectedRewardMultiplier = combo.getRewardMultiplier();
                evaluationResult.addWinningCombination(firstSymbol, combo.getName(), combo.getWhen());

            }
        }
    }
}
