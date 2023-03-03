package game.characters;


public interface IBody {
	void spawn();
	default void collide() {}
}
