package veijalainen.eljas.tiralabra;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


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
		// Ohjelma kutsuu prosessoointifunktiota
		//
		BufferedImage image = CaveGenerator.process(width, height);

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
