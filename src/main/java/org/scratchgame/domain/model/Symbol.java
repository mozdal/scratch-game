package org.scratchgame.domain.model;

import org.scratchgame.domain.model.value.Reward;
import org.scratchgame.infrastructure.config.SymbolConfig;

public class Symbol {
    String name;
    Reward rewardMultiplier;
    SymbolType type;
    Double extra;
    String impact;

    public Symbol() {}

    public Symbol(String name, Reward rewardMultiplier, SymbolType type, Double extra, String impact) {
        validate(name, type);
        this.rewardMultiplier = rewardMultiplier;
        this.type = type;
        this.name = name;
        this.extra = extra;
        this.impact = impact;
    }

    private void validate(String name, SymbolType type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Symbol name cannot be empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Symbol type cannot be empty");
        }
    }

    public Symbol createFromConfig(String name, SymbolConfig sc) {
        Reward rewardMultiplier = sc.getRewardMultiplier() != null ? new Reward(sc.getRewardMultiplier()) : null;
        Symbol.SymbolType type = sc.getType() != null ? Symbol.SymbolType.valueOf(sc.getType().toUpperCase()) : null;
        return new Symbol(name, rewardMultiplier, type, sc.getExtra(), sc.getImpact());
    }

    public enum SymbolType {
        STANDARD,
        BONUS
    }

    public SymbolType getType() {
        return type;
    }

    public Reward getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getName() {
        return name;
    }

    public Double getExtra() {
        return extra;
    }

    public String getImpact() {
        return impact;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}


