package org.scratchgame.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.model.Symbol;
import org.scratchgame.domain.model.value.Reward;
import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.infrastructure.config.ProbabilitiesConfig;
import org.scratchgame.infrastructure.config.SymbolConfig;
import org.scratchgame.infrastructure.config.WinCombinationConfig;
import org.scratchgame.infrastructure.config.model.SymbolProbabilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WinEvaluatorTest {

    private GameConfig gameConfig;
    private double betAmount;

    @BeforeEach
    void setUp() {
        gameConfig = new GameConfig();
        gameConfig.setRows(3);
        gameConfig.setColumns(3);

        SymbolConfig symbolAConfig = new SymbolConfig();
        symbolAConfig.setRewardMultiplier(2.0);
        symbolAConfig.setType("standard");

        SymbolConfig symbolBConfig = new SymbolConfig();
        symbolBConfig.setRewardMultiplier(3.0);
        symbolBConfig.setType("standard");

        Map<String, SymbolConfig> symbolsMap = new HashMap<>();
        symbolsMap.put("A", symbolAConfig);
        symbolsMap.put("B", symbolBConfig);
        gameConfig.setSymbols(symbolsMap);

        WinCombinationConfig threeSymbolCombo = new WinCombinationConfig();
        threeSymbolCombo.setRewardMultiplier(1.5);
        threeSymbolCombo.setWhen("same_symbols");
        threeSymbolCombo.setCount(3);
        threeSymbolCombo.setGroup("group1");

        WinCombinationConfig irrelevantLinear = new WinCombinationConfig();
        irrelevantLinear.setRewardMultiplier(2.0);
        irrelevantLinear.setWhen("linear_symbols");
        irrelevantLinear.setCount(0);
        irrelevantLinear.setGroup("group2");

        Map<String, WinCombinationConfig> comboMap = new HashMap<>();
        comboMap.put("three_symbol", threeSymbolCombo);
        comboMap.put("irrelevant_linear", irrelevantLinear);
        gameConfig.setWinCombinations(comboMap);

        List<SymbolProbabilities> standardSymbols = new ArrayList<>();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                SymbolProbabilities cellProb = new SymbolProbabilities();
                cellProb.setRow(row);
                cellProb.setColumn(col);

                Map<String, Integer> distribution = new HashMap<>();
                distribution.put("A", 1);
                distribution.put("B", 1);

                cellProb.setSymbols(distribution);
                standardSymbols.add(cellProb);
            }
        }

        SymbolProbabilities bonusSymbols = new SymbolProbabilities();
        bonusSymbols.setRow(0);
        bonusSymbols.setColumn(0);
        Map<String, Integer> bonusDist = new HashMap<>();
        bonusDist.put("ZERO_BONUS", 1);
        bonusSymbols.setSymbols(bonusDist);

        ProbabilitiesConfig probabilitiesConfig = new ProbabilitiesConfig();
        probabilitiesConfig.setStandardSymbols(standardSymbols);
        probabilitiesConfig.setBonusSymbols(bonusSymbols);

        gameConfig.setProbabilities(probabilitiesConfig);
        betAmount = 100.0;
    }


    @Test
    void testEvaluate_NoWins() {
        Game game = new Game(gameConfig).createFromConfig(0.2);
        WinEvaluator evaluator = new WinEvaluator(game, betAmount);
        WinEvaluator.EvaluationResult result = evaluator.evaluate();

        double finalReward = result.getReward();

        assertNotNull(result);
        assertTrue(finalReward >= 0.0, "Reward should be >= 0.0 (non-negative)");
        assertNotNull(result.getAppliedWinningCombinations());
    }


    @Test
    void testEvaluate_TripleAWin() {

        List<SymbolProbabilities> standardSymbols = new ArrayList<>();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                SymbolProbabilities cellProb = new SymbolProbabilities();
                cellProb.setRow(row);
                cellProb.setColumn(col);

                Map<String, Integer> distribution = new HashMap<>();
                distribution.put("A", 1000);

                cellProb.setSymbols(distribution);
                standardSymbols.add(cellProb);
            }
        }

        gameConfig.getProbabilities().setStandardSymbols(standardSymbols);

        Game game = new Game(gameConfig).createFromConfig(0);

        WinEvaluator evaluator = new WinEvaluator(game, betAmount);
        WinEvaluator.EvaluationResult result = evaluator.evaluate();

        double expected=300.0;
        double actual=result.getReward();
        assertEquals(expected, actual, 0.001,"Should produce 300.0 for triple A with no bonus symbol present");

        assertFalse(result.getAppliedWinningCombinations().isEmpty(), "Should have at least one win combination for A");
        Map<String, Map<String, String>> combos = result.getAppliedWinningCombinations();
        assertTrue(combos.containsKey("A"), "A should have a combo");

        assertFalse(combos.get("A").isEmpty(),"A should have something in its combo map");
        assertEquals("none", result.getAppliedBonusSymbol());

        if (result.getAppliedBonusSymbol() != null) {
            assertEquals("none", result.getAppliedBonusSymbol(), "No bonus => 'none'");
        }
    }
}
