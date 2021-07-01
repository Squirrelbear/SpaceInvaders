import java.awt.*;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * Alien class:
 * Defines an alien that will be controlled by the Alien Manager.
 * Aliens die after they are hit by a projectile, and can fire
 * projectiles of their own on a random interval of 30s or less.
 */
public class Alien extends Rectangle implements CollidableObject {
    /**
     * Defines the number of different alien types and the colours associated with them.
     */
    private final Color[] ALIEN_TYPE_COLOURS = {Color.RED, Color.BLUE, Color.YELLOW, Color.CYAN, Color.ORANGE};
    /**
     * Width of the alien.
     */
    public static final int WIDTH = 20;
    /**
     * Height of the alien.
     */
    public static final int HEIGHT = 20;
    /**
     * Used to determine if the Alien is ready to be destroyed.
     */
    private boolean isExpired;
    /**
     * Reference to the objectManager for creating projectiles and applying score on death.
     */
    private ObjectManager objectManager;
    /**
     * The colour to show for this alien based on the type.
     */
    private Color drawColor;
    /**
     * The type used to determine both colour and amount of score on destruction.
     */
    private int type;
    /**
     * Timer to track how long between firing projectiles.
     */
    private ActionTimer fireTimer;

    /**
     * Creates an alien with the specified properties ready to start playing.
     *
     * @param position Initial position to spawn the alien at.
     * @param objectManager Reference to the ObjectManager for spawning projectiles and applying score changes.
     * @param type The type to make the alien. Changes colour and score associated.
     */
    public Alien(Position position, ObjectManager objectManager, int type) {
        super(position, WIDTH, HEIGHT);
        isExpired = false;
        this.objectManager = objectManager;
        this.type = type;
        drawColor = ALIEN_TYPE_COLOURS[type];
        fireTimer = new ActionTimer((int)(Math.random()*20000));
    }

    /**
     * Updates the timer for firing shots. When it triggers, a
     * projectile is fired down and the timer is reset with a
     * new random interval.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        fireTimer.update(deltaTime);
        if(fireTimer.isTriggered()) {
            objectManager.addProjectile(this);
            fireTimer.setTimer((int)(Math.random()*20000));
        }
    }

    /**
     * Draws the alien with the alien's colour based on type.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(drawColor);
        g.fillRect(position.x, position.y, width, height);
    }

    /**
     * Checks if the alien has been destroyed.
     *
     * @return True when the alien should be destroyed.
     */
    public boolean isExpired() {
        return isExpired;
    }

    /**
     * When hit with a projectile the alien should expire
     * and increase the score relative to the type of the alien.
     */
    @Override
    public void hit() {
        isExpired = true;
        objectManager.increaseScore((type+1)*3);
    }
}
