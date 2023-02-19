package veijalainen.eljas.tiralabra;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpacePartitioningTest {
	@Test
	public void childNodesSameSizeAsParent() {
		SpacePartitioning.Node node1 = new SpacePartitioning.Node(10, 10, false, 1);
		assertEquals(node1.height, node1.left.height + node1.right.height);

		SpacePartitioning.Node node2 = new SpacePartitioning.Node(10, 10, true, 1);
		assertEquals(node2.width, node2.left.width + node2.right.width);
	}


	@Test
	public void visitAll() {

		boolean[][] booleans = new boolean[100][100];
		SpacePartitioning spacePartitioning = new SpacePartitioning(100, 100, 16);

		SpacePartitioning.visit(spacePartitioning.root, 0, 0, (node, x, y) -> {
			for (int xi = 0; xi < node.width; xi++) {
				for (int yi = 0; yi < node.height; yi++) {
					booleans[xi + x][yi + y] = true;
				}
			}
		});

		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				assertTrue("Every cell must be visited", booleans[i][j]);
			}
		}

	}
}