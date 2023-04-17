package game.objects;

import city.cs.engine.BodyImage;
import game.prefab.Player;
import game.prefab.TankType;
import org.jbox2d.common.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class Enemy extends Tank {
	public Player target;

	public Enemy(@NotNull TankType type, Vec2 position) {
		super(position);
		this.addImage(new BodyImage(type.image, 3 * scaleFactor));
		this.setMaxHealth(type.health);
		this.speed = type.speed;
		this.scoreValue = type.scoreValue;
	}

	@Override
	public void update() {

	}

	public List<Vec2> findPath(int startX, int startY, int targetX, int targetY, boolean[][] blocks) {
		Node[][] nodes = new Node[blocks.length][blocks[0].length];
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[0].length; j++) {
				nodes[i][j] = new Node(i, j, null);
			}
		}

		PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(node -> node.fCost));
		List<Node> closedList = new ArrayList<>();
		Node startNode = nodes[startX][startY];
		Node targetNode = nodes[targetX][targetY];
		openList.add(startNode);

		while (!openList.isEmpty()) {
			Node currentNode = openList.poll();
			closedList.add(currentNode);

			if (currentNode.equals(targetNode)) {
				List<Vec2> path = new ArrayList<>();
				Node pathNode = currentNode;
				while (pathNode != null) {
					path.add(0, new Vec2(pathNode.x, pathNode.y));
					pathNode = pathNode.parent;
				}
				return path;
			}

			int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
			for (int[] direction : directions) {
				int newX = currentNode.x + direction[0];
				int newY = currentNode.y + direction[1];

				if (newX >= 0 && newX < blocks.length && newY >= 0 && newY < blocks[0].length) {
					if (blocks[newX][newY]) {
						continue;
					}

					Node neighborNode = nodes[newX][newY];
					if (closedList.contains(neighborNode)) {
						continue;
					}

					double tentativeGCost = currentNode.gCost + 1;
					if (!openList.contains(neighborNode)) {
						openList.add(neighborNode);
					} else if (tentativeGCost >= neighborNode.gCost) {
						continue;
					}

					neighborNode.parent = currentNode;
					neighborNode.gCost = tentativeGCost;
					neighborNode.hCost = Math.abs(newX - targetX) + Math.abs(newY - targetY);
					neighborNode.fCost = neighborNode.gCost + neighborNode.hCost;
				}
			}
		}

		return null;
	}
}
