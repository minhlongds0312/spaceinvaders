package invaders.entities;

import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;

public class Player implements Moveable, Damagable, Renderable {

    private final Vector2D position;
    private final Animator anim = null;
    private double health;
    private boolean shot;

    private final double width = 25;
    private final double height = 30;
    private final Image image;
    private double projectileSpeed = 2.5;
    private double speed;

    public Player(Vector2D position, String colour, double speed, double health) {
        if (colour.equals("red")) {
            this.image = new Image(new File("src/main/resources/playerRed.png").toURI().toString(), width, height,
                    false,
                    true);
        } else if (colour.equals("green")) {
            this.image = new Image(new File("src/main/resources/playerGreen.png").toURI().toString(), width, height,
                    false, true);
        } else {
            this.image = new Image(new File("src/main/resources/playerBlue.png").toURI().toString(), width, height,
                    false, true);
        }

        this.position = position;
        this.health = health;
        this.speed = speed;
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - speed);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + speed);
    }

    public void shoot() {
        this.shot = true;
    }

    public boolean shootStatus() {
        return this.shot;
    }

    public void shootDone() {
        this.shot = false;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void tick() {
        return;
    }

}
