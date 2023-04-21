package checkers3d.presentation;

import checkers3d.storage.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * A class that represents a generic object that may be displayed in screen
 * space in terms of pixels. It contains the color and transparency of
 * individual pixels making up any arbitrary 2D shape that may be contained by a
 * rectangular bounding box.
 *
 * @author      Ruben Acuna
 */
public class RenderResourceBitmap implements IRenderResource {

    /**
     * The BufferedImage object which contains a pixel level representation of
     * this object's color and transparency.
     */
    private BufferedImage image;

    /**
     * Render scale for bitmap. 1.0 is normal size.
     */
    private float scale;

    /**
     * Rotation of bitmap in radians, 0.0 is normal rotation.
     */
    private float rotation;

    /**
     * Class constructor that loads image data from a file. If the file can be
     * loaded by AWT, we use it to populate the BufferedImage. Otherwise, we
     * create a blank BufferedImage and log the failure to load it.
     *
     * @param  filename A relative path to a file containing an image.
     */
    public RenderResourceBitmap(String filename) {
        this.scale = 1.0f;
        this.rotation = 0.0f;
        try {
            setImage(ImageIO.read(new File(filename)));
        } catch (IOException fail) {
            setImage(new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB));
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 31, 31);
            Logger.log(toString() + " " + filename + " - " + fail.getMessage());
        }
    }

    /**
     * Class constructor that uses a preexisting BufferedImage for image data.
     *
     * @param  image A BufferedImage that will be used for image data.
     */
    public RenderResourceBitmap(BufferedImage image) {
        this.scale = 1.0f;
        this.rotation = 0.0f;
        setImage(image);
    }

    /**
     * Returns a reference to the object containing the image data for this
     * RenderResourceBitmap.
     *
     * @return       A reference to the RenderResourceBitmap's BufferedImage
     *               object.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the BufferedImage object that contains this object's image data to
     * another BufferedImage.
     *
     * @param  image A reference to a BufferImage that this RenderResourceBitmap
     *               should draw.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Returns true if the given point is drawn by the image data, false
     * otherwise.
     *
     * @param  point A point relative to this object's image data.
     * @return       Boolean indicating if the point is drawn by the image data.
     * @see IRenderResource
     */
    public boolean containsPoint(Point point) {
        if (point.x >= image.getWidth() || point.y >= image.getHeight() || point.x < 0 || point.y < 0) return false; else {
            int pixel = image.getRGB(point.x, point.y);
            if (((pixel >> 24) & 0xff) > 0) return true; else return false;
        }
    }

    /**
     * Returns the height of this RenderResourceBitmap in pixels.
     *
     * @return Height in pixels.
     * @see IRenderResource
     */
    public int getHeight() {
        return (int) (image.getHeight() * scale);
    }

    /**
     * Gets the draw rotation of this bitmap.
     *
     * @return The current rotation value for rendering.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Gets the draw scale of this bitmap.
     *
     * @return The current scale value for rendering.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Returns the width of this RenderResourceBitmap in pixels.
     *
     * @return Width in pixels.
     * @see IRenderResource
     */
    public int getWidth() {
        return (int) (image.getWidth() * scale);
    }

    /**
     * Resets the animation of this render resource.
     */
    public void reset() {
        scale = 1.0f;
        rotation = 0.0f;
    }

    /**
     * Sets the draw rotation of this bitmap.
     *
     * @param scale The rotation value to be used for rendering.
     */
    public void setRotation(float rotation) {
        this.rotation = (float) (rotation % (Math.PI * 2));
    }

    /**
     * Sets the draw scale of this bitmap.
     *
     * @param scale The scale value to be used for rendering.
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Informs this IRenderResourceBitmap that a given amount of time in
     * milliseconds has elapsed. Used for animation and time events.
     *
     * @param ms Milliseconds elapsed.
     * @see IRenderResource
     */
    public void tick(int ms) {
    }

    /**
     * Returns a string representation of this RenderResourceBitmap. Includes the
     * class name and string representation of the image data.
     *
     * @return       A string representation of this RenderResourceBitmap.
     */
    @Override
    public String toString() {
        return getClass().getName() + " -> " + image.toString();
    }

    /**
     * Returns a new instance of this object type with all local variables the
     * same.
     *
     * @return A new instance of this object.
     */
    @Override
    public RenderResourceBitmap clone() {
        RenderResourceBitmap copy = new RenderResourceBitmap(getImage());
        copy.setScale(getScale());
        copy.setRotation(getRotation());
        return copy;
    }
}
