package game.objects;

import org.jetbrains.annotations.NotNull;

import java.util.*;


public class ALGORITHM {
    public static void main(String[] args) {
        int[][] maze = {
                {0, 1, 3, 1},
                {-1, 3, -1, 5},
                {5, 4, 2, 2},
                {1, 3, 1, 1},
                {1, -1, 1, 2}
        };

        Point start = new Point(0, 0);
        Point end = new Point(4, 3);

        long startTime = System.nanoTime();

        List<Point> path = aStar(maze, start, end);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        long elapsedTimeMs = elapsedTime / 1000000;

        System.out.println("Maze:");
        System.out.println(Arrays.deepToString(maze).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        if (!path.isEmpty()) {
            System.out.println("Travel cost: " + path.get(path.size() - 1).getG());
        } else {
            System.out.println("No path found.");
        }

        System.out.println("Elapsed time: " + elapsedTimeMs + " ms // " + elapsedTime + " ns");
        System.out.println("Path: " + path);
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
                int newRow = current.row + direction[0];
                int newCol = current.col + direction[1];

                if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length) {
                    if (maze[newRow][newCol] == -1) continue;

                    Point neighbor = new Point(newRow, newCol);
                    neighbor.setParent(current);
                    neighbor.setG(current.getG() + maze[newRow][newCol]);
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
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
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
        int row;
        int col;
        int f; // Total cost
        int g; // Cost from start
        int h; // Estimated distance to end
        Point parent;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
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
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
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
