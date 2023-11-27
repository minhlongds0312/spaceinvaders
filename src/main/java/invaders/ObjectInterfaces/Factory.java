package invaders.ObjectInterfaces;

public interface Factory {
    public Projectile createProjectile(double startX, double startY, double speed);
}
