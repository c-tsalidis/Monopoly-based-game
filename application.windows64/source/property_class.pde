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
  color propertyColor; // the color of the property
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

  void display() // display the properties blocks
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

  void showPricePerHouse() // shows the price per house text
  {
    // price per house text
    int housePriceTextX = x + (propertyWidth / 2);
    int housePriceTextY = y + (propertyHeight / 2);
    text(housePrice + "€ / house", housePriceTextX, housePriceTextY);
  }

  void rent(Player playerWhoPays)
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

  void noLongerOwned() // if the property is no longer owned, the color of the property is set to grey
    {
      ownedByPlayer = ownedByBank; // the bank now owns the property
      propertyColor = color(100); // the property color is set to grey
    }
}
