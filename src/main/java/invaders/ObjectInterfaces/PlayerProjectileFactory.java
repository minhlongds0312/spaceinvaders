package invaders.ObjectInterfaces;

import invaders.entities.PlayerProjectile;

public class PlayerProjectileFactory implements Factory {

    @Override
    public PlayerProjectile createProjectile(double startX, double startY, double speed) {
        return new PlayerProjectile(startX, startY);
    }
}
