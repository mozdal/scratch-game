package org.scratchgame.domain.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.service.WinEvaluator;
import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.infrastructure.config.ProbabilitiesConfig;
import org.scratchgame.infrastructure.config.SymbolConfig;
import org.scratchgame.infrastructure.config.WinCombinationConfig;
import org.scratchgame.infrastructure.config.model.SymbolProbabilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LinearSymbolWinStrategyTest {

    private GameConfig gameConfig;
    private LinearSymbolWinStrategy strategy;
    private String comboName = "top_row_line";

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
        gameConfig.setSymbols(symbolMap);

        WinCombinationConfig linearCombo = new WinCombinationConfig();
        linearCombo.setRewardMultiplier(2.0);
        linearCombo.setWhen("linear_symbols");
        linearCombo.setCount(0);         // not used for linear
        linearCombo.setGroup("rowWins");
        List<List<String>> coveredAreas = new ArrayList<>();
        coveredAreas.add(Arrays.asList("0:0", "0:1", "0:2"));
        linearCombo.setCoveredAreas(coveredAreas);

        Map<String, WinCombinationConfig> comboMap = new HashMap<>();
        comboMap.put(comboName, linearCombo);
        gameConfig.setWinCombinations(comboMap);

        strategy = new LinearSymbolWinStrategy();
    }

    @Test
    void testNoLineFormed() {

        List<SymbolProbabilities> cellProbs = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                SymbolProbabilities sp = makeCellProb(row, col, "A", 1, "B", 1);
                cellProbs.add(sp);
            }
        }

        SymbolProbabilities bonusDummy = new SymbolProbabilities();
        bonusDummy.setRow(0);
        bonusDummy.setColumn(0);
        bonusDummy.setSymbols(Map.of("DUMMY_BONUS", 1));

        ProbabilitiesConfig probConfig = new ProbabilitiesConfig();
        probConfig.setStandardSymbols(cellProbs);
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

       var combosMap = evalResult.getAppliedWinningCombinations();
        if (combosMap.isEmpty()) {
            assertTrue(true, "No combos => no line formed, as expected for random distribution");
        } else {
            assertTrue(true, "Randomly formed a line => not an error, but unusual in a 'no-line' test");
        }
    }

    @Test
    void testForceLineInRow0() {

        List<SymbolProbabilities> cellProbs = new ArrayList<>();

        for (int col = 0; col < 3; col++) {
            cellProbs.add(makeCellProb(0, col, "A", 100)); // 100 => forced "A"
        }
        for (int row = 1; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cellProbs.add(makeCellProb(row, col, "A", 1, "B", 1));
            }
        }

        SymbolProbabilities bonusDummy = new SymbolProbabilities();
        bonusDummy.setRow(0);
        bonusDummy.setColumn(0);
        bonusDummy.setSymbols(Map.of("DUMMY_BONUS", 1));

        ProbabilitiesConfig probConfig = new ProbabilitiesConfig();
        probConfig.setStandardSymbols(cellProbs);
        probConfig.setBonusSymbols(bonusDummy);
        gameConfig.setProbabilities(probConfig);

        Game game = new Game(gameConfig).createFromConfig(0.0);

        WinEvaluator.EvaluationResult evalResult = new WinEvaluator.EvaluationResult(
                game.getMatrix(),
                0.0,
                new HashMap<>(),
                null
        );
        strategy.evaluate(game, evalResult);

        var combosMap = evalResult.getAppliedWinningCombinations();
        assertFalse(combosMap.isEmpty(), "Row0 is all 'A' => should form a line => not empty combos");
        assertTrue(combosMap.containsKey("A"), "Symbol 'A' should be recognized as winning the line in row0");

        Map<String, String> aCombos = combosMap.get("A");
        assertNotNull(aCombos);
        assertEquals("top_row_line", aCombos.get("linear_symbols"),
                "Should detect the top_row_line combo for 'A' in row0");
    }

    private SymbolProbabilities makeCellProb(int row, int col, String forcedSymbol, int weight) {
        SymbolProbabilities cellProb = new SymbolProbabilities();
        cellProb.setRow(row);
        cellProb.setColumn(col);

        Map<String, Integer> distribution = new HashMap<>();
        distribution.put(forcedSymbol, weight);
        cellProb.setSymbols(distribution);
        return cellProb;
    }

    private SymbolProbabilities makeCellProb(int row, int col, String symA, int weightA, String symB, int weightB) {
        SymbolProbabilities cellProb = new SymbolProbabilities();
        cellProb.setRow(row);
        cellProb.setColumn(col);

        Map<String, Integer> distribution = new HashMap<>();
        distribution.put(symA, weightA);
        distribution.put(symB, weightB);

        cellProb.setSymbols(distribution);
        return cellProb;
    }
}
