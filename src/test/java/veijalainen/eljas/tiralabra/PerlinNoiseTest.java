package veijalainen.eljas.tiralabra;

import org.junit.Test;

import static org.junit.Assert.*;

public class PerlinNoiseTest {

	@Test
	public void valuesAreInRange() {
		PerlinNoise perlinNoise = new PerlinNoise(1, 1);
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				double sample = perlinNoise.sample(i / 100d, j / 100d);
				assertTrue(-1 <= sample && sample <= 1);
			}
		}
	}
}
