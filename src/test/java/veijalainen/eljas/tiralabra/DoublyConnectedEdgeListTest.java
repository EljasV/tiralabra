package veijalainen.eljas.tiralabra;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoublyConnectedEdgeListTest {

	@Test
	public void twoAdjacentRectangles() {
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();

		doublyConnectedEdgeList.addRectangle(0, 0, 1, 1);
		doublyConnectedEdgeList.addRectangle(0, 1, 1, 1);

		assertEquals("Expected two twins", 2, doublyConnectedEdgeList.edges.stream().filter(halfEdge -> halfEdge.twin != null).count());
	}


}
