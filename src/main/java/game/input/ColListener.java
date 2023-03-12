package game.input;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import game.objects.Block;
import game.objects.Tank;
import game.prefab.Shot;


public class ColListener implements CollisionListener {
	@Override
	public void collide(CollisionEvent collisionEvent) {
		System.out.println("Collision detected");
		System.out.println("Reporting body class: " + collisionEvent.getReportingBody().getClass());
		System.out.println("Other body class: " + collisionEvent.getOtherBody().getClass());

		if (collisionEvent.getOtherBody() instanceof DynamicBody) {
			System.out.println("Dynamic body detected");
			city.cs.engine.Body dynamicBody = collisionEvent.getOtherBody();
			System.out.println("Dynamic body class: " + dynamicBody.getClass());
			if (dynamicBody instanceof game.objects.abstractBody.Body) {
				System.out.println("Body detected");
				city.cs.engine.Body body = (game.objects.abstractBody.Body) dynamicBody;
				System.out.println("Body class: " + body.getClass());
				if (body instanceof Shot) {
					System.out.println("Shot detected");
					Shot shot = (Shot) body;
					shot.destroy();
				}
			}
		}

		if (collisionEvent.getReportingBody() instanceof DynamicBody) {
			System.out.println("Dynamic body detected");
			city.cs.engine.Body dynamicBody = collisionEvent.getReportingBody();
			System.out.println("Dynamic body class: " + dynamicBody.getClass());
			if (dynamicBody instanceof game.objects.abstractBody.Body) {
				System.out.println("Body detected");
				city.cs.engine.Body body = (game.objects.abstractBody.Body) dynamicBody;
				System.out.println("Body class: " + body.getClass());
				if (body instanceof Shot) {
					System.out.println("Shot detected");
					Shot shot = (Shot) body;
					shot.destroy();
				}
			}
		}

		if (collisionEvent.getOtherBody() instanceof Shot) {
			System.out.println("Shot detected");
		}

		if (collisionEvent.getReportingBody() instanceof Tank) {
			Tank tank = (Tank) collisionEvent.getReportingBody();
			System.out.println("Tank detected");
		}

		if (collisionEvent.getOtherBody() instanceof Block) {
			System.out.println("Block detected");
		}
	}
}
