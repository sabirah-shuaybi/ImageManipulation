import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import objectdraw.*;

/**
 * The Picture class provides access to the pixels in an image, allows the pixels
 * to be modified and provides functionality to display an image on an
 * objectdraw canvas.
 * 
 * @author Barbara Lerner
 * @version October 26, 2007
 */
public class Picture {
    // The pixels that make up the image.
    private int[] pixels;
    
    // The number of pixels wide that the image is
    private int imgWidth;
    
    // The number of rows of pixels in the image.
    private int imgHeight;
    
    /**
     * Load a picture from a file.
     * @param filename the name of the file containing an image to load
     */
    public Picture(String filename) {
        // Load the image
        try {
            ImageIcon icon;
            if (filename.startsWith ("http")) {
                    icon = new ImageIcon (new URL (filename));
            }
            else {
                icon = new ImageIcon (filename);
            }
            Image image = icon.getImage();
            imgWidth = icon.getIconWidth();
            imgHeight = icon.getIconHeight();
            
            // Extract the pixels from the image.
            pixels = new int[imgWidth * imgHeight];
            PixelGrabber pg = new PixelGrabber(image, 0, 0, imgWidth, imgHeight, pixels, 0, imgWidth);
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                System.err.println("interrupted waiting for pixels!");
                return;
            }
            if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                System.err.println("image fetch aborted or image not found");
                return;
            }
        }
        catch (MalformedURLException e) {
            System.err.println ("Please check the URL");
        }
    }
    
    /**
     * Create a blank picture.
     * @param width the width of the blank picture in pixels
     * @param height the height of the blank picture in pixels
     */
    public Picture (int width, int height) {
        imgWidth = width;
        imgHeight = height;
        pixels = new int[width*height];
    }
    
    /**
     * @return the number of columns of pixels in the image
     */
    public int getWidth () {
        return imgWidth;
    }
    
    /**
     * @return the number of rows of pixels in the image
     */
    public int getHeight() {
        return imgHeight;
    }
    
    /**
     * Get the color of a pixel at a particular location.  This
     * method will fail (with an ArrayIndexOutOfBoundsException)
     * if row or col are negative or are beyond the bounds of the image.
     * @param row the row of the pixel
     * @param col the column of the pixel
     * @return the color of the requested pixel
     */
    public Color getPixel (double row, double col) {
        return new Color (pixels[(int)row * imgWidth + (int)col]);
    }
    
    /**
     * Change the color of a pixel at a particular location.  This method 
     * does nothing if row or col are negative or are beyond the dimensions of 
     * the image.
     * @param row the row of the pixel
     * @param col the column of the pixel
     * @param color the new color for the pixel
     */
    public void setPixel (double row, double col, Color color) {
        int row1 = (int) row;
        int col1 = (int) col;
        if (row1 >= 0 && row1 < imgHeight && col1 >= 0 && col1 < imgWidth) {
            pixels [row1 * imgWidth + col1] = color.getRGB();
        }
    }
    
    /**
     * Displays an image on the canvas.
     * @param left the coordinate for the left edge of the image
     * @param top the coordinate for the top of the image
     * @param canvas the canvas to draw on
     * @return the VisibleImage that was displayed
     */
    public VisibleImage createVisibleImage(double left, double top, DrawingCanvas canvas) {
        // Create an image from the pixels
        Image image = Toolkit.getDefaultToolkit().createImage (new MemoryImageSource (imgWidth, imgHeight, pixels, 0, imgWidth));

        // Display the image
        return new VisibleImage (image, left, top, canvas);
    }
}
