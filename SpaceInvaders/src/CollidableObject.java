/**
 * Space Invaders
 * Author: Peter Mitchell (2021)
 *
 * CollidableObject interface:
 * Used for the objects that can be collided with
 * to provide a hit() method to be called when hit.
 */
public interface CollidableObject {
    /**
     * Called when a projectile hits a CollidableObject.
     */
    void hit();
}
