package game.prefab;

public enum ShotType {
	BASIC(),
	PENETRATING(3),
	EXPLOSIVE();

	public int numberOfPenetrations;

	ShotType() {}

	ShotType(int numberOfPenetrations) {
		this.numberOfPenetrations = numberOfPenetrations;
	}
}
