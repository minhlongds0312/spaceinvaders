package invaders.entities;

import java.io.File;

import invaders.ObjectInterfaces.FastProjectileStrategy;
import invaders.ObjectInterfaces.ProjectileStrategy;
import invaders.ObjectInterfaces.SlowProjectileStrategy;
import invaders.engine.GameEngine;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Enemy implements Moveable, Damagable, Renderable {

    private final Vector2D position;
    private final Image image;
    private boolean removed = false;
    private final double width = 25;
    private final double height = 30;
    private boolean right = true;
    private boolean shoot = true;
    private String projectileType;
    private double speed = 1;
    private double projectileSpeed;
    private ProjectileStrategy projectileStrategy;

    public static class Builder {
        private Vector2D position;
        private String projectileType;

        public Builder withPosition(Vector2D position) {
            this.position = position;
            return this;
        }

        public Builder withProjectileType(String projectileType) {
            this.projectileType = projectileType;
            return this;
        }

        public Enemy build() {
            return new Enemy(position, projectileType);
        }
    }

    public Enemy(Vector2D position, String projectileType) {
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, false, true);
        this.position = position;
        this.projectileType = projectileType;
        if ("slow_straight".equals(projectileType)) {
            this.projectileStrategy = new SlowProjectileStrategy();
        } else if ("fast_straight".equals(projectileType)) {
            this.projectileStrategy = new FastProjectileStrategy();
        }
        this.projectileSpeed = projectileStrategy.getProjectileSpeed();

    }

    public void shoot() {
        this.shoot = true;
    }

    public boolean status() {
        return this.shoot;
    }
    public double getProjectileSpeed() {
        return this.projectileSpeed;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void takeDamage(double amount) {
        this.removed = true;
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return !this.removed;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
        this.position.setY(this.position.getY() + 30);
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - speed);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + speed);
    }

    @Override
    public void tick() {
        if (right) {
            this.right();
            if (this.position.getX() >= 599 - this.width) {
                this.down();
                right = false;
            }
        } else {
            this.left();
            if (this.position.getX() <= 0) {
                this.down();
                right = true;
            }
        }
    }

    public void speedUp() {
        this.speed += 0.1;
    }

}
