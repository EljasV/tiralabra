package veijalainen.eljas.tiralabra;

/**
 * Perlin noise antaa jatkuvaa tasaista satunnaista ääntä
 * Käytän tässä toteutuksessa kulmia vektorien sijaan
 */
public class PerlinNoise {
	int width, height;
	double[][] gridAngles;

	/**
	 * Luo uuden Perlin noisen ja laskee satunnaiset kulmat ruudukolle. Tallentaa suuremman ruudukon, koska alogritmi tarvitsee jokaisen tarkasteltavan pisteen ympärille jonkin vektorin.
	 *
	 * @param width  Luettavan alueen leveys
	 * @param height Luettavan alueen korkeus
	 */
	public PerlinNoise(int width, int height) {
		this.width = width + 1;
		this.height = height + 1;
		gridAngles = new double[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				gridAngles[i][j] = CaveGenerator.random.nextDouble() * 2 * Math.PI;
			}
		}
	}


	/**
	 * Ottaa näytteen tietyltä kulmalta ja palauttaa sen vaikutuksen suhteessa pisteeseen
	 * @param angleX Kulman x-koordinaatti
	 * @param angleY Kulman y-koordinaatti
	 * @param pointX Pisteen x-koordinaatti
	 * @param pointY Pisteen y-koordinaatti
	 * @return Tietyn kulman vaikutus pisteeseen välillä [-1, 1]
	 */
	public double sampleFrom(int angleX, int angleY, double pointX, double pointY) {
		double angle = gridAngles[angleX][angleY];


		double gridX = Math.cos(angle);
		double gridY = Math.sin(angle);

		double offsetX = pointX - angleX;
		double offsetY = pointY - angleY;

		double cross = gridX * offsetX + gridY * offsetY;
		return cross;
	}

	/**
	 * Ottaa näytteen pistettä lähimmästä kulmasta
	 * @param x Pisteen x-koordinaatti
	 * @param y Pisteen y-koordinaatti
	 * @return Lähimmän pisteen vaikutus välillä [-1, 1]
	 */
	public double sampleRaw(double x, double y) {
		int roundedX = (int) Math.round(x);
		int roundedY = (int) Math.round(y);

		return sampleFrom(roundedX, roundedY, x, y);
	}

	/**
	 * Kaava, jolla saadaan muutettua arvon x sellaiseksi, että derivaatat kohdissa 0 ja 1 ovat 0.
	 * @param x Lineaarinen arvo välillä [0, 1]
	 * @return Muunnettu arvo välillä [0, 1]
	 */
	public double smoothstep(double x) {
		return (-2 * x * x * x) + (3 * x * x);
	}

	/**
	 * Varsinainen prosessointi. Algoritmin lopputulos on jatkuvaa ääntä.
	 * @param x Pisteen x-koordinaatti
	 * @param y Pisteen y-koordinaatti
	 * @return Arvo välillä [-1, 1]
	 */
	public double sample(double x, double y) {
		int floorX = (int) Math.floor(x);
		int floorY = (int) Math.floor(y);
		//Lasketaan ympärillä olevien kulmien vaikutukset tässä kohdassa
		double topLeft = sampleFrom(floorX, floorY, x, y);
		double topRight = sampleFrom(floorX + 1, floorY, x, y);
		double bottomRight = sampleFrom(floorX + 1, floorY + 1, x, y);
		double bottomLeft = sampleFrom(floorX, floorY + 1, x, y);

		double smoothedOffsetX = smoothstep(x - floorX);
		double smoothedOffsetY = smoothstep(y - floorY);

		// Interpoloidaan Smoothstep-funktion avulla ensin leveyssuunnassa olevat arvot ja sen jälkeen lopullinen arvo pisteen kohdalla
		double top = (topLeft * (1 - smoothedOffsetX)) + (topRight * smoothedOffsetX);
		double bottom = (bottomLeft * (1 - smoothedOffsetX)) + (bottomRight * smoothedOffsetX);

		double atPoint = (top * (1 - smoothedOffsetY)) + (bottom * smoothedOffsetY);

		return atPoint;
	}

}
