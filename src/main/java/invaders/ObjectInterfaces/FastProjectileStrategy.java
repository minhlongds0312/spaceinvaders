package invaders.ObjectInterfaces;

import invaders.entities.Enemy;

public class FastProjectileStrategy implements ProjectileStrategy {
    @Override
    public double getProjectileSpeed() {
        return 2.0; // Example slow speed
    }
}