package veijalainen.eljas.tiralabra;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.image.*;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);


		System.out.println("Enter width:");
		int width;
		try {

			width = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			width = 160;
		}
		System.out.println(width);

		System.out.println("Enter height:");
		int height;
		try {
			height = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			height = 90;
		}
		System.out.println(height);

		SpacePartitioning spacePartitioning = new SpacePartitioning(width, height);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.setRGB(i, j, 0x20_20_20);
			}
		}

		try {
			ImageIO.write(image, "png", new File("o.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}