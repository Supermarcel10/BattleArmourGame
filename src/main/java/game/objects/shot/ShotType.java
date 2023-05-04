package game.objects.shot;


/**
 * Enum for the different types of shots.
 */
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
