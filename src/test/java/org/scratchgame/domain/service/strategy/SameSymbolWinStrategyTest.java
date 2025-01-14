package org.scratchgame.domain.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scratchgame.domain.model.*;
import org.scratchgame.domain.service.WinEvaluator;
import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.infrastructure.config.SymbolConfig;
import org.scratchgame.infrastructure.config.WinCombinationConfig;
import org.scratchgame.infrastructure.config.ProbabilitiesConfig;
import org.scratchgame.infrastructure.config.model.SymbolProbabilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SameSymbolWinStrategyTest {

    private GameConfig gameConfig;
    private SameSymbolWinStrategy strategy;

    @BeforeEach
    void setUp() {
        gameConfig = new GameConfig();
        gameConfig.setRows(3);
        gameConfig.setColumns(3);

        SymbolConfig symA = new SymbolConfig();
        symA.setRewardMultiplier(2.0);
        symA.setType("standard");

        SymbolConfig symB = new SymbolConfig();
        symB.setRewardMultiplier(3.0);
        symB.setType("standard");

        Map<String, SymbolConfig> symbolMap = new HashMap<>();
        symbolMap.put("A", symA);
        symbolMap.put("B", symB);
        symbolMap.put("C", symB);
        symbolMap.put("D", symB);
        symbolMap.put("E", symB);
        symbolMap.put("F", symB);
        gameConfig.setSymbols(symbolMap);

        WinCombinationConfig sameSymCombo = new WinCombinationConfig();
        sameSymCombo.setRewardMultiplier(1.5);
        sameSymCombo.setWhen("same_symbols");
        sameSymCombo.setCount(3);
        sameSymCombo.setGroup("group1");

        Map<String, WinCombinationConfig> comboMap = new HashMap<>();
        comboMap.put("three_same_symbols", sameSymCombo);
        gameConfig.setWinCombinations(comboMap);

        strategy = new SameSymbolWinStrategy();
    }

    @Test
    void testNoTripleSymbol() {
        List<SymbolProbabilities> cellProbs = new ArrayList<>();

        cellProbs.add(makeCellProb(0, 0, "A"));
        cellProbs.add(makeCellProb(0, 1, "B"));
        cellProbs.add(makeCellProb(0, 2, "C"));

        cellProbs.add(makeCellProb(1, 0, "D"));
        cellProbs.add(makeCellProb(1, 1, "E"));
        cellProbs.add(makeCellProb(1, 2, "F"));

        cellProbs.add(makeCellProb(2, 0, "A"));
        cellProbs.add(makeCellProb(2, 1, "B"));
        cellProbs.add(makeCellProb(2, 2, "C"));

        ProbabilitiesConfig probConfig = new ProbabilitiesConfig();
        probConfig.setStandardSymbols(cellProbs);

        SymbolProbabilities bonusDummy = new SymbolProbabilities();
        bonusDummy.setColumn(0);
        bonusDummy.setRow(0);
        bonusDummy.setSymbols(Map.of("DUMMY_BONUS", 1));
        probConfig.setBonusSymbols(bonusDummy);

        gameConfig.setProbabilities(probConfig);

        Game game = new Game(gameConfig).createFromConfig(0.2);

        WinEvaluator.EvaluationResult evalResult = new WinEvaluator.EvaluationResult(
                game.getMatrix(),
                0.0,
                new HashMap<>(),
                null
        );

        strategy.evaluate(game, evalResult);

        assertTrue(evalResult.getAppliedWinningCombinations().isEmpty(),
                "No triple => no combos should be recorded");
    }

    @Test
    void testTripleA() {
        List<SymbolProbabilities> cellProbs = new ArrayList<>();

        cellProbs.add(makeCellProb(0, 0, "A"));
        cellProbs.add(makeCellProb(1, 0, "B"));
        cellProbs.add(makeCellProb(2, 0, "C"));

        cellProbs.add(makeCellProb(0, 1, "A"));
        cellProbs.add(makeCellProb(1, 1, "A"));
        cellProbs.add(makeCellProb(2, 1, "A")); // random choice

        cellProbs.add(makeCellProb(0, 2, "A"));
        cellProbs.add(makeCellProb(1, 2, "B"));
        cellProbs.add(makeCellProb(2, 2, "C"));

        ProbabilitiesConfig probConfig = new ProbabilitiesConfig();
        probConfig.setStandardSymbols(cellProbs);

        SymbolProbabilities bonusDummy = new SymbolProbabilities();
        bonusDummy.setColumn(0);
        bonusDummy.setRow(0);
        bonusDummy.setSymbols(Map.of("DUMMY_BONUS", 1));
        probConfig.setBonusSymbols(bonusDummy);

        gameConfig.setProbabilities(probConfig);

        Game game = new Game(gameConfig).createFromConfig(0);

        WinEvaluator.EvaluationResult evalResult = new WinEvaluator.EvaluationResult(
                game.getMatrix(),
                0.0,
                new HashMap<>(),
                null
        );
        strategy.evaluate(game, evalResult);

        var combos = evalResult.getAppliedWinningCombinations();
        assertFalse(combos.isEmpty(), "We do have a triple for 'A' => combos should be non-empty");
        assertTrue(combos.containsKey("A"), "Symbol 'A' should be recognized in combos");

        Map<String, String> subMap = combos.get("A");
        assertNotNull(subMap);
        assertEquals("three_same_symbols", subMap.get("same_symbols"),
                "Should detect 'three_symbol' combo for 'A'");
    }

    private SymbolProbabilities makeCellProb(int row, int col, String forcedSymbol) {
        SymbolProbabilities cellProb = new SymbolProbabilities();
        cellProb.setRow(row);
        cellProb.setColumn(col);

        Map<String, Integer> distribution = new HashMap<>();
        distribution.put(forcedSymbol, 100);
        cellProb.setSymbols(distribution);
        return cellProb;
    }
}
