import java.awt.*;
import objectdraw.*;

/**
 * The button class is responsible for constructing a button objec, perfectly centering the text within button, 
 * and finally defining a contains method for the button object.
 *
 * @author Sabirah Shuaybi
 * @version 10/9/16
 */

public class Button

{
    //Two components that make up a button: rectangle and text
    private FramedRect button;
    private Text description;

    //Constant for button height
    //(Since all buttons have the same height but not necessarily the same width)
    private static final int buttonHeight = 20;

 /* Private method that calculates width of a button based on length of description (string).
    The length of the string is then multiplied by a constant
    for neat spacing of text within the button */
    private double computeWidth(String buttonText) {
        return buttonText.length() * 10;
    }

    /* Constructor for Button class */
    public Button (DrawingCanvas canvas, double locX, double locY, String buttonText) {

        //Determine button width based on length of text passed in
        double buttonWidth = computeWidth(buttonText);

        button = new FramedRect(locX, locY, buttonWidth, buttonHeight, canvas);
        description = new Text(buttonText, locX, locY, canvas);

        //Calculate the center of button
        double buttonCenterX = locX + (buttonWidth/2);
        double buttonCenterY = locY + (buttonHeight/2);

        //Configure the centering of text within button
        double descriptionCenteredX = buttonCenterX - (description.getWidth()/2);
        double descriptionCenteredY = buttonCenterY - (description.getHeight()/2);

        //Move text to centered location
        description.moveTo(descriptionCenteredX, descriptionCenteredY);

    }

    /* Used to achieve even spacing between each button */
    public double getRight() {

        double buttonRight = button.getX() + button.getWidth();

        //Return the right most x coordinate of the button
        return buttonRight;
    }

    /* Determines whether click was contained within button */
    public boolean contains (Location mouseClick) {

        if (button.contains(mouseClick))
            return true;
        return false;
    }
}
