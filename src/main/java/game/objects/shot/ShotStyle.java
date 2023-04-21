package game.objects.shot;

public enum ShotStyle {
	NORMAL,
	QUAD(3);

	public int numberOfShots;

	ShotStyle() {}

	ShotStyle(int numberOfShots) {
		this.numberOfShots = numberOfShots;
	}
}
