# Finger-dance-game

How to nstall the application:
<br/>1)Clone the repository.
<br/>2)Open the project in Android Studio
<br/>3)Build the project and run it on your phone

<br/>Steps to start the game:
<br/>1)The game starts with two players. F1 for player 1 and F2 for player 2. F1 is represented by RED color and F2 is represented by BLUE color.
<br/>2)First F1 will be highlighted for player 1.
<br/>3)Then, first player would touch and hold the highlighted F1-tile. After that next random tile would automatically get highlighted for second player F2.
<br/>4)The 2nd player would touch and hold the next highlighted tile F2. After this next F1 title will be highlighted for player 1 again. 
<br/>5)The game goes on until:
  <br/>- any of the player lifts his finger from the previously selected cell.
  <br/>- player clicks on the wrong cell which is not highlited.
<br/>6)If T is the total number of touch allowed by the device at a particular time, then F = T/2 number of touch will be allowed by each player at a particular time.
<br/>7)If each player exceeds F number of touch on the screen at a particular time then the game gets over.

<br/>Code Architecture:
<br/>1)I have used a custom view named 'CellView' that represents each cell in a n\*n board.
<br/>2)For the n\*n board, a grid layout has been used, where each "CellView" is added into it dynamically.
<br/>3)Stored each cell in a CellView array with a position dx and dy of a n\*n matrix grid.
<br/>4)OnTouch of each view, have used a interface OnToggledListener to comunicate with the main thread to update the UI and generate next random row and random column for the next CellView position from the CellView array.
<br/>5)On removal of a touch from a view, have used another interface OnTouchRemove to comunicate with the main UI thread and show messages accordingly.
<br/>6)Each player is a Player Object to keep track of the player movement through each cell. So there will be two player object throughout the game.
Player object contains:
  <br/>-row(current selected cell row postion).
  <br/>-column(current selected cell column position).
  <br/>-view(current view, the player has his fingers on).
  <br/>-count(count of the number of fingers that are touching the screen at a particular time).
  


