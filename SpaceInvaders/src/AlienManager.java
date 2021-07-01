import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * AlienManager class:
 * Manages a group of aliens by spawning them in,
 * it moves them gradually down the screen moving left
 * to right.
 */
public class AlienManager {
    /**
     * List of aliens that are currently spawned in and alive.
     */
    private List<Alien> aliens;
    /**
     * Reference to the ObjectManager for passing to the Aliens.
     */
    private ObjectManager objectManager;
    /**
     * The unit vector indicating direction of motion.
     */
    private Position curMoveDir;
    /**
     * The speed to move at in direction of the unit vector.
     */
    private final int MOVE_SPEED = 1;
    /**
     * Top left corner of the AlienManager used for tracking where the whole grid of aliens are.
     */
    private Position topLeft;
    /**
     * When true the last movement for left/right was moving left. It means that the next should be right.
     */
    private boolean lastLeft;
    /**
     * The amount of pixels the aliens will move down before the move to the left or right again.
     */
    private int downNextTrigger;
    /**
     * Stores the lowest alien Y coordinate for checking for alien invasions.
     */
    private int lowestAlienY;

    /**
     * Sets up the manager and spawns in an example set of aliens.
     *
     * @param objectManager Reference to the ObjectManager to pass to the aliens.
     */
    public AlienManager(ObjectManager objectManager) {
        aliens = new ArrayList<>();
        this.objectManager = objectManager;
        reset();
    }

    /**
     * Resets by resetting the aliens with a new set of spawns,
     * and all their group movement state data back to defaults.
     */
    public void reset() {
        spawnAliens();
        curMoveDir = Position.RIGHT;
        lastLeft = false;
        downNextTrigger = 5;
        lowestAlienY = 0;
    }

    /**
     * Creates a grid of aliens to fill the area horizontally, with 5 rows.
     * Each row has a different type.
     */
    public void spawnAliens() {
        aliens.clear();
        int padding = 10;
        int rowWidth = (GamePanel.PANEL_WIDTH-4*Alien.WIDTH)/(Alien.WIDTH+padding);
        int rows = 5;
        int startX = 2*Alien.WIDTH;
        int startY = 50;
        topLeft = new Position(startX, startY);
        for(int x = 0; x < rowWidth; x++) {
            for(int y = 0; y < rows; y++) {
                aliens.add(new Alien(new Position(startX+x*(Alien.WIDTH+padding),
                                                 startY+y*(Alien.HEIGHT+padding)),objectManager,y%5));
            }
        }
    }

    /**
     * Gets the amount to move all the aliens and then applies to them all.
     * The aliens are removed if they were destroyed since last update.
     * They are also updated to manage their individual attack timers.
     * Finally, the state information for moving the whole group of aliens is
     * updated to correctly change direction.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        lowestAlienY = 0;
        Position moveOffset = new Position(curMoveDir);
        moveOffset.multiply(MOVE_SPEED);
        aliens.forEach(alien -> {alien.position.add(moveOffset);
                                 if(alien.position.y > lowestAlienY) {
                                     lowestAlienY = alien.position.y;
                                 }});
        for(int i = 0; i < aliens.size(); i++) {
            if(aliens.get(i).isExpired()) {
                aliens.remove(i);
                i--;
            } else {
                aliens.get(i).update(deltaTime);
            }
        }
        topLeft.add(moveOffset);
        if((curMoveDir.equals(Position.LEFT) && topLeft.x <= 2*Alien.WIDTH)
        || (curMoveDir.equals(Position.RIGHT) && topLeft.x >= 4*Alien.WIDTH)) {
            curMoveDir = new Position(Position.DOWN);
        } else if(curMoveDir.equals(Position.DOWN)) {
            downNextTrigger -= moveOffset.y;
            if(downNextTrigger <= 0) {
                curMoveDir = new Position(lastLeft ? Position.RIGHT : Position.LEFT);
                lastLeft = !lastLeft;
                downNextTrigger = 20;
            }
        }
    }

    /**
     * Draws all the aliens.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        aliens.forEach(a -> a.paint(g));
    }

    /**
     * Gets the total number of active aliens.
     *
     * @return The number of aliens.
     */
    public int getAlienCount() {
        return aliens.size();
    }

    /**
     * Tests all aliens to check if the projectile specified
     * has collided with any of the aliens.
     *
     * @param projectile The projectile to test against all the aliens.
     * @return The alien that was hit or null.
     */
    public Rectangle getCollision(Projectile projectile) {
        for (Alien alien : aliens) {
            if(alien.isIntersecting(projectile)) {
                return alien;
            }
        }
        return null;
    }

    /**
     * Gets the lowest alien position for checking game over conditions.
     *
     * @return The y coordinate of the lowest alien.
     */
    public int getLowestAlienY() {
        return lowestAlienY;
    }
}
