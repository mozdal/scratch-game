package org.scratchgame.infrastructure.config;

public class SymbolConfig {
    private Double reward_multiplier;
    private String type;
    private Double extra;
    private String impact;

    public Double getRewardMultiplier() { return reward_multiplier; }
    public void setRewardMultiplier(Double reward_multiplier) {
        if(reward_multiplier == null) {
            throw new IllegalArgumentException("Reward multiplier cannot be null");
        }
        this.reward_multiplier = reward_multiplier;
    }

    public String getType() { return type; }
    public void setType(String type) {
        if(type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public Double getExtra() { return extra; }
    public void setExtra(Double extra) { this.extra = extra; }

    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }
}
