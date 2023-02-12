package veijalainen.eljas.tiralabra;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CaveGenerator {
	static Random random = new Random();

	/**
	 * Luo luoaston. Eriytetty tänne Mainista helppokäyttöisyyden vuoksi.
	 * @param width Luolaston korkeus
	 * @param height Luolaston leveys
	 * @return Kuva luolastosta
	 */
	static BufferedImage process(int width, int height) {
		//
		//Ohjelma luo tarvittavat tietorakenteet ja kuvan
		//

		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height);
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();
		Grid grid = new Grid(width, height, TileType.Wall);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		//
		// Käydään SpacePartitioning läpi ja lisätään alueet DoublyConnectedEdgeListaan, luodaan jokaiselle alueelle oma huone ja piirretään huoneet.
		//
		SpacePartitioning.visit(spacePartitioning.root, 0, 0, (node, x, y) -> {
			RoomInfo roomInfo = new RoomInfo(x, y, node.width, node.height);
			DoublyConnectedEdgeList.Face face = doublyConnectedEdgeList.addRectangle(x, y, node.width, node.height);
			face.roomInfo = roomInfo;

			grid.fillArea(roomInfo.innerX, roomInfo.innerY, roomInfo.innerWidth, roomInfo.innerHeight, TileType.Floor);

		});


		//
		// Luo vieruslistat DoublyConnectedEdgeListan avulla
		//
		HashMap<RoomInfo, Set<RoomInfo>> possibleConnections = new HashMap<>();
		doublyConnectedEdgeList.edges.forEach(halfEdge -> {
			if (halfEdge.twin != null) {
				possibleConnections.putIfAbsent(halfEdge.face.roomInfo, new HashSet<>());
				possibleConnections.get(halfEdge.face.roomInfo).add(halfEdge.twin.face.roomInfo);
			}
		});

		//
		// Karsii ylimääräiset käytävät
		//
		CorridorGenerator corridorGenerator = new CorridorGenerator(possibleConnections);


		//
		// Piirtää Gridiin käytävät
		//
		corridorGenerator.connections.forEach((roomInfo1, roomInfos) -> {
			roomInfos.forEach(roomInfo2 -> {
				grid.fillLine(roomInfo1.midX, roomInfo1.midY, roomInfo2.midX, roomInfo2.midY, TileType.Floor);
			});
		});

		//
		// Tietorakenteiden avulla piirretään kuva
		// Huomio! Tällä hetkellä kuvaan pirretään huoneet ja käytävät valkoisella


		grid.visit((x, y, tileType) -> {
			switch (tileType) {

				case Wall:
					image.setRGB(x, y, 0x66_66_66);
					break;
				case Floor:
					image.setRGB(x, y, 0xFF_FF_FF);
					break;
				default:
					image.setRGB(x, y, 0x00_00_00);
			}
		});

		return image;
	}
}
