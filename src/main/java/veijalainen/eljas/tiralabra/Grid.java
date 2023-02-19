package veijalainen.eljas.tiralabra;

enum TileType {
	MaybeWall,
	MaybeFloor,
	DefinetlyWall,
	DefinetlyFloor
};

/**
 * Ruudukko sisältää tiedon luolaston muodosta soluautomaattia ja lopullista sokkeloa varten
 */
public class Grid {
	final int width, height;
	TileType[][] tileTypes;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		tileTypes = new TileType[width][height];


		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tileTypes[i][j] = CaveGenerator.random.nextBoolean() ? TileType.MaybeWall : TileType.MaybeFloor;
			}
		}
	}

	public void setTile(int x, int y, TileType tileType) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			tileTypes[x][y] = tileType;
		}
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


	/**
	 * Hanki TileType kartalta. Jos sijainti ulkopuolella, palauta default
	 *
	 * @param x           Sijainnin x-koordinaatti
	 * @param y           Sijainnin y-koordinaatti
	 * @param defaultTile oletusarvoinen Tile, jos muuta ei löydetä.
	 * @return TIle sijainnin kohdalla. Jos sijainti kartan ulkopuolella, niin default
	 */
	public TileType getSafe(int x, int y, TileType defaultTile) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			return tileTypes[x][y];
		}
		return defaultTile;
	}


	/**
	 * Iteroi soluautomaattia
	 */
	public void iterate() {
		TileType[][] oldTiles = tileTypes;
		TileType[][] newTiles = new TileType[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (oldTiles[i][j] == TileType.DefinetlyFloor || oldTiles[i][j] == TileType.DefinetlyWall) {
					newTiles[i][j] = oldTiles[i][j];
					continue;
				}

				int wallsAround = 0;
				for (int k = -1; k <= 1; k++) {
					for (int l = -1; l <= 1; l++) {
						TileType inspectedTile = getSafe(i + k, j + l, TileType.DefinetlyWall);
						if (inspectedTile == TileType.DefinetlyWall || inspectedTile == TileType.MaybeWall) {
							wallsAround++;
						}
					}
				}
				if (wallsAround >= 5) {
					newTiles[i][j] = TileType.MaybeWall;
				} else {
					newTiles[i][j] = TileType.MaybeFloor;
				}
			}
		}
		this.tileTypes = newTiles;
	}

	void solidify() {
		TileType[][] oldTiles = tileTypes;
		TileType[][] newTiles = new TileType[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				TileType previuousTile = oldTiles[i][j];
				if (previuousTile == TileType.DefinetlyFloor || previuousTile == TileType.MaybeFloor) {
					newTiles[i][j] = TileType.DefinetlyFloor;
				} else {
					newTiles[i][j] = TileType.DefinetlyWall;
				}
			}
		}
		tileTypes = newTiles;
	}
}
