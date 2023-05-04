package game.objects;

import org.jetbrains.annotations.NotNull;

import java.util.*;


class AStar {
    public static int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public static class Node {
        int x;
        int y;
        int cost;
        Node parent;

        public Node(int x, int y, int cost, Node parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
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

    public static List<Node> findPath(int[][] grid, int startX, int startY, int endX, int endY) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        HashSet<Node> closedSet = new HashSet<>();

        Node startNode = new Node(startX, startY, 0, null);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            // If we have reached the end node, we can return the path
            if (currentNode.x == endX && currentNode.y == endY) return buildPath(currentNode);

            // Firm the current node as explored
            closedSet.add(currentNode);

            // Explore all the neighbors
            for (int[] direction : directions) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                // Check if the new node is out of bounds or an obstacle
                if (newX < 0 || newX >= grid[0].length || newY < 0 || newY >= grid.length || grid[newY][newX] == -1) continue;

                Node neighbor = new Node(newX, newY, currentNode.cost + grid[newY][newX] - manhattanHeuristic(currentNode.x, currentNode.y, endX, endY) + + manhattanHeuristic(newX, newY, endX, endY), currentNode);

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

    private static int manhattanHeuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
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

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0},
                {7, -1, 10, -1, 5},
                {0, -1, 0, -1, 1},
                {0, 0, 0, 0, 0}
        };

        List<Node> path = findPath(grid, 2, 0, 2, 3);

        if (path.isEmpty()) {
            System.out.println("No path found!");
        } else {
            for (Node node : path) {
                System.out.println("[" + node.x + ", " + node.y + "]");
            }
            System.out.println("With a cost of: " + path.get(path.size() - 1).cost);
        }
    }
}
