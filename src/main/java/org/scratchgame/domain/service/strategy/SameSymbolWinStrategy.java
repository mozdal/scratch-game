package org.scratchgame.domain.service.strategy;

import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.model.Symbol;
import org.scratchgame.domain.model.WinCombination;
import org.scratchgame.domain.service.WinEvaluator;

import java.util.HashMap;
import java.util.Map;

public class SameSymbolWinStrategy implements WinStrategy {

    @Override
    public void evaluate(Game game, WinEvaluator.EvaluationResult evaluationResult) {
        Map<String, Integer> symbolCounts = countStandardSymbols(game);

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbolName = entry.getKey();
            int count = entry.getValue();

            for (WinCombination combo : game.getWinCombinations()) {
                if ("same_symbols".equalsIgnoreCase(combo.getWhen()) && count >= combo.getCount()) {
                    evaluationResult.addWinningCombination(symbolName, combo.getName(), combo.getWhen());
                }
            }
        }
    }

    private Map<String, Integer> countStandardSymbols(Game game) {
        Map<String, Integer> counts = new HashMap<>();

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                String symbolName = game.getMatrix().getSymbolNameOfPosition(row, col);
                Symbol symbol = game.getSymbols().get(symbolName);
                if (symbol != null && symbol.getType() == Symbol.SymbolType.STANDARD) {
                    counts.put(symbolName, counts.getOrDefault(symbolName, 0) + 1);
                }
            }
        }

        return counts;
    }
}
