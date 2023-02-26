package veijalainen.eljas.tiralabra;

import org.junit.Test;

import static org.junit.Assert.*;

public class GridTest {
	@Test
	public void fillLineTouchesBeginAndMidpoint() {
		Grid grid = new Grid(100, 100);
		int fromX = 10;
		int fromY = 10;
		int toX = 90;
		int toY = 90;
		grid.fillLine(fromX, fromY, toX, toY, TileType.GrassWall);

		assertEquals(TileType.GrassWall, grid.getSafe(fromX, fromY, TileType.DefinetlyWall));
		assertEquals(TileType.GrassWall, grid.getSafe((fromX + toX) / 2, (fromY + toY) / 2, TileType.DefinetlyWall));
	}

	@Test
	public void iterateFullDoesNotChange() {
		Grid grid = new Grid(100, 100);
		grid.fillArea(0, 0, 100, 100, TileType.MaybeWall);
		grid.iterate();
		for (int i = 0; i < grid.width; i++) {
			for (int j = 0; j < grid.height; j++) {
				assertEquals(TileType.MaybeWall, grid.getSafe(i, j, TileType.GrassFloor));
			}
		}
	}
}
