package veijalainen.eljas.tiralabra.othermains;

import veijalainen.eljas.tiralabra.CaveGenerator;

import java.time.Duration;
import java.time.Instant;

public class PerformanceTest {


	/**
	 * Suorituskykytestaus. luo 100 kappaletta 1000*1000 huoneita ja laskee ajan
	 * @param args _
	 */
	public static void main(String[] args) {

		Instant begin = Instant.now();

		for (int i = 0; i < 1; i++) {
			CaveGenerator.process(1000, 1000, 16);
		}
		Instant end = Instant.now();

		Duration duration = Duration.between(begin, end);

		System.out.println("1 generointia kesti " + duration.toMillis() + "ms");
	}

}
