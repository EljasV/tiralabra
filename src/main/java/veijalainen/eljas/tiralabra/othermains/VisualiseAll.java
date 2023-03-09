package veijalainen.eljas.tiralabra.othermains;

import veijalainen.eljas.tiralabra.*;
import veijalainen.eljas.tiralabra.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Luokka, jonka main metodi visualisoi generoinnin eri vaiheita
 */
public class VisualiseAll {
	public static void main(String[] args) {
		int width = 1000;
		int height = 1000;
		int nRooms = 100;
		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height, nRooms);
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();
		Grid grid = new Grid(width, height);

		BufferedImage partitionsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage roomsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage completeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage cellularImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage perlinImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		int perlinScale = 150;
		PerlinNoise perlinNoise = new PerlinNoise((int) Math.ceil((double) width / perlinScale), (int) Math.ceil((double) height / perlinScale));

		Graphics graphics1 = partitionsImage.getGraphics();
		Graphics graphics2 = roomsImage.getGraphics();

		//
		// Käydään SpacePartitioning läpi ja lisätään alueet DoublyConnectedEdgeListaan, luodaan jokaiselle alueelle oma huone ja piirretään huoneet.
		//
		SpacePartitioning.visit(spacePartitioning.root, 0, 0, (node, x, y) -> {
			RoomInfo roomInfo = new RoomInfo(x, y, node.width, node.height);
			DoublyConnectedEdgeList.Face face = doublyConnectedEdgeList.addRectangle(x, y, node.width, node.height);
			face.roomInfo = roomInfo;

			grid.fillArea(roomInfo.innerX, roomInfo.innerY, roomInfo.innerWidth, roomInfo.innerHeight, TileType.DefinetlyFloor);


			graphics1.setColor(new Color(0xFF_FF_FF & Objects.hashCode(node)));
			graphics1.fillRect(x, y, node.width, node.height);

			graphics2.setColor(new Color(0xFF_FF_FF));
			graphics2.fillRect(roomInfo.innerX, roomInfo.innerY, roomInfo.innerWidth, roomInfo.innerHeight);
		});
		saveImage(partitionsImage, "partitions.png");
		saveImage(roomsImage, "rooms.png");


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


		for (int k = 0; k < 2; k++) {

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					switch (grid.getSafe(i, j, TileType.DefinetlyWall)) {
						case MaybeWall:
							cellularImage.setRGB(i, j, 0x66_66_66);
							break;
						case MaybeFloor:
							cellularImage.setRGB(i, j, 0xAA_AA_AA);
							break;
						case DefinetlyWall:
							cellularImage.setRGB(i, j, 0x00_00_00);
							break;
						case DefinetlyFloor:
							cellularImage.setRGB(i, j, 0xFF_FF_FF);
							break;
					}
				}
			}
			saveImage(cellularImage, "cellular" + k + ".png");
			grid.iterate();
		}


		grid.iterateUntilStable();

		//
		// Muunna Gridin arvot yhtenäiseksi ja koristele kartta
		//
		grid.solidify();
		grid.decorate(perlinNoise, perlinScale);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double value = perlinNoise.sample((double) i / perlinScale, (double) j / perlinScale);
				int remapped = (int) (((value + 1) / 2) * 256);
				perlinImage.setRGB(i, j, remapped << 16 | remapped << 8 | remapped);
			}
		}
		saveImage(perlinImage, "perlin.png");

		//
		// Tietorakenteiden avulla piirretään kuva
		// Huomio! Tällä hetkellä kuvaan pirretään huoneet ja käytävät valkoisella ja vaaleanharmaalla. Seinät piiretään mustalla ja tumman harmaalla


		grid.visit((x, y, tileType) -> {
			switch (tileType) {
				case MaybeWall:
					completeImage.setRGB(x, y, 0x66_66_66);
					break;
				case MaybeFloor:
					completeImage.setRGB(x, y, 0xAA_AA_AA);
					break;
				case DefinetlyWall:
					completeImage.setRGB(x, y, 0x00_00_00);
					break;
				case DefinetlyFloor:
					completeImage.setRGB(x, y, 0xFF_FF_FF);
					break;
				case StoneWall:
					completeImage.setRGB(x, y, 0x30_30_30);
					break;
				case StoneFloor:
					completeImage.setRGB(x, y, 0xAA_AA_AA);
					break;
				case GrassWall:
					completeImage.setRGB(x, y, 0x00_4E_14);
					break;
				case GrassFloor:
					completeImage.setRGB(x, y, 0x3A_D3_4A);
					break;
				default:
					completeImage.setRGB(x, y, 0xFF_00_FF);
			}
		});

		saveImage(completeImage, "complete.png");
	}

	private static void saveImage(BufferedImage image, String filename) {
		try {

			File file = new File("./VisualiseAll/" + filename);
			file.mkdirs();
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
