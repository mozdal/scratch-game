package org.scratchgame.domain.model.value;

public class Reward {
    private final double multiplier;

    public Reward(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
