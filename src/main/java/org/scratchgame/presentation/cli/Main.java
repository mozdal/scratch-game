package org.scratchgame.presentation.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scratchgame.application.GameService;
import org.scratchgame.domain.service.WinEvaluator;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String configPath = null;
        double betAmount = 0.0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config":
                    if (i + 1 < args.length) {
                        configPath = args[++i];
                    }
                    break;
                case "--betting-amount":
                    if (i + 1 < args.length) {
                        betAmount = Double.parseDouble(args[++i]);
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    break;
            }
        }

        if (configPath == null) {
            System.err.println("Error: --config <path> is required.");
            throw new IllegalArgumentException("Config path is required");
        }
        if (betAmount <= 0) {
            System.err.println("Error: --betting-amount must be > 0.");
            throw new IllegalArgumentException("Betting amount must be greater than 0");
        }

        GameService gameService = new GameService();
        WinEvaluator.EvaluationResult result;
        try {
            result = gameService.playGame(configPath, betAmount);
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
            System.exit(1);
            return;
        }

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("matrix", result.getMatrix().getMatrixAsString());
        output.put("reward", result.getReward());
        output.put("applied_winning_combinations", result.getFinalWinningCombinations());
        output.put("applied_bonus_symbol", result.getAppliedBonusSymbol());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
            System.out.println(jsonOutput);
        } catch (IOException e) {
            System.err.println("Failed to generate JSON output: " + e.getMessage());
        }

        System.out.println("\nMatrix (visual):");
        result.getMatrix().printMatrix();
    }


}