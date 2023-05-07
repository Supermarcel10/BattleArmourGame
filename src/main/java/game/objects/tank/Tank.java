package game.objects.tank;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import game.IO.AM;
import game.objects.block.Block;
import game.objects.pickup.Pickup;
import game.objects.abstractBody.DynamicBody;
import game.objects.pickup.PickupType;
import game.objects.shot.Shot;
import game.objects.shot.ShotStyle;
import game.objects.shot.ShotType;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static game.MainGame.*;


/**
 * The base class for all tanks.
 */
public class Tank extends DynamicBody {
	private static final PickupType[] pickupTypes = PickupType.values();
	private static final int PICKUP_CHANCE = 20;
	private static final Random random = new Random();

	public static float halfSize = scaledGridSize * scaleFactor * .8f;
	private static final Shape shape = new BoxShape(halfSize, halfSize);

	public static final String shieldImage = AM.image.get("playerShield");
	public boolean shielded = false;

	protected boolean canShoot = true;
	protected int shootingDelay = 500;
	protected final Timer shootingTimer = new Timer(shootingDelay, e -> canShoot = true);
	public int shotPollingSpeed = 1;
	public float shotSpeed = 50f;
	public int shotDamage = 1;
	public HashMap<ShotStyle, Integer> shotStyle = new HashMap<>();
	public List<HashMap<ShotType, Integer>> availableShots = new ArrayList<>();

	protected static String shootSound = AM.tankSound.get("shoot");
	protected static String damageSound, destroySound;

	protected int scoreValue;
	public int health;

	public Tank(Vec2 position) {
		super(position, shape);
		shootingTimer.setRepeats(false);
	}

	public Tank(int movePollingRate, Vec2 position) {
		super(movePollingRate, position, shape);
		shootingTimer.setRepeats(false);
	}

	/**
	 * Shoots a shot from the tank.
	 */
	public void shoot() {
		// Limit the tank shooting speed.
		if (!canShoot) {
			return;
		}

		canShoot = false;
		shootingTimer.restart();

		// Find the most powerful shot type available.
		AtomicReference<ShotType> shotType = new AtomicReference<>(ShotType.BASIC);

		if (availableShots != null) {
			for (HashMap<ShotType, Integer> shot : availableShots) {
				shot.keySet().forEach(key -> {
					if (shotType.get().ordinal() < key.ordinal()) shotType.set(key);
				});
			}

			// Reduce the amount of the shot type available.
			availableShots.forEach(shot -> {
				if (shot.containsKey(shotType.get())) {
					shot.put(shotType.get(), shot.get(shotType.get()) - 1);

					// If the shot type is out of ammo, remove it.
					if (shot.get(shotType.get()) <= 0) shot.remove(shotType.get());
				}
			});
		}

		// If the tank is in quad shot mode, create 4 shots instead of 1.
		if (shotStyle.containsKey(ShotStyle.QUAD)) {
			for (int i = 0; i < 4; i++) {
				new Shot(this.getPositionJBox(), new Vec2((float) Math.cos(i * Math.PI / 2), (float) Math.sin(i * Math.PI / 2)), this, shotSpeed, shotDamage, shotType.get());
			}

			// Reduce the amount of quad shots available.
			shotStyle.put(ShotStyle.QUAD, shotStyle.get(ShotStyle.QUAD) - 1);

			// If the quad shot is out of ammo, remove it.
			if (shotStyle.get(ShotStyle.QUAD) <= 0) shotStyle.remove(ShotStyle.QUAD);
		} else {
			new Shot(this.getPositionJBox(), radiansToVec2(this.getAngle()), this, shotSpeed, shotDamage, shotType.get());
		}

		// Play the shoot sound.
		soundHandler.play(shootSound);
	}

	public void addShield() {
		shielded = true;

		addImage(new BodyImage(shieldImage, halfSize * 3));
	}

	public void removeShield() {
		shielded = false;

		removeAllImages();
		addImage(new BodyImage(TankType.PLAYER.image, halfSize * 2));
	}

	/**
	 * Damages the tank.
	 * @param damage The amount of damage to deal.
	 * @param shooter The tank that shot the tank.
	 */
	public void damage(int damage, Tank shooter) {
		if (shielded) {
			removeShield();
			return;
		}

		health -= damage;

		// TODO: Add explosion effect.
		if (health <= 0) {
			if (shooter instanceof Player) {
				score += scoreValue;
				kills++;
			}

			// Play the tank dead sound && explode if the tank is an exploding tank.
			if (this instanceof Enemy e && e.type == TankType.EXPLODING) {
				explode();
			} else {
				soundHandler.play(AM.tankSound.get("tankDead"));
			}

			destroy();
			//noinspection SuspiciousMethodCalls
			enemies.remove(this);

			// Give a chance to spawn a pickup.
			if (random.nextInt(100) < PICKUP_CHANCE && this instanceof Enemy) {
				Random random = new Random();

				new Pickup(pickupTypes[random.nextInt(pickupTypes.length)], this.getPosition());
			}

			// Play the destroy sound if it exists.
			soundHandler.play(destroySound);
		} else soundHandler.play(damageSound);
	}

	public void explode() {
		destroy();

		soundHandler.play(AM.tankSound.get("tankExplode"));

		Vec2 position = getPosition();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				Block b = blocks[(int) position.x + hGridSize + x][(int) position.y + hGridSize + y];

				if (b != null) {
					b.damage(1, this);
				}
			}
		}
	}

	private boolean isNotDrivable(Block block) {
		return block != null && !block.type.isDrivable;
	}

	private boolean isBlocked(int x1, int y1, int x2, int y2, Vec2 newPosition) {
		Block adjacentBlockFloor = blocks[x1][y1];
		Block adjacentBlockCeil = blocks[x2][y2];

		return (isNotDrivable(adjacentBlockFloor) && isWithinDistance(newPosition, adjacentBlockFloor.getPosition(), .75f))
				|| (isNotDrivable(adjacentBlockCeil) && isWithinDistance(newPosition, adjacentBlockCeil.getPosition(), .75f));
	}

	public static float roundToNearestQuarter(float number) {
		double wholePart = Math.floor(number);
		double fractionPart = number - wholePart;

		if (fractionPart < 0.125) {
			fractionPart = 0.0;
		} else if (fractionPart < 0.375) {
			fractionPart = 0.25;
		} else if (fractionPart < 0.625) {
			fractionPart = 0.5;
		} else if (fractionPart < 0.875) {
			fractionPart = 0.75;
		} else {
			fractionPart = 1.0;
		}

		return (float) (wholePart + fractionPart);
	}

	public void updateMovement() {
		// Remove redundant calls
		if (moveDirection.equals(new Vec2(0, 0))) return;

		// Get the updated player object position based on the current movement direction and speed.
		Vec2 newPosition = getPosition().add(moveDirection.mul(0.25f));

		// If the player is moving into blocks, tanks or spawners then prevent them.
		Vec2 adjacentBlockPosRaw = new Vec2(getPosition().x + moveDirection.x + hGridSize, getPosition().y + moveDirection.y + hGridSize);
		Vec2 adjacentBlockPos = new Vec2(roundToNearestQuarter(adjacentBlockPosRaw.x), roundToNearestQuarter(adjacentBlockPosRaw.y));

		int floorX = (int) Math.floor(adjacentBlockPos.x);
		int ceilX = (int) Math.ceil(adjacentBlockPos.x);
		int floorY = (int) Math.floor(adjacentBlockPos.y);
		int ceilY = (int) Math.ceil(adjacentBlockPos.y);

		if (floorX == ceilX) {
			if (isBlocked(floorX, floorY, floorX, ceilY, newPosition)) return;
		} else if (floorY == ceilY) {
			if (isBlocked(floorX, floorY, ceilX, floorY, newPosition)) return;
		} else return;

		for (Spawn s : spawners) {
			if (isWithinDistance(newPosition, s.getPosition(), .75f)) return;
		}

		for (Tank tank : player) {
			if (tank == this) continue;
			if (tank != null && tank.health != 0 && isWithinDistance(newPosition, tank.getPosition(), .75f)) return;
		}

		for (Tank tank : enemies) {
			if (tank == this) continue;
			if (isWithinDistance(newPosition, tank.getPosition(), .75f)) return;
		}

		// If no collisions occur, move the player.
		setPosition(new Vec2(roundToNearestQuarter(newPosition.x), roundToNearestQuarter(newPosition.y)));
	}

	/**
	 * Changes the shooting delay of the tank.
	 */
	public void changeShootingDelay(int delay) {
		shootingTimer.setDelay(delay);
		shootingTimer.setInitialDelay(delay);
	}

	/**
	 * Sets the max health of the tank.
	 * @param maxHealth The max health of the tank.
	 */
	public void setMaxHealth(int maxHealth) {
		this.health = maxHealth;
	}
}
