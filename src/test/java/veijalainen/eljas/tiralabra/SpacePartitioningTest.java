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
}