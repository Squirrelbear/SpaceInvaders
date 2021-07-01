import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * GamePanel class:
 * Shows the current game state and passes information to the correct objects.
 */
public class GamePanel extends JPanel implements ActionListener {
    /**
     * Time between updates.
     */
    public static final int TIME_INTERVAL = 20;
    /**
     * Height of the panel.
     */
    public static final int PANEL_HEIGHT = 600;
    /**
     * Width of the panel.
     */
    public static final int PANEL_WIDTH = 400;
    /**
     * The object manager that controls all the individual elements of the game.
     */
    private ObjectManager objectManager;
    /**
     * The timer used to force updates to happen at fixed intervals.
     */
    private Timer gameTimer;
    /**
     * A reference to the player to pass input to.
     */
    private Player player;
    /**
     * The current score.
     */
    private int score;
    /**
     * When true the game has ended as a win or loss represented by the gameOverMessage.
     */
    private boolean gameOver;
    /**
     * Shows the message when the game has ended that will be set as either a win or loss.
     */
    private String gameOverMessage;

    /**
     * Configures the game ready to play and starts it right away.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        objectManager = new ObjectManager(this);
        player = objectManager.getPlayer();
        gameTimer = new Timer(TIME_INTERVAL, this);
        gameTimer.start();
        gameOver = false;
    }

    /**
     * Draws all the game elements. These are mostly contained in the objectManager.
     * Then all the text elements for lives, score, and if necessary game over are drawn.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        super.paint(g);
        objectManager.paint(g);
        drawScore(g);
        drawLives(g);
        if(gameOver) {
            drawGameOver(g);
        }
    }

    /**
     * Handles the input by checking first for Escape to quit, R to restart,
     * and otherwise passes the input handling to the player.
     *
     * @param keyCode The key that was pressed or released.
     * @param isPressed True indicates the key is pressed, false indicates released.
     */
    public void handleInput(int keyCode, boolean isPressed) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(keyCode == KeyEvent.VK_R && isPressed) {
            objectManager.reset();
            score = 0;
            gameOver = false;
        } else {
            player.handleInput(keyCode, isPressed);
        }
    }

    /**
     * Increases the score by the amount specified.
     *
     * @param amount Amount to increase the score by.
     */
    public void increaseScore(int amount) {
        score += amount;
    }

    /**
     * Called by the gameTimer on regular intervals. If the
     * game is not ended it will update the game via the object manager.
     * Then checks for game over state changes and updates as necessary.
     *
     * @param e Information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!gameOver) {
            objectManager.update(TIME_INTERVAL);
            if(player.getLives() == 0) {
                gameOver = true;
                gameOverMessage = "You lost all lives! R to Restart.";
            } else if(objectManager.getAlienCount() == 0) {
                gameOver = true;
                gameOverMessage = "You won! R to Restart.";
            } else if(objectManager.getAlienTooFarDown()) {
                gameOver = true;
                gameOverMessage = "You lost by Invasion! R to Restart.";
            }
        }
        repaint();
    }

    /**
     * Draws the score centred at the top of the panel.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreStr = "Score: " + score;
        int strWidth = g.getFontMetrics().stringWidth(scoreStr);
        g.drawString(scoreStr, PANEL_WIDTH/2-strWidth/2, 40);
    }

    /**
     * Draws the lives left aligned in the top left corner.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawLives(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String livesStr = "Lives: " + player.getLives();
        g.drawString(livesStr, 15, 40);
    }

    /**
     * Draws a background with game over message centred in the middle of the panel.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,PANEL_HEIGHT/2-20, PANEL_WIDTH, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int strWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.drawString(gameOverMessage, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT/2+10);
    }
}
