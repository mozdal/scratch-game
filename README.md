# Scratch Game

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Examples](#examples)
- [Testing](#testing)

## Overview

The **Scratch Game** is a command-line based scratch card simulator developed in Java. It allows users to bet an amount, generate a randomized game matrix based on predefined symbol probabilities, evaluate winning combinations, and apply bonus symbols to calculate rewards. The project follows **Domain-Driven Design (DDD)** principles to ensure a clean, maintainable, and scalable codebase.

## Features

- **Configurable Game Settings**: Define symbols, their types, and probabilities via a JSON configuration file.
- **Dynamic Matrix Generation**: Generates a game matrix based on standard and bonus symbol probabilities.
- **Win Evaluation**: Detects and calculates rewards for various winning combinations.
- **Bonus Symbol Handling**: Applies bonus symbols to modify rewards dynamically.
- **Domain-Driven Design**: Structured architecture aligning with DDD principles for better maintainability.
- **Command-Line Interface**: Simple CLI for interacting with the game.

## Architecture

The project is structured following **Domain-Driven Design (DDD)** principles, divided into the following layers:

- **Domain Layer**: Contains the core business logic, entities, value objects, aggregates, and domain services.
- **Application Layer**: Orchestrates game flow using domain services.
- **Infrastructure Layer**: Handles technical concerns like configuration loading and matrix generation.
- **Presentation Layer**: Manages user interactions via the CLI.

```
src/main/java/
└── com/
    └── scratchgame/
        ├── application/
        │   └── GameService.java
        ├── domain/
        │   ├── model/
        │   │   ├── Game.java
        │   │   ├── Matrix.java
        │   │   ├── Symbol.java
        │   │   ├── WinCombination.java
        │   │   └── value/
        │   │       ├── Position.java
        │   │       └── Reward.java
        │   └── service/
        │       ├── strategy/
        │       │   ├── LinearSymbolWinStrategy.java
        │       │   ├── SameSymbolWinStrategy.java
        │       │   └── WinStrategy.java        
        │       └── WinEvaluator.java
        ├── infrastructure/
        │   └── config/
        │       ├── model/
        │       │   └── SymbolProperties.java
        │       ├── ConfigLoader.java
        │       ├── GameConfig.java
        │       ├── SymbolConfig.java
        │       ├── ProbabilitiesConfig.java
        │       └── WinCombinationConfig.java
        └── presentation/
            └── cli/
                └── Main.java
```

## Prerequisites

- **Java 18** or higher
- **Maven** for dependency management and building the project

## Installation

1**Build the project**

```bash
mvn clean package
```
   
## Configuration

The game relies on a **config.json** file to define symbols, their types, probabilities, and winning combinations. 
config.json file resides on the root directory of the project.

## Usage

After building the project and preparing the config.json, you can run the game via the command line.

### Maven version

> 3.9.9


### Running the game

```bash
java -jar target/scratch-game-1.0.jar --config path/to/config.json --betting-amount <amount>
```
* **Parameters**
  * --config: Path to your **config.json** file
  * --betting-amount: The amount you wish to bet for the game.

### Example run command

```bash
java -jar target/scratch-game-1.0.jar --config config.json --betting-amount 100
```

### Sample output

The game outputs a JSON object containing the generated matrix, the total reward, applied winning combinations, 
and any bonus symbols applied. Additionally, it prints a boxed representation of the matrix for easy visualization.

#### Sample JSON output

```json
{
  "matrix" : [
    [ "A", "B", "C", "10x" ],
    [ "A", "B", "5x", "D" ],
    [ "A", "B", "C", "E" ],
    [ "A", "F", "E", "MISS" ]
  ],
  "reward" : 13500.0,
  "applied_winning_combinations" : {
    "A" : [ "same_symbol_4_times" ],
    "B" : [ "same_symbol_3_times", "same_symbols_vertically" ]
  },
  "applied_bonus_symbol" : "10x"
}
```

#### Sample boxed matrix output

```
+---+---+---+------+
| A | B | C | 10x |
+---+---+---+------+
| A | B | 5x | D |
+---+---+---+------+
| A | B | C | E |
+---+---+---+------+
| A | F | E | MISS |
+---+---+---+------+
```

## Examples

### Example 1: Basic Win

**Configuration Highlights:**
* Bet Amount: 100
* Matrix
```
A B C 10x
A B 5x D
A B C E
A F E MISS
```
* **Winning Combinations:**
  * A appears 4 times (```same_symbol_4_times```)
  * B appears 3 times and forms a vertical line (```same_symbol_3_times``` and ```same_symbols_vertically```)
* **Bonus Symbol:** ```10x``` multiplies the reward by 10

**Reward Calculation:**

* A: 100 * 5 (reward_multiplier) * 1.5 = 750
* B: 100 * 3 * 1 * 2 = 600
* **Total before bonus:** 750 + 600 = 1350
* **After ```10x``` bonus:** 1350 * 10 = 13500

## Testing

The project includes unit tests to ensure the correctness of core functionalities.

### Running Tests

Use Maven to execute all tests:

```bash
mvn test
```



