package invaders.ObjectInterfaces;

import invaders.entities.EnemyProjectile;

public class EnemyProjectileFactory implements Factory {

    @Override
    public EnemyProjectile createProjectile(double startX, double startY, double speed) {
        EnemyProjectile EP = new EnemyProjectile(startX, startY);
        EP.setSpeed(speed);
        return EP; 
    }
}
