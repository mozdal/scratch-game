package org.scratchgame.infrastructure.config;

import java.util.Map;

public class GameConfig {
    private int columns;
    private int rows;
    private Map<String, SymbolConfig> symbols;
    private ProbabilitiesConfig probabilities;
    private Map<String, WinCombinationConfig> win_combinations;

    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }

    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }

    public Map<String, SymbolConfig> getSymbols() { return symbols; }
    public void setSymbols(Map<String, SymbolConfig> symbols) {
        if(symbols == null) {
            throw new IllegalArgumentException("Symbols cannot be null");
        }
        this.symbols = symbols;
    }

    public ProbabilitiesConfig getProbabilities() { return probabilities; }
    public void setProbabilities(ProbabilitiesConfig probabilities) {
        if(probabilities == null) {
            throw new IllegalArgumentException("Probabilities cannot be null");
        }
        this.probabilities = probabilities;
    }

    public Map<String, WinCombinationConfig> getWinCombinations() { return win_combinations; }
    public void setWinCombinations(Map<String, WinCombinationConfig> win_combinations) {
        if(win_combinations == null) {
            throw new IllegalArgumentException("Win combinations cannot be null");
        }
        this.win_combinations = win_combinations;
    }
}
