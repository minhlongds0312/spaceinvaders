package invaders.engine;

import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import invaders.GameObject;
import invaders.ObjectInterfaces.*;
import invaders.entities.*;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import invaders.engine.ConfigReader;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private List<Renderable> renderables;
	private List<PlayerProjectile> projectiles;
	private List<EnemyProjectile> enemyProjectiles;
	private List<Enemy> enemyList;
	private Player player;
	private boolean left;
	private boolean right;
	private boolean gameOver = false;
	private double enemySize = 0;

	public GameEngine(String config) {
		// read the config here
		renderables = new ArrayList<Renderable>();
		projectiles = new ArrayList<PlayerProjectile>();
		enemyProjectiles = new ArrayList<EnemyProjectile>();
		enemyList = new ArrayList<Enemy>();
		ConfigReader configReader = new ConfigReader(config);
		JSONArray enemies = configReader.getEnemies();
		JSONObject JSONplayer = configReader.getPlayer();
		Long playerSpeed = (Long) JSONplayer.get("speed");
		Long playerHealth = (Long) JSONplayer.get("lives");
		Long playerPositionX = (Long) ((JSONObject) JSONplayer.get("position")).get("x");
		Long playerPositionY = (Long) ((JSONObject) JSONplayer.get("position")).get("y");
		String playerColour = (String) JSONplayer.get("colour");
		player = new Player(new Vector2D(playerPositionX, playerPositionY), playerColour, playerSpeed, playerHealth);
		renderables.add(player);
		// add the enemies to renderables
		for (Object obj : enemies) {
			JSONObject jsonEnemy = (JSONObject) obj;
			Long positionX = (Long) ((JSONObject) jsonEnemy.get("position")).get("x");
			Long positionY = (Long) ((JSONObject) jsonEnemy.get("position")).get("y");
			String projectileType = (String) jsonEnemy.get("projectile");
			Enemy enemy = new Enemy.Builder().withPosition(new Vector2D(positionX, positionY))
					.withProjectileType(projectileType).build();
			renderables.add(enemy);
			enemyList.add(enemy);
			enemySize++;
		}
		System.out.println(enemySize);
		JSONArray bunkers = configReader.getBunkers();
		for (Object obj : bunkers) {
			JSONObject jsonBunker = (JSONObject) obj;
			Long positionX = (Long) ((JSONObject) jsonBunker.get("position")).get("x");
			Long positionY = (Long) ((JSONObject) jsonBunker.get("position")).get("y");
			Long width = (Long) ((JSONObject) jsonBunker.get("size")).get("x");
			Long height = (Long) ((JSONObject) jsonBunker.get("size")).get("y");
			Bunker bunker = new Bunker.Builder().withPosition(new Vector2D(positionX, positionY))
					.withSize(width, height).build();
			renderables.add(bunker);
		}
	}

	/**
	 * Updates the game/simulation
	 */
	public void update() {
		movePlayer();
		moveProjectiles();
		moveEnemy();
		alienShoot();
		Factory Pfactory = new PlayerProjectileFactory();
		// shoot if the player has shot
		if (player.shootStatus() && !this.gameOver && projectiles.size() < 1) {
			PlayerProjectile projectile = (PlayerProjectile) Pfactory
					.createProjectile(player.getPosition().getX() + player.getWidth() / 2, player.getPosition().getY(),
							player.getProjectileSpeed());
			projectiles.add(projectile);
			player.shootDone();
		}
		// Check for collision between playerprojectile and objects
		for (PlayerProjectile projectile : projectiles) {
			for (Renderable obj : renderables) {
				if (obj instanceof Enemy) {
					for (PlayerProjectile projectile2 : projectiles) {
						if (projectile2.getPosition().getX() >= obj.getPosition().getX()
								&& projectile2.getPosition().getX() <= obj.getPosition().getX() + obj.getWidth()
								&& projectile2.getPosition().getY() >= obj.getPosition().getY()
								&& projectile2.getPosition().getY() <= obj.getPosition().getY() + obj.getHeight()) {
							projectile2.setDelete();
							((Enemy) obj).takeDamage(1);
							speedUp();
						}
					}
				}
				if (obj instanceof Bunker) {
					for (PlayerProjectile projectile2 : projectiles) {
						if (projectile2.getPosition().getX() >= obj.getPosition().getX()
								&& projectile2.getPosition().getX() <= obj.getPosition().getX() + obj.getWidth()
								&& projectile2.getPosition().getY() >= obj.getPosition().getY()
								&& projectile2.getPosition().getY() <= obj.getPosition().getY() + obj.getHeight()) {
							projectile2.setDelete();
							((Bunker) obj).takeDamage(1);
						}
					}
				}
			}
		}
		// Check for collision between enemyprojectile and objects
		for (EnemyProjectile projectile : enemyProjectiles) {
			if (projectile.getPosition().getX() >= player.getPosition().getX()
					&& projectile.getPosition().getX() <= player.getPosition().getX() + player.getWidth()
					&& projectile.getPosition().getY() >= player.getPosition().getY()
					&& projectile.getPosition().getY() <= player.getPosition().getY() + player.getHeight()) {
				projectile.setDelete();
				player.takeDamage(1);
			}
			for (Renderable bunk : renderables) {
				if (bunk instanceof Bunker) {
					if (projectile.getPosition().getX() >= bunk.getPosition().getX()
							&& projectile.getPosition().getX() <= bunk.getPosition().getX() + bunk.getWidth()
							&& projectile.getPosition().getY() >= bunk.getPosition().getY()
							&& projectile.getPosition().getY() <= bunk.getPosition().getY() + bunk.getHeight()) {
						projectile.setDelete();
						((Bunker) bunk).takeDamage(1);
					}
				}
			}
			for (PlayerProjectile pp : projectiles) {
				if (projectile.getPosition().getX() >= pp.getPosition().getX()
						&& projectile.getPosition().getX() <= pp.getPosition().getX() + pp.getWidth()
						&& projectile.getPosition().getY() >= pp.getPosition().getY()
						&& projectile.getPosition().getY() <= pp.getPosition().getY() + pp.getHeight()) {
					projectile.setDelete();
					pp.setDelete();
				}
			}
		}
		// check for collision between enemy and player and bunker
		for (Renderable enemy : renderables) {
			if (enemy instanceof Enemy) {
				if (enemy.getPosition().getX() >= player.getPosition().getX()
						&& enemy.getPosition().getX() <= player.getPosition().getX() + player.getWidth()
						&& enemy.getPosition().getY() >= player.getPosition().getY()
						&& enemy.getPosition().getY() <= player.getPosition().getY() + player.getHeight()
						|| enemy.getPosition().getY() >= 799) {
					this.gameOver = true;
				}
				for (Renderable bunk : renderables) {
					if (bunk instanceof Bunker) {
						if (enemy.getPosition().getX() >= bunk.getPosition().getX()
								&& enemy.getPosition().getX() <= bunk.getPosition().getX() + bunk.getWidth()
								&& enemy.getPosition().getY() >= bunk.getPosition().getY()
								&& enemy.getPosition().getY() <= bunk.getPosition().getY() + bunk.getHeight()) {
							((Bunker) bunk).takeDamage(3);
						}
					}
				}
			}
		}

		if (player.getHealth() == 0 || allEnemyDead()) {
			this.gameOver = true;
		}
		// ensure that renderable foreground objects don't go off-screen
		for (Renderable ro : renderables) {
			if (!ro.getLayer().equals(Renderable.Layer.FOREGROUND)) {
				continue;
			}
			if (ro.getPosition().getX() + ro.getWidth() >= 640) {
				ro.getPosition().setX(639 - ro.getWidth());
			}

			if (ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if (ro.getPosition().getY() + ro.getHeight() >= 800) {
				ro.getPosition().setY(800 - ro.getHeight());
			}

			if (ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
	}

	private void moveEnemy() {
		for (Renderable ro : renderables) {
			if (ro instanceof Enemy) {
				((Enemy) ro).tick();
			}
		}
	}

	public boolean allEnemyDead() {
		for (Renderable enemy : renderables) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isAlive()) {
					return false;
				}
			}
		}
		return true;
	}

	public List<Renderable> getRenderables() {
		return renderables;
	}

	public void leftReleased() {
		this.left = false;
	}

	public List<PlayerProjectile> getProjectiles() {
		return projectiles;
	}

	public List<EnemyProjectile> getEnemyProjectiles() {
		return enemyProjectiles;
	}

	public void rightReleased() {
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}

	public void rightPressed() {
		this.right = true;
	}

	public boolean gameStatus() {
		return this.gameOver;
	}

	public boolean shootPressed() {
		player.shoot();
		return true;
	}

	private void movePlayer() {
		if (left) {
			player.left();
		}

		if (right) {
			player.right();
		}
	}

	public void speedUp() {
		for (Renderable ro : renderables) {
			if (ro instanceof Enemy) {
				((Enemy) ro).speedUp();
			}
		}
	}

	public void alienShoot() {
		Factory Efactory = new EnemyProjectileFactory();
		List<Enemy> shootingAliens = new ArrayList<Enemy>();
		for (Enemy alien : enemyList) {
			if (alien.isAlive()) {
				shootingAliens.add(alien);
			}
		}
		Collections.shuffle(shootingAliens);

		if (this.enemyProjectiles.size() < 1) { //we're letting 3 random aliens shoot per interval, therefore if this number is more than 1, more than 3 aliens are shooting.
			alienShootDelay(shootingAliens, Efactory, 0); //this the interval where the 3 aliens shoot 
		}
	}

	private void alienShootDelay(List<Enemy> shootingAliens, Factory Efactory, int index) {
		if (index >= 3 || index >= shootingAliens.size()) {
			return;
		}

		Enemy alien = shootingAliens.get(index);
		EnemyProjectile projectile = (EnemyProjectile) Efactory.createProjectile(
				alien.getPosition().getX() + alien.getWidth() / 2,
				alien.getPosition().getY() + alien.getHeight(), alien.getProjectileSpeed());
		enemyProjectiles.add(projectile);

		//random part of the shooting intervals
		Random rand = new Random();
		double randomDelay = 0.01 + (1 - 0.01) * rand.nextDouble();

		PauseTransition pause = new PauseTransition(Duration.seconds(randomDelay));
		pause.setOnFinished(event -> {
			alienShootDelay(shootingAliens, Efactory, index + 1);
		});
		pause.play();
	}

	public double enemyProjectilesSize() {
		return this.enemyProjectiles.size();
	}

	private void moveProjectiles() {
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).tick();
			if (projectiles.get(i).getPosition().getY() < 5) {
				projectiles.get(i).setDelete();
			}
		}
		for (int i = 0; i < enemyProjectiles.size(); i++) {
			enemyProjectiles.get(i).tick();
			if (enemyProjectiles.get(i).getPosition().getY() > 800) {
				enemyProjectiles.get(i).setDelete();
			}
		}
	}

	public double score() {
		double activeEnemies = 0;
		for (Renderable enemy : renderables) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isAlive()) {
					activeEnemies++;
				}
			}
		}
		return (enemySize - activeEnemies);
	}

}
