package invaders.entities;

import java.io.File;

import invaders.logic.Damagable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import invaders.ObjectInterfaces.*;

public class Bunker implements Damagable, Renderable {

    private final Vector2D position;
    private Image image;
    private double health = 3;
    private final double width;
    private final double height;
    private boolean removed = false;
    private BunkerState state;
    private final Image FULL_HEALTH_IMAGE;
    private final Image MEDIUM_HEALTH_IMAGE;
    private final Image LOW_HEALTH_IMAGE;

    private interface BunkerState {
        Image getImage(double width, double height);

        boolean isRemoved();

        BunkerState takeDamage();
    }

    class FullHeathState implements BunkerState {
        @Override
        public Image getImage(double width, double height) {
            return FULL_HEALTH_IMAGE;
        }

        @Override
        public boolean isRemoved() {
            return false;
        }

        @Override
        public BunkerState takeDamage() {
            return new MediumHealthState();
        }
    }

    class MediumHealthState implements BunkerState {
        @Override
        public Image getImage(double width, double height) {
            return MEDIUM_HEALTH_IMAGE;
        }

        @Override
        public boolean isRemoved() {
            return false;
        }

        @Override
        public BunkerState takeDamage() {
            return new LowHealthState();
        }
    }

    class LowHealthState implements BunkerState {
        @Override
        public Image getImage(double width, double height) {
            return LOW_HEALTH_IMAGE;
        }

        @Override
        public boolean isRemoved() {
            return false;
        }

        @Override
        public BunkerState takeDamage() {
            return new RemovedState();
        }
    }

    class RemovedState implements BunkerState {
        @Override
        public Image getImage(double width, double height) {
            return null;
        }

        @Override
        public boolean isRemoved() {
            return true;
        }

        @Override
        public BunkerState takeDamage() {
            return this;
        }
    }

    public static class Builder {
        private Vector2D position;
        private double width;
        private double height;

        public Builder withPosition(Vector2D position) {
            this.position = position;
            return this;
        }

        public Builder withSize(double width, double height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Bunker build() {
            return new Bunker(position, width, height);
        }
    }

    public Bunker(Vector2D position, double width, double height) {
        this.width = width;
        this.height = height;
        this.state = new FullHeathState();
        this.FULL_HEALTH_IMAGE = new Image(new File("src/main/resources/bunker3.png").toURI().toString(), width,
                height, false, true);
        this.MEDIUM_HEALTH_IMAGE = new Image(new File("src/main/resources/bunker2.png").toURI().toString(), width,
                height, false, true);
        this.LOW_HEALTH_IMAGE = new Image(new File("src/main/resources/bunker.png").toURI().toString(), width, height,
                false, true);
        this.position = position;
    }

    @Override
    public Image getImage() {
        return this.state.getImage(width, height);
        // return this.image;
    }

    public boolean status() {
        return this.state.isRemoved();
        // return this.removed;
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
        this.state = this.state.takeDamage();
        // this.health -= amount;
        // this.change();
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

}
