import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * Player class:
 * Defines a player that can be moved left/right and fire projectiles with space.
 */
public class Player extends Rectangle implements CollidableObject {
    /**
     * Width of the player.
     */
    private static final int WIDTH = 30;
    /**
     * Height of the player.
     */
    private static final int HEIGHT = 20;

    /**
     * Reference to the object manager for spawning projectiles.
     */
    private ObjectManager objectManager;
    /**
     * When true this will fire a shot in the next update.
     */
    private boolean fireShot;
    /**
     * Status of the keys for left/right to determine if movement should happen during updates.
     */
    private boolean keyLeftIsPressed, keyRightIsPressed;
    /**
     * The magnitude of movement translation for left/right movement.
     */
    private final int moveRate = 10;
    /**
     * Start position to allow resetting.
     */
    private Position startPosition;
    /**
     * Remaining lives that the player has.
     */
    private int lives;

    /**
     * Creates the player and prepares them with a default 3 lives.
     *
     * @param objectManager Reference to the ObjectManager to create projectiles.
     */
    public Player(ObjectManager objectManager) {
        super(new Position(GamePanel.PANEL_WIDTH/2-WIDTH/2,GamePanel.PANEL_HEIGHT-HEIGHT),WIDTH,HEIGHT);
        this.objectManager = objectManager;
        this.startPosition = new Position(position);
        keyLeftIsPressed = false;
        keyRightIsPressed = false;
        fireShot = false;
        lives = 3;
    }

    /**
     * Fires a shot if necessary. Moves left/right if input was applied for left/right movement.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        if(fireShot) {
            fireShot = false;
            objectManager.addProjectile(this);
        }
        if(keyLeftIsPressed) {
            moveWithinBounds(new Position(-moveRate,0), GamePanel.PANEL_WIDTH-width, GamePanel.PANEL_HEIGHT);
        }
        if(keyRightIsPressed) {
            moveWithinBounds(new Position(moveRate,0), GamePanel.PANEL_WIDTH-width, GamePanel.PANEL_HEIGHT);
        }
    }

    /**
     * Resets all properties back to defaults with 3 lives.
     */
    public void reset() {
        keyLeftIsPressed = false;
        keyRightIsPressed = false;
        fireShot = false;
        lives = 3;
        position = new Position(startPosition);
    }

    /**
     * Pressing/Releasing Left/Right arrows updates with the correct movement state.
     * Pressing space will fire a shot during the next update.
     *
     * @param keyCode The key that was interacted with.
     * @param isPressed When true it indicates the key was pressed, false indicates it was released.
     */
    public void handleInput(int keyCode, boolean isPressed) {
        if(keyCode == KeyEvent.VK_LEFT) {
            keyLeftIsPressed = isPressed;
        } else if(keyCode == KeyEvent.VK_RIGHT) {
            keyRightIsPressed = isPressed;
        } else if(keyCode == KeyEvent.VK_SPACE && isPressed) {
            fireShot = true;
        }
    }

    /**
     * Gets the current number of lives. This will always be 0 or greater.
     *
     * @return The current number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Draws the player as a tank with simple geometry.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(new Color(19, 73, 0));
        g.drawRect(position.x, position.y, 5, height);
        g.drawRect(position.x+width-5, position.y, 5, height);
        g.fillRect(position.x + width/2-2, position.y+height/3, 4, height*2/3+1);
    }

    /**
     * When hit the player takes 1 life off.
     */
    @Override
    public void hit() {
        lives = Math.max(0,lives-1);
    }

    /**
     * Moves based on the translationVector, but clamps the movement within the bounds of the play space.
     *
     * @param translationVector Added to position to calculate the new position.
     */
    private void moveWithinBounds(Position translationVector, int maxX, int maxY) {
        int newX = position.x+translationVector.x;
        int newY = position.y+translationVector.y;
        if(newX < 0) newX = 0;
        else if(newX > maxX) newX = maxX;
        if(newY < 0) newY = 0;
        else if(newY > maxY) newY = maxY;
        position.setPosition(newX, newY);
    }
}
