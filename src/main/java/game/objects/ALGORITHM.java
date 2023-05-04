package game.objects;

import java.util.*;


public class ALGORITHM {
    public static void main(String[] args) {
        int[][] maze = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 0}
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

        System.out.println("Elapsed time: " + elapsedTimeMs + " ms // " + elapsedTime + " ns");
        System.out.println("Path: " + path);
    }

    public static List<Point> aStar(int[][] maze, Point start, Point end) {
        PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingInt(Point::getF));
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
                    if (maze[newRow][newCol] == 1) continue;

                    Point neighbor = new Point(newRow, newCol);
                    neighbor.setParent(current);
                    neighbor.setG(current.getG() + 1);
                    neighbor.setH(heuristic(neighbor, end));
                    neighbor.setF(neighbor.getG() + neighbor.getH());

                    if (closedSet.contains(neighbor)) continue;
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else if (openSet.stream().anyMatch(p -> p.equals(neighbor) && p.getG() < neighbor.getG())) {
                        continue;
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static int heuristic(Point a, Point b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    private static List<Point> reconstructPath(Point current) {
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
        int f;
        int g;
        int h;
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
}

