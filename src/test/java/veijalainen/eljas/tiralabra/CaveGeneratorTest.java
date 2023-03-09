package veijalainen.eljas.tiralabra;

import org.junit.Test;
import veijalainen.eljas.tiralabra.util.Pair;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class CaveGeneratorTest {


	@Test
	public void roomsAreConnected() {
		int width = 1000;
		int height = 1000;
		int nRooms = 200;
		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height, nRooms);
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();
		Grid grid = new Grid(width, height);

		Set<RoomInfo> roomInfos = new HashSet<>();

		createSolidGrid(spacePartitioning, doublyConnectedEdgeList, grid, roomInfos);

		class pos {
			final int x;
			final int y;

			pos(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;

				pos pos = (pos) o;

				if (x != pos.x) return false;
				return y == pos.y;
			}

			@Override
			public int hashCode() {
				int result = x;
				result = 31 * result + y;
				return result;
			}
		}

		RoomInfo first = roomInfos.stream().findAny().get();

		Set<pos> visited = new HashSet<>();
		Queue<pos> queue = new ArrayDeque<>();

		pos firstPos = new pos(first.midX, first.midY);

		queue.add(firstPos);
		visited.add(firstPos);

		while (!queue.isEmpty()) {
			pos inspectedPos = queue.poll();

			pos[] neighbours = {
					  new pos(inspectedPos.x + 1, inspectedPos.y),
					  new pos(inspectedPos.x - 1, inspectedPos.y),
					  new pos(inspectedPos.x, inspectedPos.y + 1),
					  new pos(inspectedPos.x, inspectedPos.y - 1)
			};

			for (pos neighbourPos :
					  neighbours) {
				if (visited.contains(neighbourPos)) {
					continue;
				}
				if (grid.getSafe(neighbourPos.x, neighbourPos.y, TileType.GrassFloor) != TileType.DefinetlyFloor) {
					continue;
				}
				queue.add(neighbourPos);
				visited.add(neighbourPos);
			}

		}

		for (RoomInfo roomInfo :
				  roomInfos) {
			pos roomCenter = new pos(roomInfo.midX, roomInfo.midY);
			assertTrue(visited.contains(roomCenter));
		}
	}

	private static void createSolidGrid(SpacePartitioning spacePartitioning, DoublyConnectedEdgeList doublyConnectedEdgeList, Grid grid, Set<RoomInfo> roomInfos) {
		//
		// Käydään SpacePartitioning läpi ja lisätään alueet DoublyConnectedEdgeListaan, luodaan jokaiselle alueelle oma huone ja piirretään huoneet.
		//
		SpacePartitioning.visit(spacePartitioning.root, 0, 0, (node, x, y) -> {
			RoomInfo roomInfo = new RoomInfo(x, y, node.width, node.height);
			DoublyConnectedEdgeList.Face face = doublyConnectedEdgeList.addRectangle(x, y, node.width, node.height);
			face.roomInfo = roomInfo;

			roomInfos.add(roomInfo);

			grid.fillArea(roomInfo.innerX, roomInfo.innerY, roomInfo.innerWidth, roomInfo.innerHeight, TileType.DefinetlyFloor);

		});


		//
		// Luo vieruslistat DoublyConnectedEdgeListan avulla
		//
		HashMap<RoomInfo, Set<Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge>>> possibleConnections = new HashMap<>();
		doublyConnectedEdgeList.edges.forEach(halfEdge -> {
			if (halfEdge.twin != null) {
				possibleConnections.putIfAbsent(halfEdge.face.roomInfo, new HashSet<>());
				possibleConnections.get(halfEdge.face.roomInfo).add(new Pair<>(halfEdge.twin.face.roomInfo, halfEdge));
			}
		});

		//
		// Karsii ylimääräiset käytävät
		//
		CorridorGenerator corridorGenerator = new CorridorGenerator(possibleConnections);


		//
		// Piirrä alueiden välille kovat seinät jättäen kuitenkin käytävät vapaiksi
		//
		Set<DoublyConnectedEdgeList.HalfEdge> safeEdges = new HashSet<>();

		corridorGenerator.connections.forEach((key, innerSet) -> innerSet.stream().map(pair -> pair.right).forEach(safeEdges::add));

		doublyConnectedEdgeList.edges.forEach(halfEdge -> {
			if (!safeEdges.contains(halfEdge)) {
				grid.fillLine(halfEdge.origin.x, halfEdge.origin.y, halfEdge.next.origin.x, halfEdge.next.origin.y, TileType.DefinetlyWall);
			} else {
				grid.fillLine(halfEdge.origin.x, halfEdge.origin.y, halfEdge.next.origin.x, halfEdge.next.origin.y, TileType.MaybeWall);
			}
		});

		//
		// Piirtää Gridiin käytävät
		//
		corridorGenerator.connections.forEach((roomInfo1, roomInfosPairs) -> {
			roomInfosPairs.forEach(roomInfo2Pair -> {
				int edgeMidX = (roomInfo2Pair.right.origin.x + roomInfo2Pair.right.next.origin.x) / 2;
				int edgeMidY = (roomInfo2Pair.right.origin.y + roomInfo2Pair.right.next.origin.y) / 2;
				grid.fillLine(roomInfo1.midX, roomInfo1.midY, edgeMidX, edgeMidY, TileType.DefinetlyFloor);
				grid.fillLine(edgeMidX, edgeMidY, roomInfo2Pair.left.midX, roomInfo2Pair.left.midY, TileType.DefinetlyFloor);
			});
		});

		//
		// Iteroi soluautomaattia
		//
		grid.iterateUntilStable();
		grid.solidify();
	}
}
