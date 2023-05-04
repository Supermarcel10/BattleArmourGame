package game.objects.pickup;

import game.objects.shot.ShotStyle;
import game.objects.tank.Player;
import game.objects.shot.ShotType;

import java.util.HashMap;


/**
 * Enum for the different types of pickups.
 */
public enum PickupType {
	// TODO: Add on screen display for the pickup.
	QUAD_SHOT(10, 3),
	DOUBLE_DAMAGE(8),
	SHIELD(20),
	SPEED_BOOST(4),
	FAST_SHOT(8),
	BULLET_PROPULSION(4),
	PENETRATING_BULLETS(10, 5),
	EXPLOSIVE_BULLETS(10, 2);

	public final int duration;
	public int bulletCount;

	PickupType(int duration) {
		this.duration = duration;
	}

	PickupType(int duration, int bulletCount) {
		this.duration = duration;
		this.bulletCount = bulletCount;
	}

	/**
	 * Applies the perk to the player.
	 * @param player The player to apply the perk to.
	 */
	public void applyPerk(Player player) {
		switch (this) {
			case QUAD_SHOT -> player.shotStyle.put(ShotStyle.QUAD, bulletCount);
			case DOUBLE_DAMAGE -> player.shotDamage *= 2;
			case SHIELD -> player.shielded = true;
			case SPEED_BOOST -> player.speed += 0.2f;
			case FAST_SHOT -> player.changeShootingDelay(350);
			case BULLET_PROPULSION -> player.shotSpeed = 300f;
			case PENETRATING_BULLETS -> player.availableShots.add(new HashMap<>(){{
				put(ShotType.PENETRATING, bulletCount);
			}});
			case EXPLOSIVE_BULLETS -> player.availableShots.add(new HashMap<>(){{
				put(ShotType.EXPLOSIVE, bulletCount);
			}});
		}
	}

	/**
	 * Removes the perk from the player.
	 * @param player The player to remove the perk from.
	 */
	public void removePerk(Player player) {
		switch (this) {
			case QUAD_SHOT -> player.shotStyle.remove(ShotStyle.QUAD);
			case DOUBLE_DAMAGE -> player.shotDamage /= 2;
			case SHIELD -> player.shielded = false;
			case SPEED_BOOST -> player.speed -= 0.2f;
			case FAST_SHOT -> player.changeShootingDelay(500);
			case BULLET_PROPULSION -> player.shotSpeed = 150f;
			case PENETRATING_BULLETS -> player.availableShots.removeIf(shot -> shot.containsKey(ShotType.PENETRATING));
			case EXPLOSIVE_BULLETS -> player.availableShots.removeIf(shot -> shot.containsKey(ShotType.EXPLOSIVE));
		}
	}
}
