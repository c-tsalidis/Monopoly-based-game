import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class monopoly_clone extends PApplet {



// Developed by: Christian Tsalidis --> MED1 Medialogy student at Aalborg University, course year 2018-2019

// This is the IP programming course mini-project
// I will develop a Monopoly-like game
// Credits for the concept og the game: Hasbro --> https://monopoly.hasbro.com/

// as the Processing sound library didn't work for me using different methods, I tried out the Minim library, and it worked
// importing the processing sound library 
// import processing.sound.*;
// importing the minim library

// http://code.compartmental.net/tools/minim/quickstart/


// music and sound effects
Minim minim; // i declare the minim variable of type Minim
AudioPlayer song, coinsSound; // i declare the song and coins sound variables of type audioplayer

// the game's different scenes, such as the main menu and the game scene, are executed depending on the state of the game.
// Therefore, if the state is the same as the main menu's state, the main menu scene will be drawn
// whereas if the state is the buy state, the program will run the scene to buy properties
// Lastly, if the state is the play state, it will swith to the game scene (playState)
int state = 0; // the state of the game. By default it's zero
int mainMenuState = 0; // the main menu scene state. It's zero because the first thing that happens when the user starts the program is to be presented with the main menu scene
int buyState = 1; // the buy properties scene state
int playState = 2; // the game scene

// subsates are states that happen at the same time as other states.
// the houses substate runs at the same time as the playState to add houses to the player's owned properties
// the trade substate runs at the same time as the playState as well, to trade other player's properties in exchange for money
int substate = 0; // the substate of the game. By default it's zero
int housesState = 1; // the adding houses to the player's owned properties substate
int tradeState = 2; // the trading money for houses substate

// int waitingForAnswerState = 1;


// declaring the dice number
int dice = 0;

// declaring the number of properties needed to have a Monopoly (the different monopolies are specified afterwards in each property)
int amountOfPropertiesForMonopoly = 2;

// declaring the different properties of the game
int totalAmountOfBlocks = 16; // total number of property blocks
Property [] properties = new Property[totalAmountOfBlocks]; // the properties

// the status of the game is used to inform the players about what's happening while they play
String statusOfGame = "Start buying properties"; // by default it's dispplaying to the players the message to start buying properties, becuase it what the players need to do after they have clicked on the "START" button


// turns
int turn = 1; // the 1st player starts first, so the first turn is his
int [] turns = {1, 2, 3, 4}; // an array of turns with the same dimension as the amount of players, specifying whose turn it is

// the players
int amountOfPlayers = 4; // amount of players
Player [] players = new Player[amountOfPlayers]; // declaring the players with the Player class and the amount of players


// the images of the game
PImage mainMenuImage; // main menu image
PImage boardImage; // board image

// the different buttons of the game
Button buyButton; // buy button to change the state to the buy scene state. Used to start buying and switch to the buy state. Shown in the main menu scene with the text: "START"
Button playButton; // play button to change the state of the game to the play game scene. shown in the buying properties scene. Used to start playing the game once the players have bought their properties
Button diceButton; // roll the dice button shown in the game scene
Button tradeButton; // trade button to change the substate of the game to the trading substate. It's a toggle button. If the player wants to trade, he clicks on it and starts trading, but if he wants to stop trading he clicks on it again. it's like an on/off switch
Button increasePropertyTradePriceButton, decreasePropertyTradePriceButton; // increase and decrease property trade price buttons. They are used to raise and decrease the amount of money that the player is willing to trade another player's property for
Button acceptTradeButton; // accept trade button. If both players agree on a trade, they click here to accept the trade
Button addHousesButton; // add hotels button to change the substate to add hotels state
Button [] propertiesButtons = new Button[totalAmountOfBlocks]; // the properties buttons. Each property has a button, used for different purposes depending on the state and substate of the game.
Button nextTurnButton; // next turn button shown, used to develop the game. Only used for development purposes


public void setup() // only runs once at the start of the program
{
   // size of the program's window


  // The music
  minim = new Minim(this);
  song = minim.loadFile("data/Midnight_Special.mp3");
  coinsSound = minim.loadFile("data/Coins_Shuffling.wav");
  song.loop(); // a loop is used so as to repeat the song from scratch once the song finishes playing. How it's used:  http://code.compartmental.net/minim-beta/javadoc/ddf/minim/AudioPlayer.html#loop()
  // trying it with the processing sound library
  // music = new SoundFile(this, "data/music.wav");
  // music.play();


  // the different images used in the game
  mainMenuImage = loadImage("data/main-menu-image.png"); // main menu image
  boardImage = loadImage("data/board.jpg"); // the board image

  // the different buttons of the game
  buyButton = new Button(width / 2 - 100, height / 2 - 100, 200, 200, "START"); // buy properties button
  playButton = new Button(width / 2 - 100, 275, 200, 75, "PLAY"); // play button
  diceButton = new Button(600, 700, 200, 75, "ROLL THE DICE"); // roll the dice button
  nextTurnButton = new Button(width / 2 - 100, 300, 200, 100, "NEXT TURN"); // next turn button --> only used when developing the game
  addHousesButton = new Button(200, 700, 200, 75, "BUY HOUSES"); // add houses button
  // trade buttons
  tradeButton = new Button(width / 2 - 100, 700, 200, 75, "TRADE"); // trade button
  acceptTradeButton = new Button(width / 2 - 100, 375, 200, 75, "ACCEPT TRADE"); // accept trade button
  increasePropertyTradePriceButton = new Button(200, 700, 200, 75, "RAISE"); // increase property trade price
  decreasePropertyTradePriceButton = new Button(600, 700, 200, 75, "DECREASE"); // decrease property trade price

  // the properties
  // Property class has the following parameters: (x coordinate as int, y coordinate as int, name as string, price of the property as float, number of property as int, monopoly number as int)
  // the main properties
  properties[1] = new Property(600, 800, "BARCELONA", 20.0f, 1, 1); // Property 1 (P1)
  properties[3] = new Property(200, 800, "MADRID", 50.0f, 3, 1); // Property 2 (P2)
  properties[5] = new Property(0, 600, "AALBORG", 70.0f, 5, 2); // Property 3 (P3)
  properties[7] = new Property(0, 200, "COPENHAGEN", 100.0f, 7, 2); // Property 4 (P4)
  properties[9] = new Property(200, 0, "BERLIN", 125.0f, 9, 3); // Property 5 (P5)
  properties[11] = new Property(600, 0, "FRANKFURT", 150.0f, 11, 3); // Property 6 (P6)
  properties[13] = new Property(800, 200, "LYON", 175.0f, 13, 4); // Property 7 (P7)
  properties[15] = new Property(800, 600, "PARIS", 200.0f, 15, 4); // Property 8 (P8)

  // declaring and initializing the transport blocks
  properties[2] = new Property(400, 800, "TRANSPORT 1", 100.0f, 2, 5); // t1
  properties[10] = new Property(400, 0, "TRASNPORT 2", 100.0f, 10, 5); // t2

  // declaring and initializing the maintenance blocks
  properties[6] = new Property(0, 400, "WATER", 60.0f, 6, 6); // q1
  properties[14] = new Property(800, 400, "ELECTRICITY", 60.0f, 14, 6); // q2

  // declaring and initializing the other blocks
  properties[0] = new Property(800, 800, "PARK", 0.0f, 0, 0); // GO
  properties[4] = new Property(0, 800, "MUSEUM", 0.0f, 4, 0); // jail
  properties[8] = new Property(0, 0, "GYM", 0.0f, 8, 0); // lottery
  properties[12] = new Property(800, 0, "LIBRARY", 0.0f, 12, 0); // go to jail

  // the properties buttons
  for (int i = 0; i < propertiesButtons.length; i++) // it goes through all the properties
  {
    Property p = properties[i];
    propertiesButtons[i] = new Button(p.x, p.y, p.propertyWidth, p.propertyHeight, ""); // creates a button for the property with the current value of i 
  }

  // the players
  int playersX = 300; // the player's x coordinate
  int playersY = 600; // the player's y coordinate
  // the Player class has the following parameters: (name of player text as String, player number as int, color of the player as string, x coordinate of the player's info panel, y coordinate of the player's info panel, player's x coordinate as int, player's y coordinate as int)
  players[0] = new Player("NIKOLAS", 1, "red", playersX, playersY, 50, 75); // player 1
  players[1] = new Player("CHRISTIAN", 2, "green", playersX + 125, playersY, 100, 75); // player 2
  players[2] = new Player("IRENE", 3, "blue", playersX + 250, playersY, 50, 100); // player 3
  players[3] = new Player("DIMITRI", 4, "yellow", playersX + 375, playersY, 100, 100); // player 4
}

public void draw()
{ 
  // println("X: " + mouseX + "  |  Y: " + mouseY); // used for developing. Comes in handy when placing things in the coordinates I want 
  fill(200); // sets the color to fill shapes -->   fill(gray) --> float number specifying the value between white and black. In this case, 200
  textSize(20); // sets the size of the text --> 20
  imageMode(CENTER); // sets the position of images to center mode --> sets second and third parameters of image to the image's center point
  showScene(); // function to show the scene
}

public void showScene()
{
  if (state == mainMenuState)
  {
    mainMenu(); // runs main menu scene
  } else if (state == buyState)
  {
    buyMenu(); // runs the buying properties scene
  } else if (state == playState)
  {
    play(); // runs the playing the game scene
  }
  if (substate == tradeState)
  {
    trade(); // runs the trading substate
  }
}

public void checkTurn() // checks whose turn it is
{
  for (int i = 0; i < turns.length; i++) // goes through all the players
  {
    if (turn == turns[i]) // if the turn is the player's value of i turn, it checks the counter of the player
    {
      checkCounter(players[i]); // because the turns and players indexes are in the same order
      // println(players[i].name + "'s turn");
    }
  }
  // this is what's happening in every i value of the for loop:
  /*
  if(turn == turns[0]) // if it's the player 1's turn
   {
   checkCounter(players[0]); // checks the counter of player 1
   // players[0].move(); // check the position of player 1
   }
   else if(turn == turns[1])
   {
   checkCounter(players[1]);
   }
   [...]
   */
}


public void checkCounter(Player player) // checks the counter of the player
{
  player.checkPosition(); // checks the position of the player
  player.move(); // moves the player depending on his position
  checkPropertyInfo(); // checks the info of the property
  // nextTurnButton.display(); // used for developing purposes
}

public void changeTurn()
{ 
  if (turn == turns[0]) // if it's the 1st player's turn and he moved, it means he already finished, so now it's the 2nd player's turn
  {
    if (players[1].isBankrupt == false) // if the second player is not bankrupt it's his turn
    {
      turn = turns[1]; // changes the turn to the second player's turn
      // println(players[1].name + " 's turn");
      // text(players[1].name + " 's turn", width  / 2 - 200, 250);
    } else // if player 2 is bankrupt, it's the third player's turn
    {
      turn = turns[2]; // changes the turn to the third player
      // statusOfGame = (players[0].name + " has won the game!!");
      // turn = turns[2];
    }
  } else if (turn == turns[1]) // if it's the second player's turn
  {
    if (players[2].isBankrupt == false) // if player 2 is not bankrupt, the next turn is his
      // if(players[0].isBankrupt == false)
    {
      turn = turns[2]; // changes the turn to the third player
      // turn = turns[0]; // third player's turn
      // println(players[2].name + " 's turn");
    } else // if the player 3 is bankrupt, it's the fourth player's turn
    {
      // statusOfGame = (players[1].name + " has won the game!!");
      turn = turns[3]; // 4th player's turn
      // turn = turns[2];
    }
  } else if (turn == turns[2]) // if it's player 3's turn
  {
    if (players[3].isBankrupt == false) turn = turns[3]; // if player 4 is not bankrupt, it's his turn
    // println(players[3].name + " 's turn");
    else turn = turns[0]; // if player 4 is bankrupt, it's player 1's turn
  } else if (turn == turns[3]) // if it's player 4's turn
  {
    if (players[0].isBankrupt == false) turn = turns[0]; // if player 1 is not bankrupt, it's his turn
    // println(players[0].name + " 's turn");
    else turn = turns[1]; // if player 1 is bankrupt then it's player 2's turn
  }

  // checks if there is a winner
  int losers = 0; // amount of losers
  for (int i = 0; i < players.length; i++)
  {
    // Player winner = players[i];
    if (players[i].isLoser == true) // if the player is a loser
    {
      losers += 1; // increase the counter by 1
    }
    if (players[i].isLoser == false && losers == (amountOfPlayers - 1)) // if the player is not a loser and the amount of losers is the same as the amount of players minus one, it means that this player is the winner
    {
      println(players[i].name + " has won the game!! Congratulations!!"); // print out that this player is the winner
      statusOfGame = (players[i].name + " has won the game!! Congratulations!!"); // changes the status text displaying who the winner is
    }
  }
}


public void checkPropertyInfo() // checks who owns the property...
{
  // to see if the player has to pay rent when he lands on a property that's not his own
  for (int i = 0; i < turns.length; i++) // for every turn
  {
    Player player = players[i]; // create a player refering to the player in the players array with an index of the current value of i
    if (turn == turns[i]) // as turns[] and players[] have the same length, this statement means that if the turn is of the current player's
    {
      for (int j = 0; j < properties.length; j++) // go through all of the properties
      {
        Property property = properties[j]; // create a property of the properties array referring to the property of the properties array with an index value of i
        if (player.position == property.boardPosition) // if the player's position is the same as the property's board position
        {
          if (property.ownedByPlayer != player.number) // if the player who owns the property is not the same as the player who landed on the property
          {
            property.rent(player); // the player has to pay rent to the property's rightful owner
          }
        }
      }
    }
  }


  // if a property has the variable ownedByPlayer number the same as the player's number (player 1 || 2 || 3 || 4) 1, it means that it is owned by player 1
  // if we go through all the properties, if properties[i].ownedByPlayer == 1 --> player[0].owned[i] == true
  // and always set the non-property blocks ownedByPlayer to 0
  for (int i = 0; i < properties.length; i++)
  {
    Property property = properties[i];
    for (int j = 0; j < players.length; j++)
    {
      Player player = players[j];
      if (property.ownedByPlayer == player.number) // if the property is owned by the player
      {
        player.propertiesOwned[i] = true; // the player's boolean to check if the player owns the property is true
        player.propertiesOwnedNames[i] = property.name;
      if(player.isBankrupt == true)
      {
        property.noLongerOwned();    
      }
      }
    }
    /*
    // this is what happens inside the for loop with every value of j
     if(property.ownedByPlayer == players[0].number)
     {
     players[0].propertiesOwned[i] = true;
     players[0].propertiesOwnedNames[i] = property.name;
     }
     else if(property.ownedByPlayer == players[1].number)
     {
     players[1].propertiesOwned[i] = true;
     players[1].propertiesOwnedNames[i] = property.name;
     }
     [...]
     */
  }

  // show each player's owned properties
  for (int j = 0; j < players.length; j++)
  {
    players[j].showProperties();
  }
}


public void mainMenu() // main menu scene
{
  image(mainMenuImage, width / 2, height / 2, width, height); // displays the image of the main menu
  buyButton.display(); // displays the buy properties button to start the buying properties
}

public void buyMenu() // buying properties menu
{
  image(boardImage, width / 2, height / 2, width, height); // displays the image of the board
  playButton.display(); // displays the play button
  // nextTurnButton.display(); // only used for development purposes
  
  // show the players on the screen on their respective positions
  for (int i = 0; i < players.length; i++)
  {
    players[i].display(properties[players[i].position]);
  }

  // displays the properties
  for (int i=0; i < properties.length; i++)
  {
    properties[i].display();
  }

  showPlayerTurn(); // shows the player's turn on screen
  checkMoney(); // checks the amount of money of the players
  text(statusOfGame, width / 2, 250); // displays the statue of the game
}

public void play() // play game state
{
  image(boardImage, width / 2, height / 2, 1000, 1000); // displays the board's image
  diceButton.display(); // displays the roll the dice button on screen
  // nextTurnButton.display(); // used for development purposes
  addHousesButton.display(); // displays the add houses button
  tradeButton.display(); // displays the trade button

  // shows the properties
  for (int i=0; i < properties.length; i++)
  {
    fill(100); // sets the color used to fill shapes to 100 --> gray color (difference between black and white)
    properties[i].display(); // displays the properties
  }
  // shows the players in their respective positions
  for (int i = 0; i < players.length; i++)
  {
    players[i].display(properties[players[i].position]);
  }

  showPlayerTurn(); // shows the player's turn on screen
  checkMoney(); // checks for the player's money

  if (substate == housesState) // if the substate is the same as the houses substate
  {
    for (int i = 0; i < properties.length; i++) // goes through all the properties
    {
      if (isInHitbox(properties[i])) // checks if the mouse is inside the properties hitboxes
      {
        properties[i].showPricePerHouse(); // shoes the price per house on screen
      }
    }
  }

  // show dice number in screen
  fill(0); // sets the color used to fill shapes to black
  text("Dice: " + dice, width / 2, 350); // displays the value of the dice on screen

  // show the status of the player: who has paid what to who?...
  text(statusOfGame, width / 2, 250);
 
}

public void trade() // trade substate
{
  increasePropertyTradePriceButton.display(); // displays the button to increase the value of the property
  decreasePropertyTradePriceButton.display(); // displays the button to decrease the value of the property
  acceptTradeButton.display();

  fill(0); // sets the color used to fill shapes to black
  text(statusOfGame, width / 2, 250); // shows the status of the game on screen
}

public void rollDice() // it rolls the dice --> gives a random number between 1 and 6
{
  dice = PApplet.parseInt(random(1, 7)); // up to 7 because it's not included
  // println("player " + turn + ": " + dice);
  checkTurn(); // checks the player's turn
  changeTurn(); // changes the player's turn
}

public void showPlayerTurn()
{
  // show the current player's turn
  for (int i = 0; i < turns.length; i++)
  {
    Player player = players[i];
    if (turn == turns[i])
    {
      fill(0);
      text(player.name + "'s turn", width / 2, 225);
      // statusOfGame = (player.name + "'s turn");
    }
  }
}

public void checkMoney() // check money to see if anyone is bankrupt
{

  for (int i = 0; i < players.length; i++)
  {
    /*
    if(players[i].money < 0)
     {
     players[i].money = 0;
     }
     */
  }
}


/*
  The mouseClicked() function is called after a mouse button has been pressed and then released. 

Mouse and keyboard events only work when a program has draw(). Without draw(), the code is only run once and then stops listening for events.
*/

public void mouseClicked()
{
  // if the mouse is clicked inside of the buttons hitboxes and the state of the game is the same as the 2nd parameter state
  // play button
  if (isInHitbox(playButton, buyState) && substate != tradeState) // if the mouse is clicked inside of the play button's hitbox and the game's state is the buyState and the substate is not the trade substate 
    // if (mouseX >= playButton.x && mouseX <= (playButton.x + playButton.buttonWidth) && mouseY >= playButton.y && mouseY <= (playButton.y + playButton.buttonHeight) && (state == mainMenuState || state == buyState))
  {
    playButton.isPressed = true;
    println("play button clicked");
    state = playState; // change the state of the game to the play state
  }
  // roll the dice button
  else
    if (isInHitbox(diceButton, playState) && substate != tradeState)
      // if (mouseX >= diceButton.x && mouseX <= (diceButton.x + diceButton.buttonWidth) && mouseY >= diceButton.y && mouseY <= (diceButton.y + diceButton.buttonHeight) && state == playState)
    {
      diceButton.isPressed = true;
      println("Roll the dice button clicked");
      rollDice(); // roll the dice function
    }
  // next turn button
    else
      if (isInHitbox(nextTurnButton, playState) && substate != tradeState)
        //if (mouseX >= nextTurnButton.x && mouseX <= (nextTurnButton.x + nextTurnButton.buttonWidth) && mouseY >= nextTurnButton.y && mouseY <= (nextTurnButton.y + nextTurnButton.buttonHeight) && state == playState)
      {
        nextTurnButton.isPressed = true;
        println("Next turn button pressed");
        println("--------------------------------------------------");
        changeTurn(); // change the turn of the player
      } 



  // trade substate
  for (int i = 0; i < turns.length; i++) // goes through all the turns
  {
    if (turn == turns[i]) // if the turn is the current player's turn
    {
      Player player = players[i];
      // trade button
      if (isInHitbox(tradeButton, playState))
      {
        tradeButton.isPressed = true;
        println("Trade button pressed");
        if (substate != tradeState) // if the substate is not the trading substate
        {
          substate = tradeState; // change the substate to the trading substate
          println("Substate is now tradeState. --> " + substate);
        } else if (substate == tradeState) // if the substate is the trading substate
        {
          int defaultSubstate = 0;
          substate = defaultSubstate; // chage the substate to the default substate (which is zero)
          println("Substate is now default substate. --> " + substate);
          player.tradePropertyPrice = 0; // change the player's property trade price to zero
        }
      }
      if (isInHitbox(increasePropertyTradePriceButton, playState) && substate == tradeState)
      {
        increasePropertyTradePriceButton.isPressed = true;
        // println("Increase property trade price button pressed");
        int increasePropertyTradePriceValue = 100;
        println(player.name + " Increased " + player.tradePropertyName + " price by " + increasePropertyTradePriceValue + "€. Now the trade price is " + player.tradePropertyPrice + "€");
        player.tradePropertyPrice += (increasePropertyTradePriceValue);
        statusOfGame = (player.name + " wants to trade " + player.tradePropertyName + " for " + player.tradePropertyPrice + "€");
      }
      if (isInHitbox(decreasePropertyTradePriceButton, playState) && substate == tradeState)
      {
        decreasePropertyTradePriceButton.isPressed = true;
        // println("Decrease property trade price button pressed");
        int decreasePropertyTradePriceValue = 100;
        println(player.name + " Decreased " + player.tradePropertyName + " price by " + decreasePropertyTradePriceValue + "€. Now the trade price is " + player.tradePropertyPrice + "€");
        player.tradePropertyPrice -= (decreasePropertyTradePriceValue);
        statusOfGame = (player.name + " wants to trade " + player.tradePropertyName + " for " + player.tradePropertyPrice + "€");
      }
      if (isInHitbox(acceptTradeButton, playState) && substate == tradeState)
      {
        acceptTradeButton.isPressed = true;
        println("Accept trade button pressed");
        player.tradeAccepted(); // the players have accepted the trade
      }
    }
  }

  // buy properties button
  // else
  if (isInHitbox(buyButton, mainMenuState))
    // if (mouseX >= buyButton.x && mouseX <= (buyButton.x + buyButton.buttonWidth) && mouseY >= buyButton.y && mouseY <= (buyButton.y + buyButton.buttonHeight) && state == mainMenuState)
  {
    buyButton.isPressed = true;
    println("Buy button pressed");
    state = buyState; // change the state to the buy properties state
  }
  // add houses button
  else
    if (isInHitbox(addHousesButton, playState) && substate != tradeState)
      // if (mouseX >= addHousesButton.x && mouseX <= (addHousesButton.x + addHousesButton.buttonWidth) && mouseY >= addHousesButton.y && mouseY <= (addHousesButton.y + addHousesButton.buttonHeight) && (state == playState || state == buyState))
    {
      addHousesButton.isPressed = true;
      println("Buy button pressed");
      if (substate == housesState) // if the substate is the same as the adding houses substate
      {
        int defaultSubstate = 0;
        substate = defaultSubstate;
        println("substate is now " + substate);
      } else // if the substate is not the same as the addung houses substate
      {
        substate = housesState; // change the substate to the adding houses substate
      }
      println("substate is now houses state --> " + substate);
    }


  // properties buttons to buy them
  for (int i = 0; i < propertiesButtons.length; i++) // goes through all the properties buttons
    if (isInHitbox(properties[i])) // if the mouse is clicked inside of the property's hitbox
      // if (mouseX >= propertiesButtons[i].x && mouseX <= (propertiesButtons[i].x + propertiesButtons[i].buttonWidth) && mouseY >= propertiesButtons[i].y && mouseY <= (propertiesButtons[i].y + propertiesButtons[i].buttonHeight))
    {
      if (state == buyState) // if teh state of the game is the same as the buying properties state
      {
        propertiesButtons[i].isPressed = true;
        println(properties[i].name + " buy button pressed");

        for (int j = 0; j < turns.length; j++) // goes through all the turns
        {
          int ownedByBank = 0;
          if (turn == turns[j] && properties[i].ownedByPlayer == ownedByBank) // if it's the current player's turn and the property is owned by the bank
          {
            players[j].buy(properties[i]); // the player buys the property
            changeTurn(); // changes the turn of the player
          }
        }
      }
      // to add houses in the properties
      else if ((state == playState || state == buyState) && substate == housesState)
      {
        for (int j = 0; j < players.length; j++) // goes through all the players
        {
          Player player = players[j];
          if (turn == turns[j]) // if it's the current player's turn
          {
            if (player.money >= properties[i].housePrice && properties[i].ownedByPlayer == player.number) // if the player's money is higher than the property's house price and the property is owned by the player
            {
              player.addHouse(properties[i]); // the player wants to add a house in that property
            } else // if the player doesn't have enough money or if the property is not owned by the player
            {
              println(player.name + " does not have enough money to add a house in " + properties[i].name);
              statusOfGame = (player.name + " does not have enough money to add a house in " + properties[i].name); // update the status of the game
            }
          }
        }
      }
      // if the player is trading properties
      // and the player who wnats to trade pffers more money than the onw he wants to trade from
      else if ((state == playState) && substate == tradeState)
      {
        for (int j = 0; j < players.length; j++) // goes through all the players
        {
          Player player = players[j];
          int ownedByBank = 0;
          if (turn == turns[j] && properties[i].ownedByPlayer != ownedByBank) // if it's the current player's turn and the propert is not owned by the bank
          {
            // println(player.name + " wants to trade " + properties[i].name + " for " + player.tradePropertyPrice + "€ from player " + properties[i].ownedByPlayer);
            // player.tradePropertyPrice = (properties[i].tradePrice);
            player.propertyToTrade = properties[i]; // the player wants to trade this property
            player.tradePropertyName = properties[i].name; // the player's trade property name is this property's name
            player.trade(properties[i]); // the player wants to trade this property
          }
        }
      }
    }
}


// the function isInHitbox has something called overloading. this means that depending on the parameters received when the function is called, it will run one of both
public boolean isInHitbox(Property property) // if the mouse is inside of the property's hitbox
{
  if (mouseX >= property.x && mouseX <= (property.x + property.propertyWidth) && mouseY >= property.y && mouseY <= (property.y + property.propertyHeight))
  {
    return true; // the mouse is inside of the hitbox
  } else
    return false; // the mouse is not inside of the hitbox
}

public boolean isInHitbox(Button button, int currentState) // if the mouse is inside of the button's hitbox and the game is in a specific state
{
  if (mouseX >= button.x && mouseX <= (button.x + button.buttonWidth) && mouseY >= button.y && mouseY <= (button.y + button.buttonHeight) && (state == currentState))
  {
    return true; // the mouse is inside of the hitbox
  } else
    return false; // the mouse is not inside of the hitbox
}
class Button
{
  // Variables of the button
  int x; // x coordinate of the button
  int y; // y coordinate of the button
  int buttonWidth; // width of the button
  int buttonHeight; // height of the button
  String text; // the button's text
  boolean isPressed = false; // to check if the button is pressed or not


  // Constructor of the Button
  Button(int temp_x, int temp_y, int temp_buttonWidth, int temp_buttonHeight, String temp_text)
  {
    x = temp_x;
    y = temp_y;
    buttonWidth = temp_buttonWidth;
    buttonHeight = temp_buttonHeight;
    text = temp_text;
  }

  public void display() // display the button
  {
    fill(100); // give the button a grey color
    rect(x, y, buttonWidth, buttonHeight); // draw the button's rectangle
    fill(200); // paint the text of the button in an almost white color
    textAlign(CENTER); // align the text in the center
    // button text
    int textX = x + (buttonWidth / 2);
    int textY = y + (buttonHeight / 2); 
    text(text, textX, textY);
  }
}
class Player
{
  // variables
  String name; // player name
  int number; // player number (player 1, player 2 ...)
  int counter = 0; // the index number of the GO property block
  int x; // x coordinate of the player
  int y; // y coordinate of the player
  int playerColor; // color of the player
  int position = 0; // by default the players position is 0, which is the GO block position. Each property has a position, and the player heads over to that position depending on the number of the dice
  float money = 1500; // the starting amount of money of the players
  int positionInPropertiesX; // the x position they have inside the properties so that all the players don't get drawn on top of the other
  int positionInPropertiesY; // the y position they have inside the properties so that all the players don't get drawn on top of the other
  float moneyGained = 0; // total amount of money gained in the game
  float moneySpent = 0; // total amount of money spent in the game
  float revenue; // total revenue of the player in the game
  float moneyPaid = 0; // total amount of money paid to other players in the game
  float tradePropertyPrice; // the player's trading property price 
  String tradePropertyName; // the name of the property the player wants to trade for money
  Property propertyToTrade; // property that the player wants to trade
  boolean isBankrupt; // if the player is bankrupt he won't have any more turns

  boolean isLoser; // to check if the player is a loser


  // info panel
  int infoPanelX; // x coordinate of the info panel
  int infoPanelY; // y coordinate of the info panel
  int infoPanelWidth = 125;  // width of the info panel
  int infoPanelHeight = 100;  // height of the info panel
  // Property [] propertiesOwned = new Property[10]; // the properties owned by the player. The total amount of properties is 16
  boolean [] propertiesOwned = new boolean[totalAmountOfBlocks]; // by default booleans are false
  String [] propertiesOwnedNames = new String[totalAmountOfBlocks];

  // booleans to check if the player is one of the special blocks
  // boolean isInGo, isInLottery, isInJail, isInGoToJail;
  // boolean [] isInQuestion = new boolean[2];

  // constructor
  Player(String temp_name, int temp_number, String temp_color_text, int temp_infoPanelX, int temp_infoPanelY, int temp_position_in_properties_x, int temp_position_in_properties_y)
  {
    name = temp_name; // name of the player
    number = temp_number; // number of the player
    if (temp_color_text == "red") // if the string is red
    {
      playerColor = color(255, 0, 0); // player color is red
    } else if (temp_color_text == "green") // if string is green
    {
      playerColor = color(50, 205, 50); // player color is green
    } else if (temp_color_text == "blue") // if string is blue
    {
      playerColor = color(0, 0, 255); // player color is blue
    } else if (temp_color_text == "yellow") // if string is yellow
    {
      playerColor = color(255, 215, 0); // player color is yellow
    }
    infoPanelX = temp_infoPanelX;
    infoPanelY = temp_infoPanelY;

    positionInPropertiesX = temp_position_in_properties_x;
    positionInPropertiesY = temp_position_in_properties_y;
  }

  public void display(Property property) // displays the player in the screen
  {
    fill(playerColor); // paint the player with its corresponding color
    // player's avatar
    int avatarX = property.x + positionInPropertiesX;
    int avatarY = property.y + positionInPropertiesY;
    int avatarWidth = 20;
    int avatarHeight = 20;
    rect(avatarX, avatarY, avatarWidth, avatarHeight);
    // text(name, property.x + 100, property.y + 100);
    revenue = moneyGained - moneySpent; // revenue of the player is the same as total money gained - total money spent
    // text(name + ": " + money + "€" + "\nGained: " + moneyGained + "€" + "\nPaid: " + moneyPaid + "€", infoPanelX, infoPanelY);
    
    // tne player's info panel
    text(name + "" + "\n" + money + "€", infoPanelX, infoPanelY); // text that displays the player's money inside the player's info panel
    int infoPanelTransparency = 50; // transparency level of the info panel
    fill(playerColor, infoPanelTransparency); // paints the info panel of the player's color with a transparency level
    int infoPanelRectX = infoPanelX - (infoPanelWidth / 2);
    int infoPanelRectY = infoPanelY - (infoPanelHeight / 2);
    rect(infoPanelRectX, infoPanelRectY, infoPanelWidth, infoPanelHeight); // draws the info panel rectangle
    // strokeWeight(40);
    // line(infoPanelX, infoPanelY, infoPanelX, (infoPanelY - money));

    // money bars
    if (substate != tradeState) // if the substate of the game is not the trading substate
    {
      int moneyBarsX = infoPanelX;
      int moneyBarsY = infoPanelY - (infoPanelHeight / 2);
      int moneyBarsWidth = 20;
      float moneyBarsHeight = (-money / 10);
      rect(moneyBarsX, moneyBarsY, moneyBarsWidth, moneyBarsHeight); // draws the player's money bar on top of the payer's info panel
    }
    // money bar shown when the user is trading
    else if (substate == tradeState) // if the substate of the game is the trading substate
    {
      int tradeMoneyBarsX = infoPanelX;
      int tradeMoneyBarsY = infoPanelY - (infoPanelHeight / 2);
      int tradeMoneyBarsWidth = 20;
      float tradeMoneyBarsHeight = (-tradePropertyPrice / 10);
      rect(tradeMoneyBarsX, tradeMoneyBarsY, tradeMoneyBarsWidth, tradeMoneyBarsHeight); // draws the trading money bar on top of the player's info panel
    }
  }

  public void move() // moves the player to the correct place
  {
    // As the properties index are in order, the player's x and y are the same as the x and y coordinates of the property's position
    x = properties[position].x; // move the player to the property's x position
    y = properties[position].y; // move the player to the property's y position
    // println("Player " + turn + " is in "+ "property: " + properties[position].name);
  }

  public void checkPosition() // updates and checks the position of the player depending on the dice number and its previous position
  {
    counter += dice; // adds the dice value to the player's counter
    position += counter; // the player's position is his last position plus the counter
    if (position >= totalAmountOfBlocks) // if the player's position is bigger than or the same as the total amount of blocks it means that the player has gone through the entire board
    {
      position = position - totalAmountOfBlocks; // so that it doesn't go to zero, but to the differnece between them
    }
    counter = 0; // reset the players' counter to zero
    // println("Counter: " + counter + "  |  Position: " + position);
  }

  public void buy(Property property) // the player buys the property he chose
  {
    money -= property.buyPrice; // the player's money is decreased by the property's buy price
    property.ownedByPlayer = number; // the property is assigned as owned by the player number
    // if the player has option to buy more properties, he will. That means that if the properties owned by player array is not still full, it has null

    // println(name + " has bought " + property.name + " for " + property.buyPrice + "€");
    statusOfGame = (name + " has bought " + property.name + " for " + property.buyPrice + "€"); // updates the status of the game
    moneySpent += property.buyPrice; // the total money spent by the player is increased by the value of the property's buy price
    // println(name + "has spent a total of " + moneySpent + "€");
    // fill(0, 0, 200, 50); // paint it blue with a transparency of 50
    // rect(infoPanelX, infoPanelY, 200, 50);

    coinsSound.play(0); // play the coin sound
  }

  /*
  void rent(Property property, Player playerWhoGetsPaid)
   {
   println("Number of houses in " + property.name + ": " + property.houseCounter);
   // 
   // if(property.houseCounter == 0)
   // {
   //   money -= property.rentPrice;
   //   playerWhoGetsPaid.money += property.rentPrice;
   //   println(name + " has paid " + property.rentPrice + "€ to " + playerWhoGetsPaid.name + " in " + property.name);
   //   fill(200, 0, 0, 50);
   //   rect(infoPanelX, infoPanelY, 200, 50);
   // }
   
   // else if(property.houseCounter >= 0)
   // {
   println("");
   money -= (property.houseRent * property.houseCounter); // the player has to pay the house rent times the number of houses in that property
   playerWhoGetsPaid.money += (property.houseRent * property.houseCounter);
   println(name + " has paid " + (property.houseRent * property.houseCounter) + "€ to " + playerWhoGetsPaid.name + " in " + property.name);
   statusOfGame = (name + " has paid " + (property.houseRent * property.houseCounter) + "€ to " + playerWhoGetsPaid.name + " in " + property.name);
   // moneySpent += (property.houseRent * property.houseCounter);
   moneyPaid += (property.houseRent + property.houseCounter);
   // println(name + "has spent a total of " + moneySpent + "€");
   println(name + "has paid a total of " + moneyPaid + "€");
   playerWhoGetsPaid.moneyGained += (property.houseRent * property.houseCounter);
   println(playerWhoGetsPaid.name + "has gained a total of " + playerWhoGetsPaid.moneyGained + "€");
   // fill(200, 0, 0, 50);
   // rect(infoPanelX, infoPanelY, 200, 50);
   // }
   }
   */

  public void addHouse(Property property) // the player adds a house to the property he chooses
  {
    int count = 0; // to count the amount of properties of a same monopoly that the player owns. As the code is right now, the amount of houses needed of the same monopoly are two

    // check if the player has all of the properties that make the monopoly

    if (property.houseCounter <= property.maximumHouses) // if the property's house counter is smaller than the property's maximum amount of houses he can still add more houses to that property
    {
      for (int i = 0; i < properties.length; i++) // it goes through all the properties
      { 
        Property p = properties[i];
        // println("Monopoly number: " + p.monopolyNumber);
        if (p.monopolyNumber == property.monopolyNumber && p.ownedByPlayer == number) // if the property's monopoly number is the same as the chosen property's number and the property is owned by the player 
        {
          println("Monopoly number: " + p.monopolyNumber);
          count++; // increase the monopoly counter
          println("Count: " + count);
          // if(count == 2) hasMonopoly = true;
        }
      }
      // if(count == amountOfPropertiesForMonopoly && hasMonopoly == true)
      if (count == amountOfPropertiesForMonopoly) // if the moonopoly counter is the same as the amount of properties needed to have a monopoly it means that this player has the chosen property's monopoly
      {
        println("As " + name + " has the monopoly it is possible to add a house in " + property.name);
        if (money >= property.housePrice) // if the player's money is bigger than or equal than the property's house price
        {
          money -= property.housePrice; // decrease the player's money by the value of the property's house price
          property.houseCounter += 1; // increase the amount of houses that the property has by one
          println(name + " has added a house in " + property.name);
          statusOfGame = (name + " has added a house in " + property.name); // update the status of the game
          coinsSound.play(0); // play the coins shuffling sound
        } else // if the player doesn't have enough money to add houses
        {
          statusOfGame = (name + " doesn't have enough money to \n perform this operation"); // update the status of the game
        }
      }
    } else // if the player's house counter is bigger than the maximum amount of houses of a property
    {
      println(property.name + " already has the maximum house quantity");
      statusOfGame = (property.name + " already has the maximum house quantity"); // update the status of the game
    }
  }

  public void showProperties() // show the properties
  {
    println(name + "'s properties:");
    for (int i = 0; i < propertiesOwned.length; i++)
    {
      if (propertiesOwnedNames[i] != null)
      {
        // println(propertiesOwned[i]);
        // println(propertiesOwnedNames[i]);

        // color owned properties
        // fill(playerColor, 200);
        // rect(properties[i].x, properties[i].y, properties[i].propertyWidth, properties[i].propertyHeight);
      }
    }
  }

  public void trade(Property property) // the player wants to trade the selected property
  {
    tradePropertyPrice += property.tradePrice; // increase the trade property price by the value of the property's trade price
    println(name + " wants to trade " + property.name + " for " + tradePropertyPrice + "€ from player " + property.ownedByPlayer);
    statusOfGame = (name + " wants to trade " + property.name + " for " + tradePropertyPrice + "€"); // update the status of the game
  }

  public void tradeAccepted() // if the trade has been accepted
  {
    if (propertyToTrade != null) // if the property to trade is not null
    {
      println(name + " has successfully traded " + propertyToTrade.name + " for " + tradePropertyPrice + "€ from " + propertyToTrade.ownedByPlayer);
      statusOfGame = (name + " has successfully traded " + propertyToTrade.name + " for " + tradePropertyPrice + "€\nfrom " + propertyToTrade.ownedByPlayer); // update the status of the game

      if (money >= tradePropertyPrice) // if the money of the player is bigger than or equal to the property trade price 
      {
        money -= tradePropertyPrice; // decrease the player's money by the value of the property's trade price
        for (int i = 0; i < players.length; i++) // it goes through all the properties
        {
          Player player = players[i];
          if (player.number == propertyToTrade.ownedByPlayer) // if the player's number is owned by the same player that owns the property that is to be traded
          {
            player.money += tradePropertyPrice; // increase that player's money to the value of the player's property trade price
          }
        }
        propertyToTrade.ownedByPlayer = number; // the traded house is now owned by this player
        coinsSound.play(); // play the coins shuffling sound
      } else // if the player doesn't have enough money to trade
      {
        statusOfGame = (name + " doesn't have enough money to \n perform this operation"); // update the status of the game
      }
    } else // if the property is null (owned by the bank)
    {
      println("Cannot trade a not owned property");
      statusOfGame = ("Cannot trade a not owned property"); // update the status of the game
    }
  }

}
class Property
{
  // variables
  String name; // the name of the property
  int boardPosition; // each block of the board has a number and name
  int x; // x coordinate of the property
  int y; // y coordinate of the property
  int propertyWidth = 200; // width of the property
  int propertyHeight = 200; // height of the property
  float buyPrice; // the price to buy the property
  float rentPrice; // the price to rent the property
  float housePrice; // the price to buy a house for the property
  float tradePrice;
  float houseRent; // the price to rent the property with houses
  int houseCounter = 1; // the number of houses that the property has. By default it's only one
  int ownedByPlayer = 0; // by default, the property is not owned by any of the players
  int propertyColor; // the color of the property
  int monopolyNumber; // the monopoly number of the property. If a player has all of the properties that make a monopoly he can start adding houses

  // maximum number of houses that the user can add in one property
  int maximumHouses = 4; // this excludes the already existing house that comes when buying the property --> this means that the total amount of houses in a property is 5

  int ownedByBank = 0; // the bank's properties (properties that weren't bought)


  // constructor
  Property(int temp_x, int temp_y, String temp_name, float temp_buyPrice, int temp_board_position, int temp_monopoly_number)
  {
    x = temp_x;
    y = temp_y;
    name = temp_name;
    boardPosition = temp_board_position;
    buyPrice = temp_buyPrice;
    rentPrice = buyPrice / 5;
    housePrice = buyPrice - (buyPrice / 4);
    houseRent = housePrice / 5;
    tradePrice = (houseRent * 2);
    monopolyNumber = temp_monopoly_number;
  }

  public void display() // display the properties blocks
  {
    // to give the property the color of the player
    for (int i = 0; i < players.length; i++)
    {
      if (ownedByPlayer == players[i].number) // if yhe player owns the property
      {
        propertyColor = players[i].playerColor; // set the property color to be the same as the player color
      }
    }

    // paint the property with its corresponding color with a transparency of 50
    fill(propertyColor, 50);
    rect(x, y, propertyWidth, propertyHeight);

    fill(0); // black
    // houses text
    int housesTextX = x + (propertyWidth / 2);
    int housesTextY = y + (propertyHeight / 2 + 75);
    text("Houses: " + houseCounter, housesTextX, housesTextY);
    // houses name text
    int housesNameX = x + 100;
    int housesNameY = y + 50;
    text(name, housesNameX, housesNameY);


    int [] cornerProperties = {0, 4, 8, 12}; // the corner properties

    if (ownedByPlayer == ownedByBank) // if the bank owns the property
    {
      // buy price text
      int buyTextX = x + 100;
      int buyTextY = y + 150;
      text("Buy: " + buyPrice + "€", buyTextX, buyTextY);
    } else if (boardPosition == cornerProperties[0] || boardPosition == cornerProperties[1] || boardPosition == cornerProperties[2] || boardPosition == cornerProperties[3])
    {
      // public property text
      int publicTextX = x + 100;
      int publicTextY = y + 150;
      text("Public", publicTextX, publicTextY);
    } else
    {
      // rent house text
      int rentTextX = x + 100;
      int rentTextY = y + 150;
      text("Rent: " + (houseRent * houseCounter) + "€", rentTextX, rentTextY);
    }
  }

  public void showPricePerHouse() // shows the price per house text
  {
    // price per house text
    int housePriceTextX = x + (propertyWidth / 2);
    int housePriceTextY = y + (propertyHeight / 2);
    text(housePrice + "€ / house", housePriceTextX, housePriceTextY);
  }

  public void rent(Player playerWhoPays)
  {
    float payAmount = (houseRent * houseCounter); // the amount of money that the player who landed on this property needs to pay to its rightful owner

    for (int i = 0; i < players.length; i++) // goes through all the players
    {
      Player player = players[i];
      if (player.number == (ownedByPlayer)) // if the player's number is the same as the player's number who owns the property
      {
        if (playerWhoPays.money >= payAmount) // if the player has enough money
        {
          playerWhoPays.money -= payAmount; // the player who landed on the property pays the pay amount
          player.money += payAmount; // the player who owns the property gains the pay amount

          statusOfGame = (playerWhoPays.name + " has paid " + payAmount + "€ to " + player.name + " in " + name); // updates the status of the game
          // moneySpent += (property.houseRent * property.houseCounter);
          playerWhoPays.moneyPaid += payAmount; // the player's total pay amount is increased by this pay amount
          // println(name + "has spent a total of " + moneySpent + "€");
          // println(playerWhoPays.name + "has paid a total of " + playerWhoPays.moneyPaid + "€");
          player.moneyGained += payAmount; // the player who owns the property increases his money gained by the pay amount value
          // println(player.name + "has gained a total of " + player.moneyGained + "€");

          // play the coins sound
          coinsSound.play(0);
        } else // if the player doesn't have enough money to pay
        {
          statusOfGame = (playerWhoPays.name + " has gone bankrupt!!"); // update the status of the game
          playerWhoPays.money -= payAmount; // the player goes bankrupt (minus zero) by paying the price of the rent
          playerWhoPays.isBankrupt = true; // the player is now bankrupt
          changeTurn(); // it changes the player turn
        }
      }
    }

    /*
    money -= payAmount; // the player has to pay the house rent times the number of houses in that property
     playerWhoGetsPaid.money += payAmount;
     println(name + " has paid " + (payAmount) + "€ to " + playerWhoGetsPaid.name + " in " + property.name);
     statusOfGame = (name + " has paid " + (payAmount) + "€ to " + playerWhoGetsPaid.name + " in " + property.name);
     // moneySpent += (property.houseRent * property.houseCounter);
     moneyPaid += (payAmount);
     // println(name + "has spent a total of " + moneySpent + "€");
     println(name + "has paid a total of " + moneyPaid + "€");
     playerWhoGetsPaid.moneyGained += (payAmount);
     println(playerWhoGetsPaid.name + "has gained a total of " + playerWhoGetsPaid.moneyGained + "€");
     */
  }

  public void noLongerOwned() // if the property is no longer owned, the color of the property is set to grey
    {
      ownedByPlayer = ownedByBank; // the bank now owns the property
      propertyColor = color(100); // the property color is set to grey
    }
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "monopoly_clone" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
