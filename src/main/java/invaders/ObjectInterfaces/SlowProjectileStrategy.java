package invaders.ObjectInterfaces;

import invaders.entities.Enemy;

public class SlowProjectileStrategy implements ProjectileStrategy {

    @Override
    public double getProjectileSpeed() {
        return 4.0; // Example fast speed
    }
}
