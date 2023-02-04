package veijalainen.eljas.tiralabra;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class Main {
	/**
	 * Ohjelman suoritus alkaa main- funktiosta.
	 * Ohjelma kysyy kartan korkeuden ja leveyden. Sen jälkeen luodaan tarvittavat tietorakenteet, joiden avulla muodostetaan kuva luolastosta. Luolasto tallennetaan kuvana tiedostoon.
	 *
	 * @param args main-funktion argumentit, jotka annetaan komentoriviltä ennen ohjelman suoritusta. Tällä hetkellä ei käytössä.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		//
		// Ohjelma kysyy kartan leveyden
		//
		System.out.println("Enter width:");
		int width;
		try {

			width = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			width = 160;
		}
		System.out.println(width);

		//
		// Ohjelma kysyy kartan korkeuden
		//
		System.out.println("Enter height:");
		int height;
		try {
			height = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			height = 90;
		}
		System.out.println(height);

		//
		//Ohjelma luo tarvittavat tietorakenteet ja kuvan
		//

		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height);
		DoublyConnectedEdgeList doublyConnectedEdgeList = new DoublyConnectedEdgeList();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		//
		// Tietorakenteiden avulla piirretään kuva
		// Huomio! Tällä hetkellä kuvaan pirretään eri väreillä "osastot", joihin tullaan myöhemmin rakentamaan huoneet.
		Graphics2D graphics = image.createGraphics();
		SpacePartitioning.Visitor visitor = (SpacePartitioning.Node node, int x, int y) -> {
			graphics.setColor(new Color(0x66_66_66));

			graphics.fillRect(x, y, node.width, node.height);

			Random random = SpacePartitioning.random;

			double innerXOffset = (node.width / 4f) * random.nextDouble();
			double innerYOffset = (node.height / 4f) * random.nextDouble();
			int innerX = (int) (x + innerXOffset);
			int innerY = (int) (y + innerYOffset);
			int innerWidth = (int) (3f / 4f * (node.width - innerXOffset) + 1f / 4f * random.nextDouble() * (node.width - innerXOffset));
			int innerHeight = (int) (3f / 4f * (node.height - innerYOffset) + 1f / 4f * random.nextDouble() * (node.height - innerYOffset));

			graphics.setColor(new Color(0xFF_FF_FF));

			graphics.fillRect(innerX, innerY, innerWidth, innerHeight);


			doublyConnectedEdgeList.addRectangle(x, y, node.width, node.height);
		};

		SpacePartitioning.visit(spacePartitioning.root, 0, 0, visitor);

		graphics.setColor(new Color(0xFF_00_00));
		doublyConnectedEdgeList.edges.forEach(halfEdge -> {
			if (halfEdge.twin != null) {
				graphics.drawLine(halfEdge.face.x, halfEdge.face.y, halfEdge.twin.face.x, halfEdge.twin.face.y);
			}
		});


		//
		// Ohjelma kirjoittaa kuvan levylle
		//
		try {
			ImageIO.write(image, "png", new File("o.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
