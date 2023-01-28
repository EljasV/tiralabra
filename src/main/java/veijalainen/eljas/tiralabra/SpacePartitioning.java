package veijalainen.eljas.tiralabra;

import java.util.Random;

public class SpacePartitioning {
	final int width;
	final int height;

	static Random random = new Random();

	class Node {
		Node left;
		Node right;
		boolean isHorizontal;
		final int width, height;

		public Node(int width, int height, boolean isHorizontal, int depth) {
			this.width = width;
			this.height = height;
			this.isHorizontal = isHorizontal;
			if (depth > 0) {
				if (isHorizontal) {
					int split = random.nextInt(width - 2) + 1;
					left = new Node(split, height, !isHorizontal, depth - 1);
					right = new Node(width - split, height, !isHorizontal, depth - 1);
				} else {
					int split = random.nextInt(height - 2) + 1;
					left = new Node(width, split, !isHorizontal, depth - 1);
					right = new Node(width, height - split, !isHorizontal, depth - 1);
				}
			}
		}
	}

	Node root;

	public SpacePartitioning(int width, int height) {
		this.width = width;
		this.height = height;
		root = new Node(width, height, random.nextBoolean(), 4);
	}
}
