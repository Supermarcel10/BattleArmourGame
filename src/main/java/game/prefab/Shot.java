package game.prefab;

import city.cs.engine.*;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;


public class Shot extends Body {
	private static final float speed = 10f;
	private Vec2 travelDirection;

	public Shot(float speed, World world, Vec2 position, Vec2 travelDirection) {
		super(speed, world, position);
		this.travelDirection = travelDirection;
		spawn();
	}

	@Override
	public void spawn() {
		// TODO: Get player direction and make the pellet move in that direction.
		setMoveDirection(travelDirection);
		body = new DynamicBody(world, new CircleShape(0.5f * scaleFactor, position));
		// TODO: Fix stuff with the ghostly fixture.
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
