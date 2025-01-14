package org.scratchgame.domain.model;

import org.scratchgame.infrastructure.config.GameConfig;
import org.scratchgame.infrastructure.config.SymbolConfig;
import org.scratchgame.infrastructure.config.WinCombinationConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    int rows;
    int columns;
    Map<String, Symbol> symbols;

    List<WinCombination> winCombinations;
    Matrix matrix;

    GameConfig gameConfig;


    public Game(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    private Game(
            int rows,
            int columns,
            Map<String, Symbol> symbols,
            List<WinCombination> winCombinations,
            Matrix matrix
    ) {
        this.rows = rows;
        this.columns = columns;
        this.symbols = symbols;
        this.winCombinations = winCombinations;
        this.matrix = matrix;
    }

    public Game createFromConfig(double bonusProbability) {
        rows = gameConfig.getRows();
        columns = gameConfig.getColumns();

        Map<String, Symbol> symbols = new HashMap<>();

        gameConfig.getSymbols().forEach((name, symbolConfig) -> {
            symbols.put(name, new Symbol().createFromConfig(name, symbolConfig));
        });

        List<WinCombination> winCombinations = new ArrayList<>();

        gameConfig.getWinCombinations().forEach((name, wcc) -> {
            winCombinations.add(new WinCombination().createFromConfig(name, wcc));
        });

        Matrix matrix = new Matrix(gameConfig, bonusProbability);

        return new Game(
                gameConfig.getRows(),
                gameConfig.getColumns(),
                symbols,
                winCombinations,
                matrix
        );
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Map<String, Symbol>  getSymbols() {
        return symbols;
    }

    public List<WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public Matrix getMatrix() {
        return matrix;
    }

}
