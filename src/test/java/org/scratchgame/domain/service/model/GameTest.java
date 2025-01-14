package org.scratchgame.domain.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scratchgame.domain.model.Game;
import org.scratchgame.domain.model.Matrix;
import org.scratchgame.domain.model.Symbol;
import org.scratchgame.domain.model.WinCombination;
import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.infrastructure.config.ProbabilitiesConfig;
import org.scratchgame.infrastructure.config.SymbolConfig;
import org.scratchgame.infrastructure.config.WinCombinationConfig;
import org.scratchgame.infrastructure.config.model.SymbolProbabilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private GameConfig gameConfig;

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
    }

    @Test
    void testCreateFromConfig_success() {
        Game game = new Game(gameConfig);
        Game createdGame = game.createFromConfig(0.2);

        assertEquals(3, createdGame.getRows(), "Rows should match config (3).");
        assertEquals(3, createdGame.getColumns(), "Columns should match config (3).");

        Map<String, Symbol> symbols = createdGame.getSymbols();
        assertNotNull(symbols, "Symbols map should not be null.");
        assertEquals(2, symbols.size(), "We added 2 symbol configs (A and B).");

        Symbol symA = symbols.get("A");
        assertNotNull(symA, "Symbol A should exist in the map.");
        assertEquals(2.0, symA.getRewardMultiplier().getMultiplier(), 0.001, "Symbol A reward multiplier should be 2.0.");

        Symbol symB = symbols.get("B");
        assertNotNull(symB, "Symbol B should exist in the map.");
        assertEquals(3.0, symB.getRewardMultiplier().getMultiplier(), 0.001, "Symbol B reward multiplier should be 3.0.");

        List<WinCombination> combos = createdGame.getWinCombinations();
        assertNotNull(combos, "Win combinations list should not be null.");
        assertEquals(2, combos.size(), "We added 2 combos (three_symbol, dummy_linear).");

        Matrix matrix = createdGame.getMatrix();
        assertNotNull(matrix, "Matrix should not be null.");
    }

    @Test
    void testCreateFromConfig_noSymbols() {
        gameConfig.setSymbols(new HashMap<>());

        Game game = new Game(gameConfig);

        assertThrows(IllegalStateException.class,() -> game.createFromConfig(0));
    }

    @Test
    void testCreateFromConfig_noWinCombinations() {
        gameConfig.setWinCombinations(new HashMap<>());

        Game game = new Game(gameConfig);
        Game createdGame = game.createFromConfig(0.2);

        assertEquals(3, createdGame.getRows());
        assertEquals(3, createdGame.getColumns());

        List<WinCombination> combos = createdGame.getWinCombinations();
        assertNotNull(combos, "Win combos list should not be null even if empty.");
        assertTrue(combos.isEmpty(), "Win combos should be empty now.");
    }
}

