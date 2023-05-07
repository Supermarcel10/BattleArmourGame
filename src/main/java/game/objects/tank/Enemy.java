package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.block.Block;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static game.MainGame.*;


/**
 * The enemy tank class.
 */
public class Enemy extends Tank {
	private final static int RECALCULATE_PATH_RATE = 60;
	private int untilRecalculateUpdate = 0;

	private final int originalShootingDelay = shootingDelay;

	public final TankType type;

	List<AStar.Node> path;
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
		super(type.updateFrequency, position);

		this.type = type;

		this.addImage(new BodyImage(type.image, halfSize * 2));
		this.setMaxHealth(type.health);
		this.scoreValue = type.scoreValue;
	}

	/**
	 * Destroys the {@link Enemy} and removes it from the {@link game.MainGame#enemies} list.
	 */
	public void destroy() {
		super.destroy();
	}

	private void shootIfPlayerInSight() {
		Vec2 lookDirection = radiansToVec2(getAngle());

		if (type != TankType.EXPLODING && canShoot && !Objects.equals(lookDirection, new Vec2(0, 0))) {
			Vec2 pos = getPosition();
			pos = new Vec2(pos.x + hGridSize, pos.y + hGridSize);
			int blocksEncountered = 0, distance = 0;

			while (blocksEncountered < 2) {
				distance++;
				pos = pos.add(lookDirection);

				if (!(pos.x >= 0 && pos.x <= gridSize && pos.y >= 0 && pos.y <= gridSize)) break;

				Block b = blocks[(int) pos.x][(int) pos.y];

				if (b != null && b.type.isSolid) {
					if (b.type.damageable) blocksEncountered++;
					else return;
				}

				// Change the shooting delay slightly to make the enemies more human.
				shootingDelay = (int) (originalShootingDelay * 2 + (Math.random() * 250));
				shootingTimer.setInitialDelay(shootingDelay);
				shootingTimer.setDelay(shootingDelay);

				for (Player p : player) {
					// If the player is not in the game, dead or not moving: skip
					if (p == null || p.health <= 0) continue;

					if (Objects.equals(p.moveDirection, new Vec2(0, 0))) {
						// Check if the player is within shooting conditions.
						if (isWithinDistance(p.getPosition(), new Vec2(pos.x - hGridSize, pos.y - hGridSize), 1)) {
							shoot();
						}
					} else {
						if (isWithinDistance(p.getPosition(), new Vec2(pos.x - hGridSize, pos.y - hGridSize), 1 + (distance / 20f))) {
							shoot();
						}
					}
				}
			}
		}
	}

	private void pathFind() {
		Point start = new Point((int) (getPosition().x + hGridSize), (int) (getPosition().y + hGridSize));

		List<Point> p1Trace = new ArrayList<>();
		List<Point> p2Trace = new ArrayList<>();
		List<Point> baseTrace = new ArrayList<>();

		if (target == null) {
			if (player[0] != null && player[0].health > 0) {
				Point end = new Point((int) (player[0].getPosition().x + hGridSize), (int) (player[0].getPosition().y + hGridSize));
				p1Trace = aStar(blockCosts, start, end);
			}

			if (player[1] != null && player[1].health > 0) {
				Point end = new Point((int) (player[1].getPosition().x + hGridSize), (int) (player[1].getPosition().y + hGridSize));
				p2Trace = aStar(blockCosts, start, end);
			}

			Point end = new Point((int) (player[0].getPosition().x + hGridSize), (int) (player[0].getPosition().y + hGridSize));
			baseTrace = aStar(blockCosts, start, end);
		}

		path = p1Trace;

		outputDebug();

		System.out.println("Path: " + path);

		if (!path.isEmpty()) {
			System.out.println("Travel cost: " + path.get(path.size() - 1).getG());
		} else {
			System.out.println("No path found.");
		}
	}

	/**
	 * Updates the enemy tank with pathfinding, movement and shooting mechanics.
	 */
	public void update() {
		// PATHFINDING
		if (untilRecalculateUpdate == 0) {
			untilRecalculateUpdate = RECALCULATE_PATH_RATE;
//			pathFind();

			// Remove the last node in the path, as it is the current position of the player.
			if (path != null && path.size() > 0) path.remove(path.size() - 1);
		}
//		else untilRecalculateUpdate--;

		// MOVEMENT
		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			setAngle((float) (Vec2ToDegrees(moveDirection) * Math.PI / -180));
		}

		if (path != null && path.size() > 1) {
			Vec2 currentPosition = new Vec2(getPosition().x + hGridSize, getPosition().y + hGridSize);
			Point nextPointInPath = path.get(1);

			// TODO: Fix the fact that the enemy might go past the next point in the path.
			if (isWithinDistance(currentPosition, new Vec2(nextPointInPath.x, nextPointInPath.y), 0.1f) && path.size() > 2) {
				path.remove(0);
				nextPointInPath = path.get(1);
			}

			// TODO: Something is not right here.
//			double xDiff = Math.abs(nextPointInPath.x - currentPosition.x);
//			int xSign = (int) Math.signum(nextPointInPath.x - currentPosition.x);

//			int xSign = nextPointInPath.x > currentPosition.x ? 1 : -1;
//			double xComponent = xSign * Math.sqrt(1.0 - Math.pow(10e-9, 2));
//
//			int ySign = nextPointInPath.y > currentPosition.y ? 1 : -1;
//			double yComponent = ySign * Math.sqrt(1.0 - Math.pow(10e-9, 2));
//
//			moveDirection = new Vec2((float) xComponent, (float) yComponent);

//			moveDirection = new Vec2(nextPointInPath.x - currentPosition.x, nextPointInPath.y - currentPosition.y);
//
//			System.out.println(moveDirection);

//			Vec2 newPosition = getPositionJBox().add(moveDirection.mul(speed * scaleFactor));
//			setPositionJBox(newPosition);
//
//			if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
//				setAngle((float) (Vec2ToDegrees(moveDirection) * Math.PI / -180));
//			}
		}

		// SHOOTING
		shootIfPlayerInSight();
	}

	public static List<Point> aStar(int[][] maze, Point start, Point end) {
		FastOpenSet openSet = new FastOpenSet();
		Set<Point> closedSet = new HashSet<>();

		openSet.add(start);

		while (!openSet.isEmpty()) {
			Point current = openSet.poll();
			closedSet.add(current);

			if (current.equals(end)) {
				return reconstructPath(current);
			}

			int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
			for (int[] direction : directions) {
				int newY = current.y + direction[0];
				int newX = current.x + direction[1];

				if (newY >= 0 && newY < maze.length && newX >= 0 && newX < maze[0].length) {
					if (maze[newY][newX] == -1) continue;

					Point neighbor = new Point(newX, newY);
					neighbor.setParent(current);
					neighbor.setG(current.getG() + maze[newY][newX]);
					neighbor.setH(heuristic(neighbor, end));
					neighbor.setF(neighbor.getG() + neighbor.getH());

					if (closedSet.contains(neighbor)) continue;
					if (!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					} else if (openSet.get(neighbor).getG() > neighbor.getG()) {
						openSet.update(neighbor);
					}
				}
			}
		}
		return Collections.emptyList();
	}

	private static int heuristic(@NotNull Point a, @NotNull Point b) {
		return Math.abs(a.y - b.y) + Math.abs(a.x - b.x);
	}

	private static @NotNull List<Point> reconstructPath(Point current) {
		List<Point> path = new ArrayList<>();
		while (current != null) {
			path.add(0, current);
			current = current.getParent();
		}
		return path;
	}

	static class Point {
		int x;
		int y;
		int f; // Total cost
		int g; // Cost from start
		int h; // Estimated distance to end
		Point parent;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getF() {
			return f;
		}

		public void setF(int f) {
			this.f = f;
		}

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		public Point getParent() {
			return parent;
		}

		public void setParent(Point parent) {
			this.parent = parent;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Point point = (Point) obj;
			return y == point.y && x == point.x;
		}

		@Override
		public int hashCode() {
			return Objects.hash(y, x);
		}

		@Override
		public String toString() {
			return "(" + y + ", " + x + ")";
		}
	}

	static class FastOpenSet {
		private final PriorityQueue<Point> priorityQueue;
		private final HashSet<Point> hashSet;

		public FastOpenSet() {
			this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Point::getF));
			this.hashSet = new HashSet<>();
		}

		public void add(Point point) {
			priorityQueue.add(point);
			hashSet.add(point);
		}

		public boolean contains(Point point) {
			return hashSet.contains(point);
		}

		public Point poll() {
			Point point = priorityQueue.poll();
			hashSet.remove(point);
			return point;
		}

		public boolean isEmpty() {
			return priorityQueue.isEmpty();
		}

		public Point get(Point point) {
			for (Point p : priorityQueue) {
				if (p.equals(point)) {
					return p;
				}
			}
			return null;
		}

		public void update(Point point) {
			priorityQueue.remove(point);
			priorityQueue.add(point);
		}
	}
}
