package org.scratchgame.infrastructure.config.model;

import java.util.Map;

public class SymbolProbabilities {

    private Integer column;
    private Integer row;
    private Map<String, Integer> symbols;

    public Integer getColumn() { return column; }
    public void setColumn(Integer column) { this.column = column; }

    public Integer getRow() { return row; }
    public void setRow(Integer row) { this.row = row; }

    public Map<String, Integer> getSymbols() { return symbols; }
    public void setSymbols(Map<String, Integer> symbols) {
        if(symbols == null) {
            throw new IllegalArgumentException("Symbols cannot be null");
        }
        this.symbols = symbols; }
}
