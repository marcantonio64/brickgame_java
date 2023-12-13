# Brick Game with Java

## Overview
An exercise project for GUIs using tools from the java libraries awt/swing.
The goal is to make a client with some simple games: snake, breakout,
asteroids, and tetris.

The aspect is of a 20x10 grid of fixed `Sprite` objects, which are used
to form `Block` pixels for the construction of each game.

> A `Sprite` is a `javax.swing.JPanel` where an outer empty square containing
  a smaller filled square is drawn.
> A `Block` is a logical entity formed with coordinates that 'lights' a 
  `Sprite` to create an illusion of movement.

A manual with the rules and controls of each game can be found on
`...\brickgame_java\docs\GameManuals.md`. Instructions for adding more games can
be seen on `...\brickgame_java\docs\UserGuide.md`.

## Installation
In the command line, after setting up your directory, download and
play the Brick Game with:

### Windows

```shell
git clone https://github.com/marcantonio64/brickgame_java.git
cd brickgame_java
.\mvnw.cmd clean install
java -jar brickgame.jar
```

### Unix based systems
```shell
git clone https://github.com/marcantonio64/brickgame_java.git
cd brickgame_java
./mvnw clean install
java -jar brickgame.jar
```

Alternatively, if your IDE supports Maven, you can directly
[download](https://github.com/marcantonio64/brickgameJava) the
compressed project, extract it, and then run the
`brickgame_java\src\main\java\com\brickgame\Main.java` file through the IDE.

Keep in mind that changes in the contents of the project do not reflect
immediately into `brickgame.jar`, and you will need to run

```shell
.\mvnw.cmd clean install
```
(Windows), or

```shell
./mvnw clean install
```
(Unix) every time you want to update the `brickgame.jar` file.

## Metadata
Author: marcantonio64

Contact: mafigueiredo08@gmail.com

Date: 11-Dec-2023

License: MIT

Version: OpenJDK1.8