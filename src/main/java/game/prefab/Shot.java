package game.prefab;

import city.cs.engine.*;
import game.objects.Tank;
import game.objects.abstractBody.Body;
import org.jbox2d.common.Vec2;

import javax.swing.*;


public class Shot extends Body {
	private static final float speed = 12f;
	protected Tank shooter;
	private Vec2 travelDirection;

	public Shot(float speed, World world, Vec2 position, Vec2 travelDirection, Tank shooter) {
		super(speed, world, position);
		this.shooter = shooter;
		this.travelDirection = travelDirection;
		spawn();
	}

	@Override
	public void spawn() {
		// Set the body's move direction.
		setMoveDirection(travelDirection);
		body = new DynamicBody(world, new CircleShape(0.5f * scaleFactor, position));
		body.applyImpulse(travelDirection.mul(speed));
		// TODO: Fix stuff with the ghostly fixture.
		// TODO: Fix accelerating the body.
//		GhostlyFixture fixture =  new GhostlyFixture(body, new CircleShape(0.5f * scaleFactor, position));
//		fixture.setDensity(0f);
		body.setFillColor(java.awt.Color.RED);
//		body.setGravityScale(0);
//		body.setBullet(true);
//		body.setClipped(true);

//		 Destroy the body after 10 seconds.
		new Timer(10000, e -> {
			if (body != null) {
				body.destroy();
			}
		}).start();
	}
}
