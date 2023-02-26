package veijalainen.eljas.tiralabra;

import veijalainen.eljas.tiralabra.util.Pair;

import java.awt.image.BufferedImage;
import java.util.*;

public class CaveGenerator {
	static Random random = new Random();

	/**
	 * Luo luoaston. Eriytetty tänne Mainista helppokäyttöisyyden vuoksi.
	 *
	 * @param width  Luolaston korkeus
	 * @param height Luolaston leveys
	 * @param nRooms Huoneiden minimiäärä
	 * @return Kuva luolastosta
	 */
	static BufferedImage process(int width, int height, int nRooms) {
		//
		//Ohjelma luo tarvittavat tietorakenteet ja kuvan
		//

		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height, nRooms);
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();
		Grid grid = new Grid(width, height);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int perlinScale = 150;
		PerlinNoise perlinNoise = new PerlinNoise((int) Math.ceil((double) width / perlinScale), (int) Math.ceil((double) height / perlinScale));

		//
		// Käydään SpacePartitioning läpi ja lisätään alueet DoublyConnectedEdgeListaan, luodaan jokaiselle alueelle oma huone ja piirretään huoneet.
		//
		SpacePartitioning.visit(spacePartitioning.root, 0, 0, (node, x, y) -> {
			RoomInfo roomInfo = new RoomInfo(x, y, node.width, node.height);
			DoublyConnectedEdgeList.Face face = doublyConnectedEdgeList.addRectangle(x, y, node.width, node.height);
			face.roomInfo = roomInfo;

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
		corridorGenerator.connections.forEach((roomInfo1, roomInfos) -> {
			roomInfos.forEach(roomInfo2Pair -> {
				int edgeMidX = (roomInfo2Pair.right.origin.x + roomInfo2Pair.right.next.origin.x) / 2;
				int edgeMidY = (roomInfo2Pair.right.origin.y + roomInfo2Pair.right.next.origin.y) / 2;
				grid.fillLine(roomInfo1.midX, roomInfo1.midY, edgeMidX, edgeMidY, TileType.DefinetlyFloor);
				grid.fillLine(edgeMidX, edgeMidY, roomInfo2Pair.left.midX, roomInfo2Pair.left.midY, TileType.DefinetlyFloor);
			});
		});

		//
		// Iteroi soluautomaattia
		//
		for (int i = 0; i < 1000; i++) {
			grid.iterate();
		}

		//
		// Muunna Gridin arvot yhtenäiseksi ja koristele kartta
		//
		grid.solidify();
		grid.decorate(perlinNoise, perlinScale);

		//
		// Tietorakenteiden avulla piirretään kuva
		// Huomio! Tällä hetkellä kuvaan pirretään huoneet ja käytävät valkoisella ja vaaleanharmaalla. Seinät piiretään mustalla ja tumman harmaalla


		grid.visit((x, y, tileType) -> {
			switch (tileType) {
				case MaybeWall:
					image.setRGB(x, y, 0x66_66_66);
					break;
				case MaybeFloor:
					image.setRGB(x, y, 0xAA_AA_AA);
					break;
				case DefinetlyWall:
					image.setRGB(x, y, 0x00_00_00);
					break;
				case DefinetlyFloor:
					image.setRGB(x, y, 0xFF_FF_FF);
					break;
				case StoneWall:
					image.setRGB(x, y, 0x30_30_30);
					break;
				case StoneFloor:
					image.setRGB(x, y, 0xAA_AA_AA);
					break;
				case GrassWall:
					image.setRGB(x, y, 0x00_4E_14);
					break;
				case GrassFloor:
					image.setRGB(x, y, 0x3A_D3_4A);
					break;
				default:
					image.setRGB(x, y, 0xFF_00_FF);
			}
		});

		return image;
	}
}
