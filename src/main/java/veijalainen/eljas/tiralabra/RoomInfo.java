package veijalainen.eljas.tiralabra;


/**
 * Pitää sisällään huoneen tarvitsemat tiedot
 */
public class RoomInfo {

	public final int innerX;
	public final int innerY;
	public final int innerWidth;
	public final int innerHeight;
	public final int midX;
	public final int midY;

	/**
	 * Luo satunnaisen huoneen anneyulle alueelle
	 * @param x Alueen x-koordinaatti
	 * @param y Alueen y-koordinaatti
	 * @param width Alueen leveys
	 * @param height Alueen korkeus
	 */
	public RoomInfo(int x, int y, int width, int height) {
		double innerXOffset = (width / 4f) * CaveGenerator.random.nextDouble();
		double innerYOffset = (height / 4f) * CaveGenerator.random.nextDouble();
		innerX = (int) (x + innerXOffset);
		innerY = (int) (y + innerYOffset);
		innerWidth = (int) (3f / 4f * (width - innerXOffset) + 1f / 4f * CaveGenerator.random.nextDouble() * (width - innerXOffset));
		innerHeight = (int) (3f / 4f * (height - innerYOffset) + 1f / 4f * CaveGenerator.random.nextDouble() * (height - innerYOffset));

		midX = (innerX + innerX + innerWidth) / 2;
		midY = (innerY + innerY + innerHeight) / 2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		RoomInfo roomInfo = (RoomInfo) o;

		if (innerX != roomInfo.innerX) {
			return false;
		}
		if (innerY != roomInfo.innerY) {
			return false;
		}
		if (innerWidth != roomInfo.innerWidth) {
			return false;
		}
		if (innerHeight != roomInfo.innerHeight) {
			return false;
		}
		if (midX != roomInfo.midX) {
			return false;
		}
		return midY == roomInfo.midY;
	}

	@Override
	public int hashCode() {
		int result = innerX;
		result = 31 * result + innerY;
		result = 31 * result + innerWidth;
		result = 31 * result + innerHeight;
		result = 31 * result + midX;
		result = 31 * result + midY;
		return result;
	}
}
