package game.objects.pickup;

import game.IO.AM;
import game.objects.shot.ShotStyle;
import game.objects.tank.Player;
import game.objects.shot.ShotType;

import java.util.HashMap;

import static game.MainGame.toCamelCase;


/**
 * Enum for the different types of pickups.
 */
public enum PickupType {
	// TODO: Add on screen display for the pickup.
	QUAD_SHOT("powerupLow", 10, 3),
	DOUBLE_DAMAGE("powerupLow", 8),
	SHIELD("powerupLow", 20),
	SPEED_BOOST("powerupLow", 4),
	FAST_SHOT("powerupLow", 8),
	BULLET_PROPULSION("powerupLow", 4),
	PENETRATING_BULLETS("powerupHigh", 10, 5);
//	EXPLOSIVE_BULLETS("powerupHigh", 10, 2);

	public final String image;

	public final String soundFile;
	public final int duration;
	public int bulletCount;

	PickupType(String soundFile, int duration) {
		this.soundFile = AM.miscSound.get(soundFile);
		this.image = AM.image.get(toCamelCase(this.toString()));
		this.duration = duration;
	}

	PickupType(String soundFile, int duration, int bulletCount) {
		this.soundFile = AM.miscSound.get(soundFile);
		this.image = AM.image.get(toCamelCase(this.toString()));
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
			case SHIELD -> player.addShield();
			case SPEED_BOOST -> {
				player.movePollingRate -= 1;
				player.currentMovePoll = Math.max(player.currentMovePoll - 1, 0);

			}
			case FAST_SHOT -> player.changeShootingDelay(350);
			case BULLET_PROPULSION -> player.shotSpeed = 1f;
			case PENETRATING_BULLETS -> player.availableShots.add(new HashMap<>(){{
				put(ShotType.PENETRATING, bulletCount);
			}});
//			case EXPLOSIVE_BULLETS -> player.availableShots.add(new HashMap<>(){{
//				put(ShotType.EXPLOSIVE, bulletCount);
//			}});
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
			case SHIELD -> player.removeShield();
			case SPEED_BOOST -> player.movePollingRate += 1;
			case FAST_SHOT -> player.changeShootingDelay(500);
			case BULLET_PROPULSION -> player.shotSpeed = 0.5f;
			case PENETRATING_BULLETS -> player.availableShots.removeIf(shot -> shot.containsKey(ShotType.PENETRATING));
//			case EXPLOSIVE_BULLETS -> player.availableShots.removeIf(shot -> shot.containsKey(ShotType.EXPLOSIVE));
		}
	}
}
