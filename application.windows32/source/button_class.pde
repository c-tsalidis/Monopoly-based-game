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

  void display() // display the button
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
