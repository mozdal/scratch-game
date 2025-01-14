package org.scratchgame.domain.service;

import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.model.Matrix;
import org.scratchgame.domain.model.Symbol;
import org.scratchgame.domain.model.WinCombination;
import org.scratchgame.domain.service.strategy.LinearSymbolWinStrategy;
import org.scratchgame.domain.service.strategy.SameSymbolWinStrategy;
import org.scratchgame.domain.service.strategy.WinStrategy;

import java.util.*;

public class WinEvaluator {

    private final Game game;
    private final double betAmount;

    private final Map<String, Map<String, String>> appliedWinningCombinations = new HashMap<>();
    private final List<WinStrategy> strategies;

    public WinEvaluator(Game game, double betAmount) {
        this.game = game;
        this.betAmount = betAmount;
        this.strategies = initializeStrategies();
    }

    private List<WinStrategy> initializeStrategies() {
        List<WinStrategy> strategyList = new ArrayList<>();
        strategyList.add(new SameSymbolWinStrategy());
        strategyList.add(new LinearSymbolWinStrategy());

        return strategyList;
    }

    public EvaluationResult evaluate() {

        EvaluationResult evaluationResult = new EvaluationResult(
                game.getMatrix(),
                0.0,
                appliedWinningCombinations,
                null
        );

        for (WinStrategy strategy : strategies) {
            strategy.evaluate(game, evaluationResult);
        }

        double totalReward = calculateSymbolRewards();

        String appliedBonusSymbol;
        if (!appliedWinningCombinations.isEmpty()) {
            appliedBonusSymbol = applyBonusSymbol();
            if (!appliedBonusSymbol.equals("none")) {
                totalReward = applyBonusEffect(totalReward, game.getSymbols().get(appliedBonusSymbol));
            }
            evaluationResult.appliedBonusSymbol = appliedBonusSymbol;
        }

        evaluationResult.reward = totalReward;

        return evaluationResult;
    }

    double calculateSymbolRewards() {
        double total = 0.0;

        for (Map.Entry<String, Map<String, String>> entry : appliedWinningCombinations.entrySet()) {
            String symbolName = entry.getKey();
            List<String> comboNames = entry.getValue().values().stream().toList();

            Symbol symbol = game.getSymbols().get(symbolName);
            if (symbol == null) continue;

            double baseReward = betAmount * symbol.getRewardMultiplier().getMultiplier();
            double multiplier = 1.0;

            for (String comboName : comboNames) {
                WinCombination combo = findWinCombinationByName(comboName);
                if (combo != null) {
                    multiplier *= combo.getRewardMultiplier();
                }
            }
            total += baseReward * multiplier;
        }

        return total;
    }

    private WinCombination findWinCombinationByName(String name) {
        for (WinCombination combo : game.getWinCombinations()) {
            if (combo.getName().equals(name)) {
                return combo;
            }
        }
        return null;
    }

    private double applyBonusEffect(double currentReward, Symbol bonusSymbol) {
        if ("multiply_reward".equalsIgnoreCase(bonusSymbol.getImpact())) {
            return currentReward * bonusSymbol.getRewardMultiplier().getMultiplier();
        } else if ("extra_bonus".equalsIgnoreCase(bonusSymbol.getImpact()) && bonusSymbol.getExtra() != null) {
            return currentReward + bonusSymbol.getExtra();
        }

        return currentReward;
    }

    private String applyBonusSymbol() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                String symbolName = game.getMatrix().getSymbolNameOfPosition(row, col);
                Symbol symbol = game.getSymbols().get(symbolName);
                if (symbol != null && symbol.getType() == Symbol.SymbolType.BONUS) {
                    return symbolName;
                }
            }
        }
        return "none";
    }

    public static class EvaluationResult {

        private final Matrix matrix;
        private double reward;
        private final Map<String, Map<String, String>> appliedWinningCombinations;

        private Map<String, List<String>> finalWinningCombinations = new HashMap<>();

        private String appliedBonusSymbol;

        public EvaluationResult(Matrix matrix, double reward, Map<String, Map<String, String>> appliedWinningCombinations, String appliedBonusSymbol) {
            this.matrix = matrix;
            this.reward = reward;
            this.appliedWinningCombinations = appliedWinningCombinations;
            this.appliedBonusSymbol = appliedBonusSymbol;
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public double getReward() {
            return reward;
        }

        public Map<String, Map<String, String>> getAppliedWinningCombinations() {
            return appliedWinningCombinations;
        }

        public String getAppliedBonusSymbol() {
            return appliedBonusSymbol;
        }

        public void addWinningCombination(String symbolName, String comboName, String comboWhen) {
            if(!appliedWinningCombinations.containsKey(symbolName)) {
                appliedWinningCombinations.put(symbolName, new HashMap<>(Map.of(comboWhen, comboName)));
            } else {
                appliedWinningCombinations.get(symbolName).put(comboWhen, comboName);
            }
        }

        public Map<String, List<String>> getFinalWinningCombinations() {
            appliedWinningCombinations.forEach((symbolName, comboMap) -> {
                List<String> comboNames = new ArrayList<>(comboMap.values());
                finalWinningCombinations.put(symbolName, comboNames);
            });

            return finalWinningCombinations;
        }
    }
}

