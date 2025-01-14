package org.scratchgame.infrastructure.config;

import org.scratchgame.domain.model.value.Position;
import org.scratchgame.infrastructure.config.model.SymbolProbabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProbabilitiesConfig {
    private List<SymbolProbabilities> standard_symbols;
    private SymbolProbabilities bonus_symbols;

    public List<SymbolProbabilities> getStandardSymbols() { return standard_symbols; }
    public void setStandardSymbols(List<SymbolProbabilities> standard_symbols) {
        if(standard_symbols == null) {
            throw new IllegalArgumentException("Standard symbols cannot be null");
        }
        this.standard_symbols = standard_symbols;
    }

    public SymbolProbabilities getBonusSymbols() { return bonus_symbols; }
    public void setBonusSymbols(SymbolProbabilities bonus_symbols) {
        if(bonus_symbols == null) {
            throw new IllegalArgumentException("Bonus symbols cannot be null");
        }
        this.bonus_symbols = bonus_symbols;
    }

    public Map<Position, Map<String, Integer>> buildStandardProbabilities(GameConfig config) {
        Map<Position, Map<String, Integer>> standardProbabilities = new HashMap<>();

        List<SymbolProbabilities> standardSymbolsList = this.getStandardSymbols();
        if (standardSymbolsList == null || standardSymbolsList.isEmpty()) {
            throw new IllegalStateException("No standard symbol probabilities defined in configuration.");
        }

        for (SymbolProbabilities cellProb : standardSymbolsList) {
            Position pos = new Position(cellProb.getRow(), cellProb.getColumn());
            Map<String, Integer> symbols = cellProb.getSymbols();

            if (symbols == null || symbols.isEmpty()) {
                throw new IllegalStateException("No symbols defined for cell: " + pos);
            }

            for (String symbol : symbols.keySet()) {
                SymbolConfig symbolConfig = config.getSymbols().get(symbol);
                if (symbolConfig == null || !"standard".equalsIgnoreCase(symbolConfig.getType())) {
                    throw new IllegalStateException("Symbol '" + symbol + "' in cell " + pos + " is not a valid standard symbol.");
                }
            }

            standardProbabilities.put(pos, symbols);
        }

        return standardProbabilities;
    }
}
