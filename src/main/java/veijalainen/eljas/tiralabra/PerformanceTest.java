package veijalainen.eljas.tiralabra;

import java.time.Duration;
import java.time.Instant;

public class PerformanceTest {


	/**
	 * Suorituskykytestaus. luo 1000 kappaletta 1000*1000 huoneita ja laskee ajan
	 * @param args _
	 */
	public static void main(String[] args) {

		Instant begin = Instant.now();

		for (int i = 0; i < 1000; i++) {
			CaveGenerator.process(1000, 1000);
		}
		Instant end = Instant.now();

		Duration duration = Duration.between(begin, end);

		System.out.println("1000 generointia kesti " + duration.toMillis() + "ms");
	}

}
