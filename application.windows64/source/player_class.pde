class Player
{
  // variables
  String name; // player name
  int number; // player number (player 1, player 2 ...)
  int counter = 0; // the index number of the GO property block
  int x; // x coordinate of the player
  int y; // y coordinate of the player
  color playerColor; // color of the player
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

  void display(Property property) // displays the player in the screen
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

  void move() // moves the player to the correct place
  {
    // As the properties index are in order, the player's x and y are the same as the x and y coordinates of the property's position
    x = properties[position].x; // move the player to the property's x position
    y = properties[position].y; // move the player to the property's y position
    // println("Player " + turn + " is in "+ "property: " + properties[position].name);
  }

  void checkPosition() // updates and checks the position of the player depending on the dice number and its previous position
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

  void buy(Property property) // the player buys the property he chose
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

  void addHouse(Property property) // the player adds a house to the property he chooses
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

  void showProperties() // show the properties
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

  void trade(Property property) // the player wants to trade the selected property
  {
    tradePropertyPrice += property.tradePrice; // increase the trade property price by the value of the property's trade price
    println(name + " wants to trade " + property.name + " for " + tradePropertyPrice + "€ from player " + property.ownedByPlayer);
    statusOfGame = (name + " wants to trade " + property.name + " for " + tradePropertyPrice + "€"); // update the status of the game
  }

  void tradeAccepted() // if the trade has been accepted
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
