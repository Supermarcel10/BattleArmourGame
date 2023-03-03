package game.character.abstractBody;


public interface IBody {
	void spawn();
	void destroy();
	default void collide() {}
}
