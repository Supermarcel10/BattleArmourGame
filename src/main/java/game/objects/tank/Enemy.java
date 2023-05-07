package game.objects.tank;

import city.cs.engine.BodyImage;
import game.objects.block.Block;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

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
		AStar.Node start = new AStar.Node((int) (Math.floor(getPosition().x)) + hGridSize, (int) (Math.floor(getPosition().y)) + hGridSize);

		AStar.Node end1 = null;
		if (player[0].health > 0) {
			end1 = new AStar.Node((int) (player[0].getPosition().x + hGridSize), (int) (player[0].getPosition().y + hGridSize));
		}

		AStar.Node end2 = null;
		if (player.length > 1 && player[1] != null && player[1].health > 0) {
			end2 = new AStar.Node((int) (player[1].getPosition().x + hGridSize), (int) (player[1].getPosition().y + hGridSize));
		}

		AStar.Node targetEnd = null;

		if (end1 != null && end2 != null) {
			double distanceToEnd1 = Math.sqrt(Math.pow(start.x - end1.x, 2) + Math.pow(start.y - end1.y, 2));
			double distanceToEnd2 = Math.sqrt(Math.pow(start.x - end2.x, 2) + Math.pow(start.y - end2.y, 2));

			targetEnd = (distanceToEnd1 < distanceToEnd2) ? end1 : end2;
		} else if (end1 != null) targetEnd = end1;
		else if (end2 != null) targetEnd = end2;

		if (targetEnd != null) path = AStar.findPath(blockCosts, start, targetEnd);
	}

	/**
	 * Updates the enemy tank with pathfinding, movement and shooting mechanics.
	 */
	public void update() {
		// PATHFINDING
		if (untilRecalculateUpdate == 0) {
			untilRecalculateUpdate = RECALCULATE_PATH_RATE;
			pathFind();

			// Remove the last node in the path, as it is the current position of the player.
			if (path != null && path.size() > 0) path.remove(path.size() - 1);
		} else untilRecalculateUpdate--;

		// MOVEMENT
		if (path != null && path.size() >= 1) {
			Vec2 currentPosition = new Vec2(getPosition().x + hGridSize, getPosition().y + hGridSize);
			AStar.Node nextPointInPath = path.get(0);

			if (isWithinDistance(currentPosition, new Vec2(nextPointInPath.x, nextPointInPath.y), 0.1f)) {
				path.remove(0);

				if (path.isEmpty()) return;
				nextPointInPath = path.get(0);
			}

			float deltaX = nextPointInPath.x - currentPosition.x;
			float deltaY = nextPointInPath.y - currentPosition.y;

			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				moveDirection =  new Vec2((int)Math.signum(deltaX), 0);
			} else {
				moveDirection = new Vec2(0, (int) Math.signum(deltaY));
			}

			super.update();
		}

		if (!(moveDirection.x == 0 && moveDirection.y == 0)) {
			setAngle((float) (Vec2ToDegrees(moveDirection) * Math.PI / -180));
		} else if (path != null && path.size() == 0) {
			setAngle((float) (Vec2ToDegrees(new Vec2(player[0].getPosition().x - getPosition().x, player[0].getPosition().y - getPosition().y)) * Math.PI / -180));
		}

		// SHOOTING
		shootIfPlayerInSight();
	}

	static class AStar {
		public static int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

		public static class Node {
			int x;
			int y;
			int cost = 0;
			Node parent = null;

			public Node(int x, int y, int cost, Node parent) {
				this.x = x;
				this.y = y;
				this.cost = cost;
				this.parent = parent;
			}

			public Node(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Node node = (Node) o;
				return x == node.x && y == node.y;
			}

			@Override
			public String toString() {
				return String.format("Node {x=%s, y=%s, cost=%d, parent=%s}", x, y, cost, parent);
			}

			@Override
			public int hashCode() {
				return Objects.hash(x, y);
			}
		}

		public static List<Node> findPath(int[][] grid, Node startNode, Node endNode) {
			PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
			HashSet<Node> closedSet = new HashSet<>();

			openSet.add(startNode);

			while (!openSet.isEmpty()) {
				Node currentNode = openSet.poll();

				// If we have reached the end node, we can return the path
				if (currentNode.x == endNode.x && currentNode.y == endNode.y) return buildPath(currentNode);

				// Firm the current node as explored
				closedSet.add(currentNode);

				// Explore all the neighbors
				for (int[] direction : directions) {
					int newX = currentNode.x + direction[0];
					int newY = currentNode.y + direction[1];

					// Check if the new node is out of bounds or an obstacle
					if (newX < 0 || newX >= grid[0].length || newY < 0 || newY >= grid.length || grid[newX][newY] == -1) continue;

					Node neighbor = new Node(newX, newY, currentNode.cost + grid[newX][newY] - manhattanHeuristic(currentNode, endNode) + manhattanHeuristic(newX, newY, endNode), currentNode);

					// Check if the new node has already been explored
					if (closedSet.contains(neighbor)) continue;

					// Check if the new node is already in the open set
					// If an existing node has a higher cost than the current node, replace it
					Optional<Node> existingNode = openSet.stream().filter(node -> node.x == newX && node.y == newY).findFirst();
					if (existingNode.isPresent()) {
						if (existingNode.get().cost > neighbor.cost) {
							openSet.remove(existingNode.get());
							openSet.add(neighbor);
						}
					} else {
						openSet.add(neighbor);
					}
				}
			}

			return Collections.emptyList();
		}

		private static int manhattanHeuristic(int x1, int y1, @NotNull Node n2) {
			return Math.abs(x1 - n2.x) + Math.abs(y1 - n2.y);
		}

		private static int manhattanHeuristic(@NotNull Node n1, @NotNull Node n2) {
			return Math.abs(n1.x - n2.x) + Math.abs(n1.y - n2.y);
		}

		private static @NotNull List<Node> buildPath(Node node) {
			List<Node> path = new ArrayList<>();

			while (node != null) {
				path.add(node);
				node = node.parent;
			}

			Collections.reverse(path);
			return path;
		}
	}
}
