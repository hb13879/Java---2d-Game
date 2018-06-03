# 2d-Game

## Summary

I have built a game called 'Coin Hog' using javafx, in which the player has to collect
as many coins as possible in the limited time without hitting into enemies (poisonous mushrooms).
Below is a summary of the different elements involved, with notes about the development process.
The program will be built, tested and run when make is typed into the command line. Nb the time limit
can be set using the STARTTIME parameter in Display.java. It is set as very short by default for
development but can be altered if you wish to play a longer game.

## Basic Grid

Grid.java contains the game state and the key game dynamics. A character representation of the game
grid is stored here. The move function is thoroughly unit tested.

## Basic Graphical Display and Code Structure

The dimensions of the display are parameterised as much as possible, and the java.awt.toolkit library is
used to get screen dimensions for use in these calculations. The game can therefore be resized and should
fit nicely on any screen for any grid size (within reason obviously) by changing the SIZE parameter in Display.java.

Display.java is really the controller class which also takes care of the graphics. Entity.java is a parent class
for the player, coins and enemies. Each entity object takes care of its own position, image and rotation.

## Graphical Features Used

Below is a summary of the graphical elements used.

### Animation

When the player moves, there are animations for both the movement between grid squares and the rotation. This is handled
inside the player class.

### GridPane UI Panels

The sidebar, gameoverpopup, username input form for the leaderboard and the leaderboard itself make up the UI
for the game. They are made up of labels, TextAreas, TextFields and buttons.

### Timer

The timer uses the timer class with keyframes of length 1 second to display the timer to the player.

### Leaderboard

The leaderboard uses a reverse ordered tree map of integers (the score) to a list of strings (the players who
have achieved that score and submitted their name via the username input form) to store the data. This
map, and subsequently the target list, is iterated through in order to create a correctly ordered leaderboard
of length LEADERBOARDLENGTH.

### CSS

I attach a single css stylesheet for all the ui elements in the game.

## Further Development

Further work would include tweaking some of the actual css settings to improve the overall aesthetic. Unfortunately
I ran out of time to do this.
