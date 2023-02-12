package veijalainen.eljas.tiralabra;

enum TileType {
	Wall,
	Floor
};

/**
 * Ruudukko sisältää tiedon luolaston muodosta soluautomaattia ja lopullista sokkeloa varten
 */
public class Grid {
	final int width, height;
	TileType[][] tileTypes;

	public Grid(int width, int height, TileType baseType) {
		this.width = width;
		this.height = height;
		tileTypes = new TileType[width][height];
		fillArea(0, 0, width, height, baseType);
	}

	public void setTile(int x, int y, TileType tileType) {
		tileTypes[x][y] = tileType;
	}

	public void fillArea(int x, int y, int width, int height, TileType tileType) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tileTypes[i + x][j + y] = tileType;
			}
		}
	}

	public void fillLine(int fromX, int fromY, int toX, int toY, TileType tile) {
		double distance = Math.sqrt(Math.pow(toX - fromX, 2) + Math.pow(toY - fromY, 2));
		double deltaX = (toX - fromX) / (distance * 4);
		double deltaY = (toY - fromY) / (distance * 4);

		double xIterator = fromX;
		double yIterator = fromY;
		while (Math.sqrt(Math.pow(xIterator - toX, 2) + Math.pow(yIterator - toY, 2)) > 0.5) {
			xIterator += deltaX;
			yIterator += deltaY;

			setTile((int) xIterator, (int) yIterator, tile);

			setTile((int) xIterator + 1, (int) yIterator, tile);
			setTile((int) xIterator - 1, (int) yIterator, tile);
			setTile((int) xIterator, (int) yIterator + 1, tile);
			setTile((int) xIterator, (int) yIterator - 1, tile);
		}

	}

	@FunctionalInterface
	interface Visitor {
		void visit(int x, int y, TileType tileType);
	}

	public void visit(Visitor visitor) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				visitor.visit(i, j, tileTypes[i][j]);
			}
		}
	}
}
