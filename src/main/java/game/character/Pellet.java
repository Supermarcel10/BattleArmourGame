package game.character;

import city.cs.engine.*;
import game.character.abstractBody.Body;
import org.jbox2d.common.Vec2;


public class Pellet extends Body {
	public Pellet(float speed, World world, Vec2 position) {
		super(speed, world, position);
		spawn();
	}

	@Override
	public void spawn() {
		// TODO: Get player direction and make the pellet move in that direction.
		setMoveDirection(new Vec2(0, 0));
		body = new DynamicBody(world, new CircleShape(0.5f * scaleFactor, position));
		GhostlyFixture fixture =  new GhostlyFixture(body, new CircleShape(0.5f * scaleFactor, position));
		fixture.setDensity(0f);
		body.setFillColor(java.awt.Color.RED);
		body.setPosition(position);
		body.setGravityScale(0);
		body.setBullet(true);
		body.setClipped(true);
	}

	@Override
	public void collide() {
		// implementation here
	}
}
