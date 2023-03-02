package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static game.Config.resolution;


public class Player extends Walker {
	float speed = 8f;
	Vec2 moveDir = new Vec2(0,0);
//	final BodyImage image = new BodyImage("data/img/player.png", 4);
	World world;
	DynamicBody player;

	public Player(World world) {
		super(world);

		this.world = world;

		spawn();
	}


	public void spawn() {
		float scaleFactor = resolution.x / 1920;

		// Spawn the player
		Shape shape = new BoxShape(1 * scaleFactor,2 * scaleFactor);
		player = new DynamicBody(world, shape);
		player.setPosition(new Vec2(4 * scaleFactor,10 * scaleFactor));
//		player.addImage(image);
	}


	public void setMoveDir(Vec2 moveDir) {
		this.moveDir = moveDir;
	}


	public void update(long elapsedNanos) {
		// Update the position of the player object based on the current movement direction
		Vec2 position = player.getPosition();
		position = position.add(moveDir.mul(speed * (elapsedNanos / 1000000000.0f)));
		player.setPosition(position);
	}
}
