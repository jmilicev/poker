# Texas Hold 'Em Poker Game in Java Swing

Welcome to the poker game repository. This project is a simple, yet engaging Texas Hold 'Em poker minigame developed using Java and Swing. The goal of this project is to provide an interactive and fun way for individuals to play poker on their computers. Additionally, this project aims to serve as an educational resource for those interested in learning about game development in Java.

## Table of Contents

- [Getting Started](#getting-started)
- [Game Features](#game-features)
- [How to Play](#how-to-play)
- [Code Overview](#code-overview)
- [License](#license)
- [Development](#development)
- [Contributing](#contributing)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

## Getting Started

To get started with this game, you have two options:

1. **Compile and Run Java Files:** Compile all the Java classes and launch the `Controller.java` file. You can do this in your preferred Java IDE or from the command line.

2. **Run the JAR File:** Alternatively, you can download the standalone executable `poker.jar` file and run it. This does not require compilation of the Java files.

Please ensure that you have the Java Runtime Environment (JRE) installed on your machine before you attempt to run the game.

## Game Features

The Texas Hold 'Em poker game has the following features:

- **Interactive UI:** The game features an interactive user interface developed using Java Swing. It is designed to be user-friendly and intuitive.

- **Game Sounds:** The game includes sounds for a more immersive gaming experience.

- **Deck and Card Management:** The game includes classes to represent a deck of cards (`Deck.java`), and individual cards (`Card.java`).

- **Player and Game Management:** The game includes classes to manage the players (`Player.java`), and the overall game state (`Game.java`).

## How to Play

The game follows the standard rules of Texas Hold 'Em poker. To start the game, compile all the classes and launch `Controller.java` or run the standalone `poker.jar` file. The game interface will guide you through the game process.

## Code Overview

This project consists of several Java classes:

- `Card.java`: This class represents a single poker card. Each card has a suit and a rank.

- `Controller.java`: This class controls the game logic. It communicates between the game and the player classes.

- `Deck.java`: This class represents a deck of cards. It includes methods to shuffle the deck and deal cards.

- `Game.java`: This class manages the game. It handles the game rounds and player actions.

- `Player.java`: This class represents a player in the game. Each player has a hand of cards and a current bet.

- `Sound.java`: This class handles the game sounds.

Additionally, the `recources` folder contains various resources used in the game, and the `poker.jar` file is a standalone executable version of the game.


## Contributing

Contributions to this project are welcome! If you have a feature you'd like to add, or you've discovered a bug, feel free to fork this repository and submit a pull request. There are no specific contribution guidelines at the moment, but please ensure your code is clean, well-documented, and that all tests pass before submitting your pull request.

## Contact

If you have any questions or comments about this project, please open an issue on this GitHub repository. I'm always interested in hearing feedback and suggestions for improvement.

## Acknowledgements

A special thank you to everyone who contributes to this project, watches it, or uses it to learn about Java game development. Your support is greatly appreciated!

## Future Plans

While the game is currently in a playable state, I plan to add more features in the future. Some of these include:

- Multiplayer support: I plan to add network support so that users can play with friends over the internet.
- Improved AI: I plan to improve the AI for the computer players to make the game more challenging.
- Enhanced UI: I plan to enhance the user interface to make the game even more enjoyable.

Stay tuned for these updates and more!

