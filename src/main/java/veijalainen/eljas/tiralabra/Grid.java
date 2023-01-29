package veijalainen.eljas.tiralabra;

enum TileType {
	Wall,
	Floor
};

/**
 * Ei vielä käytössä. Tulee sisältämään kartan pikelien tiedot
 */
public class Grid {
	final int width, height;
	TileType[][] tileTypes;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		tileTypes = new TileType[width][height];
	}
}
