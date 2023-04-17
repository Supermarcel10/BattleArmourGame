package game.objects;

public class Node {
	int x;
	int y;
	Node parent;
	double gCost;
	double hCost;
	double fCost;

	public Node(int x, int y, Node parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.gCost = 0;
		this.hCost = 0;
		this.fCost = 0;
	}
}

