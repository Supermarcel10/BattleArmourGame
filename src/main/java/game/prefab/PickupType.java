package game.prefab;

public enum PickupType {
	QUAD_SHOT(10),
	DOUBLE_DAMAGE(8),
	SHIELD(20),
	SPEED_BOOST(4),
	FAST_SHOT(8),
	BULLET_PROPULSION(4),
	PENETRATING_BULLETS(10, 5),
	EXPLOSIVE_BULLETS(30, 2);

	public final int duration;
	public int bulletCount;

	PickupType(int duration) {
		this.duration = duration;
	}

	PickupType(int duration, int bulletCount) {
		this.duration = duration;
		this.bulletCount = bulletCount;
	}
}
