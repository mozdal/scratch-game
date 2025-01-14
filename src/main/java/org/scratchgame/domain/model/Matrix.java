package org.scratchgame.domain.model;

import org.scratchgame.domain.model.value.Position;
import org.scratchgame.infrastructure.config.GameConfig;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Matrix {

    private final String[][] matrix;
    private final double bonusProbability;
    public Matrix(GameConfig gameConfig, double bonusProbability) {
        this.bonusProbability = bonusProbability;
        this.matrix = createFromConfig(gameConfig);
    }

    private static final Random random = new Random();

    public String getSymbolNameOfPosition(int row, int column) {
        return this.matrix[row][column];
    }

    public String getMatrixAsString() {
        return Arrays.deepToString(matrix);
    }

    public String[][] createFromConfig(GameConfig gameConfig) {
        int rows = gameConfig.getRows();
        int columns = gameConfig.getColumns();
        String[][] matrix = new String[rows][columns];

        Map<Position, Map<String, Integer>> standardProbabilities = gameConfig.getProbabilities().buildStandardProbabilities(gameConfig);

        Map<String, Integer> bonusSymbols = gameConfig.getProbabilities().getBonusSymbols().getSymbols();

        boolean placeBonus = shouldPlaceBonusSymbol();

        if (placeBonus) {
            setMatrixSymbols(rows, columns, standardProbabilities, matrix);
            String selectedBonusSymbol = pickSymbolBasedOnProbability(bonusSymbols);
            Position bonusPosition = selectRandomPosition(rows, columns);
            matrix[bonusPosition.getRow()][bonusPosition.getColumn()] = selectedBonusSymbol;
        } else {
            setMatrixSymbols(rows, columns, standardProbabilities, matrix);
        }

        return matrix;
    }

    private void setMatrixSymbols(int rows, int columns, Map<Position, Map<String, Integer>> standardProbabilities, String[][] matrix) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Position pos = new Position(row, col);
                Map<String, Integer> cellStandardSymbols = standardProbabilities.get(pos);

                if (cellStandardSymbols == null || cellStandardSymbols.isEmpty()) {
                    cellStandardSymbols = standardProbabilities.get(new Position(0,0));
                }

                String selectedStandardSymbol = pickSymbolBasedOnProbability(cellStandardSymbols);
                matrix[row][col] = selectedStandardSymbol;
            }
        }
    }

    private boolean shouldPlaceBonusSymbol() {
        return random.nextDouble() < bonusProbability;
    }

    private Position selectRandomPosition(int rows, int columns) {
        int row = random.nextInt(rows);
        int col = random.nextInt(columns);
        return new Position(row, col);
    }

    private String pickSymbolBasedOnProbability(Map<String, Integer> symbols) {
        int totalWeight = symbols.values().stream().mapToInt(Integer::intValue).sum();
        int rand = random.nextInt(totalWeight);
        int cumulative = 0;
        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            cumulative += entry.getValue();
            if (rand < cumulative) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unable to pick a symbol based on probabilities.");
    }

    public void printMatrix() {
        if (matrix == null || matrix.length == 0) {
            System.out.println("Matrix is empty.");
            return;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        int[] colWidths = new int[columns];
        for (int col = 0; col < columns; col++) {
            int maxLen = 0;
            for (int row = 0; row < rows; row++) {
                String cell = matrix[row][col];
                if (cell.length() > maxLen) {
                    maxLen = cell.length();
                }
            }
            colWidths[col] = maxLen;
        }

        for (int row = 0; row < rows; row++) {
            printHorizontalSeparator(colWidths);

            for (int col = 0; col < columns; col++) {
                String cell = matrix[row][col];
                System.out.print("| " + padRight(cell, colWidths[col]) + " ");
            }
            System.out.println("|");
        }

        printHorizontalSeparator(colWidths);
    }

    private void printHorizontalSeparator(int[] colWidths) {
        for (int width : colWidths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) { // +2 for padding
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    private String padRight(String s, int length) {
        return String.format("%-" + length + "s", s);
    }
}
