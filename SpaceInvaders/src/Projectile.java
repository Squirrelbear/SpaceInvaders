import java.awt.*;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * Projectile class:
 * Defines a projectile that moves in a direction.
 */
public class Projectile extends Rectangle {
    /**
     * Speed to move at. Distance in pixels to move over 1000ms.
     */
    private static final int MOVE_SPEED = 300;
    /**
     * Width of the projectile.
     */
    private static final int WIDTH = 4;
    /**
     * Height of the projectile.
     */
    private static final int HEIGHT = 10;
    /**
     * Reference to the object that spawned the projectile.
     */
    private Rectangle parentObj;
    /**
     * The unit vector representing direction of projectile movement.
     */
    private Position moveVector;
    /**
     * When true, the projectile has hit something or moved off the screen and should be removed.
     */
    private boolean isExpired;
    /**
     * Reference to the object manager to check for collisions.
     */
    private ObjectManager objectManager;

    /**
     * Creates the projectile centred inside the parent object. The direction is
     * set to either up if the parent is the player, or down otherwise.
     *
     * @param parentObj Reference to the object that spawned this projectile.
     * @param objectManager Reference to the object manager to check for collisions.
     */
    public Projectile(Rectangle parentObj, ObjectManager objectManager) {
        super(parentObj.getCentre(), WIDTH, HEIGHT);
        this.objectManager = objectManager;
        this.parentObj = parentObj;
        if(parentObj instanceof Player) {
            moveVector = new Position(Position.UP);
        } else {
            moveVector = new Position(Position.DOWN);
        }
        isExpired = false;
    }

    /**
     * Updates the projectile by moving in the direction defined at creation.
     * Checks for collisions with objects or leaving the screen to make it expire
     * ready for removal. If an object is collided with that uses the CollidableObject
     * interface it will call the hit() method.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        Position relativeMove = new Position(moveVector);
        relativeMove.multiply(MOVE_SPEED * deltaTime / 1000);
        position.add(relativeMove);
        Rectangle collision = objectManager.getCollision(this);
        if(position.y <= -height || position.y >= GamePanel.PANEL_HEIGHT || collision != null) {
            isExpired = true;
        }
        if(collision != null && collision instanceof CollidableObject) {
            ((CollidableObject)collision).hit();
        }
    }

    /**
     * Draws the projectile at its current location as either white for the player,
     * or green for any other parent.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(parentObj instanceof Player ? Color.WHITE : Color.GREEN);
        g.fillRect(position.x, position.y, width, height);
    }

    /**
     * Gets the parent object reference to whatever spawned this projectile.
     *
     * @return A reference to the parent object.
     */
    public Rectangle getParentObject() {
        return parentObj;
    }

    /**
     * A projectile expires when it either leaves the screen or
     * when it has collided with any collidable object.
     *
     * @return True if the projectile is scheduled to be destroyed.
     */
    public boolean isExpired() {
        return isExpired;
    }
}
