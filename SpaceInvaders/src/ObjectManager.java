import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * ObjectManager class:
 * Manages all the objects in the scene including:
 * the player, the alien manager, the projectiles, and
 * any obstacles.
 */
public class ObjectManager {
    /**
     * Reference to the gamePanel for passing score updates.
     */
    private GamePanel gamePanel;
    /**
     * The player object.
     */
    private Player player;
    /**
     * The collection of aliens that are managed as a collective.
     */
    private AlienManager alienManager;
    /**
     * The projectiles that are currently active moving up or down.
     */
    private List<Projectile> projectiles;
    /**
     * Obstacles that block projectiles.
     */
    private List<Obstacle> obstacles;

    public ObjectManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        projectiles = new ArrayList<>();
        obstacles = new ArrayList<>();
        player = new Player(this);
        alienManager = new AlienManager(this);
        spawnObstacles();
    }

    /**
     * Resets all the object states back to default.
     */
    public void reset() {
        alienManager.reset();
        player.reset();
        projectiles.clear();
    }

    /**
     * Updates the player, aliens, and projectiles.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        player.update(deltaTime);
        alienManager.update(deltaTime);
        updateProjectiles(deltaTime);
    }

    /**
     * Draws the player, aliens, obstacles, and projectiles.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        obstacles.forEach(o -> o.paint(g));
        player.paint(g);
        alienManager.paint(g);
        projectiles.forEach(p -> p.paint(g));
    }

    /**
     * Counts the number of aliens currently remaining in the alien manager.
     *
     * @return The number of aliens that are currently active.
     */
    public int getAlienCount() {
        return alienManager.getAlienCount();
    }

    /**
     * Checks if the lowest alien is too low down near the player.
     *
     * @return True when the game should end due to an alien invasion.
     */
    public boolean getAlienTooFarDown() {
        return alienManager.getLowestAlienY() >= player.position.y - Alien.HEIGHT;
    }

    /**
     * Passes the increase score call up to the game panel.
     *
     * @param amount Amount to increase the score by.
     */
    public void increaseScore(int amount) {
        gamePanel.increaseScore(amount);
    }

    /**
     * Gets the player object.
     *
     * @return A reference to the player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Spawns one projectile using the object that it is being spawned from
     * as the way to determine direction and start position.
     *
     * @param spawnFromObject The object that is being spawned from.
     */
    public void addProjectile(Rectangle spawnFromObject) {
        projectiles.add(new Projectile(spawnFromObject, this));
    }

    /**
     * Tests the aliens, then the player, and then the obstacles
     * to check if there are collisions with the projectile.
     * Projectiles do not check collisions on their own type.
     * Eg, player projectiles do not hit players, and alien projectiles
     * do not hit the aliens.
     *
     * @param projectile The projectile to test for collisions.
     * @return A reference to the first object collided with, or null.
     */
    public Rectangle getCollision(Projectile projectile) {
        Rectangle parentObj = projectile.getParentObject();
        if(parentObj instanceof Player) {
            Rectangle alienCollision = alienManager.getCollision(projectile);
            if(alienCollision != null) {
                return alienCollision;
            }
        } else if(player.isIntersecting(projectile)) {
            return player;
        }
        for (Obstacle obstacle : obstacles) {
            if(obstacle.isIntersecting(projectile)) {
                return obstacle;
            }
        }
        return null;
    }

    /**
     * Updates the projectiles and if they have hit something
     * or left the screen to expire they will be removed.
     *
     * @param deltaTime Time since last update.
     */
    private void updateProjectiles(int deltaTime) {
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).update(deltaTime);
            if(projectiles.get(i).isExpired()) {
                projectiles.remove(i);
                i--;
            }
        }
    }

    private void spawnObstacles() {
        obstacles.add(new Obstacle(new Position(50,player.position.y - 100),50, 20));
        obstacles.add(new Obstacle(new Position(GamePanel.PANEL_WIDTH/2-25,player.position.y - 100),50, 20));
        obstacles.add(new Obstacle(new Position(GamePanel.PANEL_WIDTH-100,player.position.y - 100),50, 20));
    }
}
