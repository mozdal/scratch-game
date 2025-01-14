package org.scratchgame.infrastructure.config;

import java.util.List;

public class WinCombinationConfig {
    private double reward_multiplier;
    private String when;
    private int count;
    private String group;
    private List<List<String>> covered_areas;

    public double getRewardMultiplier() { return reward_multiplier; }
    public void setRewardMultiplier(double reward_multiplier) { this.reward_multiplier = reward_multiplier; }

    public String getWhen() { return when; }
    public void setWhen(String when) {
        if(when == null) {
            throw new IllegalArgumentException("When cannot be null");
        }
        this.when = when;
    }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getGroup() { return group; }
    public void setGroup(String group) {
        if(group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }
        this.group = group;
    }

    public List<List<String>> getCoveredAreas() { return covered_areas; }
    public void setCoveredAreas(List<List<String>> covered_areas) { this.covered_areas = covered_areas; }
}
