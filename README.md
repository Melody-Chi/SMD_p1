# SWEN30006 Project 1: Pacman Multiverse Edition

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [How to Run](#how-to-run)
- [Design](#design)
  - [Initial Design Analysis](#initial-design-analysis)
  - [New Design: Simple Version](#new-design-simple-version)
  - [Extended Version Design](#extended-version-design)
- [Contributing](#contributing)
- [Acknowledgments](#acknowledgments)
- [License](#license)

## Overview

This project is a reimagined version of the classic Pacman game, bringing in multiverse capabilities, introducing new monsters, and dynamic gameplay changes, like consuming gold and ice to impact monster behavior.

## Prerequisites

- Java JDK (version 11 or later recommended)
- Any IDE that supports Java (e.g., IntelliJ IDEA, Eclipse)

## Installation

1. Clone the repository:
```
git clone https://github.com/your-username/swen30006_project1.git
```

2. Navigate to the project directory:
```
cd swen30006_project1
```

3. Compile the Java files:
```
javac *.java
```

## How to Run

1. To start the game:
```
java Game
```

## Design

### Initial Design Analysis

The initial design of the game was centered around GRASP principles. However, challenges such as lack of polymorphism, bloated classes, and convoluted object creation patterns were identified. The aim of our redesign is to make the game more modular, efficient, and extensible.

### New Design: Simple Version

- **Monsters' Movement**: We abstracted the `Monster` class and created specific child classes (`Troll` and `Tx5`) to manage different monster types, enhancing polymorphism and readability.
  
- **Reusability & Modularity**: Introduced a random walk method in the `Monster` class to prevent code duplication.
  
- **Version Management**: Established an abstract `GameVersion` class to handle different game editions, with `SimpleVersion` managing the basic game mechanics.

### Extended Version Design

- **New Monsters**: Introduced three new monsters (`Alien`, `Wizard`, `Orion`), each bringing unique movement patterns and capabilities.

- **Game Dynamics**: Consumption of `Ice` and `Gold` has been introduced to add tactical depth. These consumables affect monster behavior by freezing or hastening their movement.
  
- **State Management**: Implemented an `enum` to handle various monster states like `Normal`, `Freeze`, and `Furious`.
  
- **Multiverse Extension**: Created the `MultiverseVersion` class, derived from `GameVersion`, to manage this advanced gameplay version.

## Contributing

Contributions are welcome! Please fork the project and submit a pull request with your changes. For substantial modifications, please open an issue first to discuss the proposed changes.

## Acknowledgments

- Project contributors and team members.
- SWEN30006 course staff for their guidance.

## License

This project falls under the MIT License. Please see the `LICENSE` file for more details.
