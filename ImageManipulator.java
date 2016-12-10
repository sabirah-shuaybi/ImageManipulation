import java.awt.*;
import objectdraw.*;

/**
 * The ImageManipulator class loads, displays a picture and constructs buttons from button class
 * that have contain methods attached to photoshopping methods like gray scale, mirror, blur and
 * (revert back to) original.
 *
 * This class defines an instance variable Picture picture that holds the value of the mutated picture
 * throughout the program and thus allows the user to perform multiple effects on the picture.
 * For example, a user can click the button "Mirror" at which point the picture will become mirrored.
 * Then, directly afterwards, the user can press the button "Gray" and the mirrored picture will then become
 * grayed. Thus, the class allows for a layering of effects and similarly, blurrings of blurred picture.
 *
 * @author Sabirah Shuaybi
 * @version 10/04/16
 */

public class ImageManipulator extends WindowController

{
    private Picture picture;
    private VisibleImage visibleImage;

    //To position picture 40 pixels down from top of canvas (to leave room for buttons)
    private static final int PIC_LOC_Y = 40;

    //Image manipulating buttons
    private Button grayButton;
    private Button mirrorButton;
    private Button blurButton;
    private Button originalButton;

    //Constant y coordinate of buttons
    private static final double LOC_Y = 10.0;
    //Constant x coordinate of first button (gray button)
    private static final double LOC_X = 20.0;
    //Constant distance between each button
    private static final double BUTTON_DISTANCE = 15.0;
    //Constant blur count
    private static final int BLUR_COUNT = 20;

    public void begin() {

        //Debugging
        log("Entering begin()");

        //Load a picture from URL/file path
        loadPicture();

        //Display this picture onto canvas
        displayPicture();

        log("Resizing picture");

        //Resize window to make it an appropriate size for picture
        resize(picture.getWidth(), picture.getHeight());

        //Construct gray button
        grayButton = new Button(canvas, LOC_X, LOC_Y, "Gray");

        //Compute x coordinate of mirror button relative to x location of gray button
        double mirrorButtonX = grayButton.getRight() + BUTTON_DISTANCE;

        //Construct mirror button
        mirrorButton = new Button(canvas, mirrorButtonX, LOC_Y, "Mirror");

        //Compute x coordinate of blur button relative to x location of mirror button
        double blurButtonX = mirrorButton.getRight() + BUTTON_DISTANCE;

        //Construct blur button
        blurButton = new Button(canvas, blurButtonX, LOC_Y, "Blur");

        //Compute x coordinate of original button relative to x location of blur button
        double originalButtonX = blurButton.getRight() + BUTTON_DISTANCE;

        //Construct original button
        originalButton = new Button(canvas, originalButtonX, LOC_Y, "Original");
    }

    /* Loads picture url and stores it in instance variable picture */
    private void loadPicture() {

        picture = new Picture ("http://www.mtholyoke.edu/~blerner/SnickersInSnow2.jpg");
    }

 /* Displays loaded picture onto canvas and allows mutated version of
    original picture to be displayed on canvas */
    private void displayPicture() {

        //Remove loaded picture from canvas only if there is a loaded picture in the first place
        if (visibleImage != null) {
            visibleImage.removeFromCanvas();
        }
        //Create a visible image at coordinate (0,40) on canvas
        visibleImage = picture.createVisibleImage(0, PIC_LOC_Y, canvas);
    }

 /* Pure function/method that takes any picture as a parameter and
    returns a grayed version of that picture */
    private Picture grayScale(Picture picture) {

        log("Entering grayScale()");

        //Assign width and height of picture to local variables to create a empty picture of same size
        int width = picture.getWidth();
        int height = picture.getHeight();

        //Create a new Picture object w/ identical dimensions of instance variable picture
        Picture grayedPicture = new Picture (width, height);

        //Initialize row value to zero at start of outer loop
        double row = 0;

        //While value of row is less than height of picture, keep moving down rows of pixels
        while (row < height) {
            //Initialize column value to zero for the start of each NEW row
            double column = 0;

            //While value of column is less than width of picture, keep moving across columns of said row
            while (column < width) {

                //Get said pixel at (row, column)
                Color pixelColor = picture.getPixel(row, column);
                //Request red component of pixel
                int red = pixelColor.getRed();
                //Request green component of pixel
                int green = pixelColor.getGreen();
                //Request blue component of pixel
                int blue = pixelColor.getBlue();

                //Average RGB values
                int averageRGB = (red + green + blue)/3;

                //Create a new color with each of the RGB values set to the average calculated above
                Color grayShade = new Color(averageRGB, averageRGB, averageRGB);

                //Set pixel at a said row and said column to this grayShade color
                grayedPicture.setPixel(row, column, grayShade);

                //Proceed to next column
                column++;
            }
            //Proceed to next row
            row++;
        }
        //Return a grayed picture
        return grayedPicture;
    }

 /* Pure function/method that takes any picture as a parameter and returns a mirror of that picture */
    private Picture mirror(Picture picture) {

        //Assign width and height of picture to local variables to create an empty picture of same size
        int width = picture.getWidth();
        int height = picture.getHeight();

        //Create a new Picture object w/ identical dimensions of instance variable picture
        Picture mirroredPicture = new Picture (width, height);

        //Initialize row value to zero at start of outer loop
        double row = 0;

        //While row value is less than height of picture, keep moving down rows of pixels
        while (row < height) {

            //A variable pointer that moves across picture, column by column, from left to right
            double c1 = 0;

            //A variable pointer that moves across picture, column by column, from right to left
            //Subtracting 1 from width because columns begin with 0, 1, 2, 3...
            double c2 = width - 1;

            //While c1 pointer is <= to c2 pointer, keep moving c1 pointer to left and c2 pointer to right
            //Once c1 pointer is greater than c2, it means that the entire image has been covered in terms of swapping pixels
            while (c1 <= c2) {

                Color left = picture.getPixel(row, c1);
                Color right = picture.getPixel(row, c2);

                //A temporary Color variable to store value of left while swapping right and left values
                Color temp;

                //Swap the left pixels and the right pixels by using temp var
                //Significance: to temporarily hold value of left so it is not lost during the swap
                temp = left;
                left = right;
                right = temp;

                //Set these switched pixel values to mirroredPicture
                mirroredPicture.setPixel(row, c1, left);
                mirroredPicture.setPixel(row, c2, right);

                //Increment c1 variable by 1
                c1++;
                //Decrement c2 variable by 1
                c2--;
            }
            //Proceed to next row
            row++;
        }
        //Return a mirrored picture
        return mirroredPicture;
    }

 /* Box blur method that takes 9 pixels (3x3 box) and returns a color that is the average of all 9 pixels
    Box blur = filter in which each pixel in mutated image has a value equal to
    the average value of its neighboring pixels */
    private Color averagePixel(Picture picture, double x, double y) {

        //9 boxed pixels constituted by 9 Color variables
        //Pixels are arranged side by side in a 3 by 3 box unit
        //Boxed arrangement is achieved by adjusting x, y coordinates of each color variable in reference to p0 (x, y)
        Color p0 = picture.getPixel(x, y);
        Color p1 = picture.getPixel((x -1), (y-1));
        Color p2 = picture.getPixel(x, (y-1));
        Color p3 = picture.getPixel((x+1), (y-1));
        Color p4 = picture.getPixel((x-1), y);
        Color p5 = picture.getPixel((x+1), y);
        Color p6 = picture.getPixel((x-1), (y+1));
        Color p7 = picture.getPixel(x, (y+1));
        Color p8 = picture.getPixel((x+1), (y+1));

        //Average all 9 red components of pixels in a 3x3 box
        int red = ((p0.getRed() + p1.getRed() + p2.getRed() + p3.getRed() + p4.getRed() +
                    p5.getRed() + p6.getRed() + p7.getRed() + p8.getRed())/9);

        //Average all 9 green components of pixels in a 3x3 box
        int green = ((p0.getGreen() + p1.getGreen() + p2.getGreen() + p3.getGreen() + p4.getGreen() +
                    p5.getGreen() + p6.getGreen() + p7.getGreen() + p8.getGreen())/9);

        //Average all 9 blue components of pixels in a 3x3 box
        int blue = ((p0.getBlue() + p1.getBlue() + p2.getBlue() + p3.getBlue() + p4.getBlue() +
                    p5.getBlue() + p6.getBlue() + p7.getBlue() + p8.getBlue())/9);

        //Return a blurred Color with the three averaged R,G,B values
        return new Color (red, green, blue);
    }

 /* Method that takes any picture as a parameter and returns a blurred version of that picture
    through multiple blur loop iterations */
    private Picture blur(Picture picture) {

        log("In blur()");

        //Assign width and height of picture to local variables to create a empty picture of same size
        int width = picture.getWidth();
        int height = picture.getHeight();

        //Create a new Picture object w/ identical dimensions of instance variable picture
        Picture blurredPicture = new Picture (width, height);

        //Initiialize blurCount to zero
        int blurCount = 0;

        //While blurCount is less than constant BLUR_COUNT (20), keep adding 1 to blurCount
        //AND while blurCount is less than 20, keep assigning blurredPicture to picture
        //These multiple iterations of blur loop approximate the Gaussian blur
        while (blurCount < BLUR_COUNT) {

            //Initialize row to zero
            double row = 0;

            //WHile row is less than height of picture, keep moving down rows of pixels
            while (row < height) {

                //Initialize column to zero
                double column = 0;

                //While column value is less than width of picture, keep moving across columns of said row
                while (column < width) {

                    //To hold the new, blurred color
                    Color newPixel;

                    //If pixel is part of border of picture, get pixels from original picture
                    //Significance: to leave border alone - no blurring of border pixels
                    //This is because we are not blurring the borders, leaving them as they were
                    if (row == 0 || row == height-1 || column == 0 || column == width-1) {
                        newPixel = picture.getPixel(row, column);
                    }
                    //If pixel is not part of the border, blur via averaging RGB
                    else {
                        newPixel = averagePixel(picture, row, column);
                    }

                    //Set pixel at said row, said column of blurredPicture to newPixel
                    //The color of newPixel will depend on which if/else condition will be satisfied
                    blurredPicture.setPixel(row, column, newPixel);

                    //Proceed to next column
                    column++;
                }
                //Proceed to next row
                row++;
            }

            //Run while loop again (for a greater blurred effect)
            blurCount++;
            //Blur the blurred image
            picture = blurredPicture;
        }
        //Return a blurred picture
        return blurredPicture;
    }

    /* Determines which action to take depending on location of mouse click and which button was clicked */
    public void onMouseClick(Location mouseClick) {

        //If gray button clicked, gray the picture,
        //Assign grayed picture to main picture variable
        //This assignment will allow layering of many effects (such as mirroring a grayed pic)
        //Lastly, display grayed picture onto canvas
        if(grayButton.contains(mouseClick)) {
            Picture grayedPicture = this.grayScale(picture);
            this.picture = grayedPicture;
            displayPicture();
        }

        //If mirror button clicked, mirror the picture
        //Assign mirrored picture to instance variable picture (to allow layering of effects)
        //Lastly, display mirrored picture onto canvas
        if(mirrorButton.contains(mouseClick)) {
            Picture mirroredPicture = this.mirror(picture);
            this.picture = mirroredPicture;
            displayPicture();
        }

        //If blur button clicked, blur the picture
        //Assign blurred picture to instance variable picture (layering)
        //Lastly, display this blurred picture onto canvas
        if(blurButton.contains(mouseClick)) {
            Picture blurredPicture = this.blur(picture);
            this.picture = blurredPicture;
            displayPicture();
        }

        //If original button clicked, load original picture
        //Display the original picture onto canvas
        if(originalButton.contains(mouseClick)) {
            loadPicture();
            displayPicture();
        }

    }

    /* Mini-private method created for debugging */
    private void log(String msg) {

        System.out.println(msg);
    }

}
