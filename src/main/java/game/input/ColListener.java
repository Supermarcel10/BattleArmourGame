package game.input;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import game.objects.Tank;
import org.jetbrains.annotations.NotNull;

public class ColListener implements CollisionListener {
	@Override
	public void collide(CollisionEvent collisionEvent) {
		System.out.println("Shot collided with " + collisionEvent.getOtherBody().getClass().getSimpleName());
//		if (collisionEvent.getOtherBody() instanceof Tank) {
//			Tank tank = (Tank) collisionEvent.getOtherBody();
////			if (tank != shooter) {
////				tank.damage();
////				body.destroy();
////			}
//		}
	}
}
