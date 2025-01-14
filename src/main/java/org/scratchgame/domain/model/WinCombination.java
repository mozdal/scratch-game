package org.scratchgame.domain.model;

import org.scratchgame.domain.model.value.Position;
import org.scratchgame.infrastructure.config.WinCombinationConfig;

import java.util.ArrayList;
import java.util.List;

public class WinCombination
{
    String name;

    double rewardMultiplier;
    String when;
    int count;

    String group;

    List<List<Position>> coveredAreas;

    public WinCombination() {}

    private WinCombination(
            String name,
            double rewardMultiplier,
            String when,
            int count,
            String group,
            List<List<Position>> coveredAreas
    ) {
        validate(name, when, group);
        this.name = name;
        this.rewardMultiplier = rewardMultiplier;
        this.when = when;
        this.count = count;
        this.group = group;
        this.coveredAreas = coveredAreas;
    }

    private void validate(String name, String when, String group) {
        if (name == null || when == null || group == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    public WinCombination createFromConfig(String name, WinCombinationConfig wcc) {
        List<List<Position>> coveredAreas = null;

        if (wcc.getCoveredAreas() != null) {
            coveredAreas = new ArrayList<>();
            for (List<String> areaStr : wcc.getCoveredAreas()) {
                List<Position> area = new ArrayList<>();
                for (String posStr : areaStr) {
                    area.add(Position.fromString(posStr));
                }
                coveredAreas.add(area);
            }
        }

        return new WinCombination(
                name,
                wcc.getRewardMultiplier(),
                wcc.getWhen(),
                wcc.getCount(),
                wcc.getGroup(),
                coveredAreas
        );
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getWhen() {
        return when;
    }

    public int getCount() {
        return count;
    }

    public List<List<Position>> getCoveredAreas() {
        return coveredAreas;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }
}