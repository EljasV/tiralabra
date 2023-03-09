package veijalainen.eljas.tiralabra.othermains;

import veijalainen.eljas.tiralabra.PerlinNoise;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Apuluokka main-metodilla Perlin Noisen debuggaamiseen
 */
public class PerlinVisualiser {

	public static void main(String[] args) {
		int width = 10;
		int height = 10;
		int scale = 100;

		PerlinNoise perlinNoise = new PerlinNoise(width, height);
		BufferedImage image = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);


		for (int i = 0; i < width * scale; i++) {
			for (int j = 0; j < height * scale; j++) {
				double value = perlinNoise.sample((double) i / scale, (double) j / scale);
				int remapped = (int) (((value + 1) / 2) * 256);
				image.setRGB(i, j, remapped << 16 | remapped << 8 | remapped);
			}
		}

		try {
			ImageIO.write(image, "png", new File("perlin.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
