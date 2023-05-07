package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.pickup.Pickup;
import game.objects.pickup.PickupType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static game.MainGame.*;


/**
 * The player class.
 */
public class Player extends Tank {
	public HashMap<PickupType, Integer[]> perks = new HashMap<>();

	private int playerIndex = -1;

	public Player(Vec2 position) {
		super(TankType.PLAYER.updateFrequency, position);
		setMaxHealth(TankType.PLAYER.health);
		addImage(new BodyImage(TankType.PLAYER.image, halfSize * 2));
		scoreValue = TankType.PLAYER.scoreValue;
	}

	/**
	 * Handle pickup logic.
	 * @param pickup The pickup object to be picked up.
	 */
	public void pickUp(@NotNull Pickup pickup) {
		// Add the perk to the perks list.
		if (pickup.type.bulletCount != 0) {
			perks.put(pickup.type, new Integer[]{pickup.type.duration, pickup.type.bulletCount});
		} else {
			perks.put(pickup.type, new Integer[]{pickup.type.duration});
		}

		// Remove the pickup from the world.
		pickup.destroy();

		new Timer(pickup.type.duration * 1000, e -> {
			// Remove the perk from the perks list.
			perks.remove(pickup.type);

			// Remove the perk from the player.
			pickup.type.removePerk(this);

			((Timer) e.getSource()).stop();
		}).start();

		// Apply the perk to the player.
		pickup.type.applyPerk(this);
	}

	@Override
	public void update() {
		super.update();

		// Play sound if the player is moving.
		if (playerIndex == -1) {
			playerIndex = Arrays.asList(player).indexOf(this);
		}

		if (Objects.equals(moveDirection, new Vec2(0, 0))) {
			soundHandler.stopPlayerMovement(playerIndex);
		} else {
			soundHandler.playPlayerMovement(playerIndex);
		}
	}
}
