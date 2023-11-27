package invaders.entities;

import java.io.File;

import invaders.ObjectInterfaces.Projectile;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PlayerProjectile implements Projectile, Moveable, Renderable {
    private final Image image;
    private final Vector2D position;
    private boolean delete = false;
    private final double width = 5;
    private final double height = 30;
    private final double speed = 2.5;

    public PlayerProjectile(double startX, double startY) {
         this.image = new Image(new File("src/main/resources/playerProjectile.png").toURI().toString(), width, height, false, false);
         this.position = new Vector2D(startX, startY);
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
    public void up() {
        this.position.setY(this.position.getY() - speed);
    }

    @Override
    public void down() {
        this.position.setY(this.position.getY() + speed);
    }

    @Override
    public void left() {
    }

    @Override
    public void right() {
    }

    public void setDelete(){
        this.delete = true;
    }
    public boolean status(){
        return this.delete;
    }

    public double getX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }

    public double getY() {
        return this.position.getY();
    }
    public void tick(){
        this.up();
    }
}
