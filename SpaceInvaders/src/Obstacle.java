import java.awt.*;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * Obstacle class:
 * Defines a simple obstacle that just draws the specified rectangle.
 */
public class Obstacle extends Rectangle {
    /**
     * Defines an obstacle that will block projectiles.
     *
     * @param position Position where to place the obstacle.
     * @param width Width of the obstacle.
     * @param height Height of the obstacle.
     */
    public Obstacle(Position position, int width, int height) {
        super(position, width, height);
    }

    /**
     * Draws the rectangle to fill in the obstacle.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(new Color(36, 127, 4, 191));
        g.fillRect(position.x, position.y, width, height);
    }
}
