package game.character.abstractBody;


public interface IBody {
	void spawn();
	default void collide() {}
}
