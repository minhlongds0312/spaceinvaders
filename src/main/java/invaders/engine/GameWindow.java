package invaders.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import invaders.entities.EntityViewImpl;
import invaders.entities.PlayerProjectile;
import invaders.entities.SpaceBackground;
import javafx.util.Duration;
import invaders.entities.Bunker;
import invaders.entities.Enemy;
import invaders.entities.EnemyProjectile;
import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameWindow {
    private final int width;
    private final int height;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;

    public GameWindow(GameEngine model, int width, int height) {
        this.width = width;
        this.height = height;
        this.model = model;
        pane = new Pane();
        scene = new Scene(pane, width, height, Color.BEIGE);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        entityViews = new ArrayList<EntityView>();

    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        model.update();
        // timeline for enemy shooting
        List<Renderable> renderables = model.getRenderables();
        Iterator<Renderable> renderableIterator = renderables.iterator();

        while (renderableIterator.hasNext()) {
            Renderable entity = renderableIterator.next();
            boolean notFound = true;
            Iterator<EntityView> viewIterator = entityViews.iterator();

            while (viewIterator.hasNext()) {
                EntityView view = viewIterator.next();
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    if (entity instanceof Enemy) {
                        if (!((Enemy) entity).isAlive()) {
                            pane.getChildren().remove(view.getNode());
                            viewIterator.remove();
                            renderableIterator.remove();
                        }
                    }
                    if (entity instanceof Bunker) {
                        if (((Bunker) entity).status()) {
                            pane.getChildren().remove(view.getNode());
                            viewIterator.remove();
                            renderableIterator.remove();
                        }
                    }
                    break;
                }
            }

            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }
        Iterator<PlayerProjectile> projectileIterator = model.getProjectiles().iterator();
        while (projectileIterator.hasNext()) {
            PlayerProjectile projectile = projectileIterator.next();
            boolean notFound = true;
            Iterator<EntityView> viewIterator = entityViews.iterator();
            while (viewIterator.hasNext()) {
                EntityView view = viewIterator.next();
                if (view.matchesEntity(projectile)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    if (projectile.status()) {
                        pane.getChildren().remove(view.getNode());
                        viewIterator.remove();
                        projectileIterator.remove();
                    }
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(projectile);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        Iterator<EnemyProjectile> enemyProjectileIterator = model.getEnemyProjectiles().iterator();
        while (enemyProjectileIterator.hasNext()) {
            EnemyProjectile projectile = enemyProjectileIterator.next();
            boolean notFound = true;
            Iterator<EntityView> viewIterator = entityViews.iterator();
            while (viewIterator.hasNext()) {
                EntityView view = viewIterator.next();
                if (view.matchesEntity(projectile)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    if (projectile.status()) {
                        pane.getChildren().remove(view.getNode());
                        viewIterator.remove();
                        enemyProjectileIterator.remove();
                    }
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(projectile);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }
        if (model.gameStatus()) {
            // remove all objects
            pane.getChildren().clear();
            Text gameOverText = new Text("Game Over");
            gameOverText.setFont(new Font(50)); // Set font size to 50, adjust as needed
            gameOverText.setFill(Color.RED); // Set color to red, adjust as needed
            gameOverText.xProperty().bind(pane.widthProperty().subtract(gameOverText.prefWidth(-1)).divide(2));
            gameOverText.yProperty().bind(pane.heightProperty().subtract(gameOverText.prefHeight(-1)).divide(2));
            pane.getChildren().add(gameOverText);
        }

    }

    public Scene getScene() {
        return scene;
    }

    public Pane getPane() {
        return pane;
    }

    public List<EntityView> getEntityViews() {
        return entityViews;
    }
}
