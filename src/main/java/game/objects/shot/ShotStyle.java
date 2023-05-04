package game.objects.shot;


/**
 * Enum for the different shooting styles.
 */
public enum ShotStyle {
	NORMAL,
	QUAD(3);

	public int numberOfShots;

	ShotStyle() {}

	ShotStyle(int numberOfShots) {
		this.numberOfShots = numberOfShots;
	}
}
