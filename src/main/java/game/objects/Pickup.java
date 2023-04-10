package game.objects;

import game.objects.abstractBody.Body;
import game.prefab.PerkType;
import org.jbox2d.common.Vec2;

public class Pickup extends Body {
	PerkType type;

	public Pickup(PerkType type, Vec2 position) {
		super(position);
		this.type = type;
	}
}
