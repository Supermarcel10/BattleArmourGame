package game.objects;

import city.cs.engine.*;
import game.objects.abstractBody.Body;
import game.prefab.PerkType;
import game.prefab.Player;
import org.jbox2d.common.Vec2;

import static game.objects.Tank.halfSize;


public class Pickup extends Body implements CollisionListener {
	private static final Shape shape = new CircleShape(halfSize);
	public final PerkType type;

	public Pickup(PerkType type, Vec2 position) {
		super(position, shape);
		this.type = type;
		this.addCollisionListener(this);

		spawn();
	}

	public void spawn() {
		this.setPosition(new Vec2(((scaledGridSize * 2) * getPosition().x) * scaleFactor,
				((scaledGridSize * 2) * getPosition().y) * scaleFactor));
	}

	@Override
	public void collide(CollisionEvent e) {
		if (e.getOtherBody() instanceof Player p) p.pickUp(this);
	}
}
