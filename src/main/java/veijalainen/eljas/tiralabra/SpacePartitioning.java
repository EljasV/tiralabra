package veijalainen.eljas.tiralabra;

import java.util.Random;

/**
 * Edustaa puuta, joka pitää sisällään alueita, jonka jokaiseen lehtisolmuun rakennetaan huone.
 */
public class SpacePartitioning {
	final int width;
	final int height;

	static Random random = new Random();


	/**
	 * Edustaa puun solmua.
	 */
	static class Node {

		/**
		 * Pitää sisällään vasemman tai ylemmän lapsen. Jos on null, niin solmulla ei ole lapsia.
		 */
		Node left;
		/**
		 * Pitää sisällään oikean tai alemman lapsen. Jos on null, niin solmulla ei ole lapsia.
		 */
		Node right;
		/**
		 * Onko solmun lapset rinnakkain ({@code  true}) vai päällekkäin ({@code false})
		 */
		boolean isHorizontal;
		final int width, height;

		/**
		 * @param width        Solmun kattavan alueen leveys
		 * @param height       Solmun kattaman alueen korkeus
		 * @param isHorizontal Onko solmun lapset rinnakkain ({@code  true}) vai päällekkäin ({@code false})
		 * @param depth        Rekursion syvyys, vähenee syvemmälle mennessä, kunnes 0 eikä rekursio jatku.
		 */
		public Node(int width, int height, boolean isHorizontal, int depth) {
			this.width = width;
			this.height = height;
			this.isHorizontal = isHorizontal;
			if (depth > 0) {
				if (isHorizontal) {
					int split = (int) (random.nextDouble() * (width * 1d / 3d) + width * 1d / 3d);

					left = new Node(split, height, !isHorizontal, depth - 1);
					right = new Node(width - split, height, !isHorizontal, depth - 1);
				} else {
					int split = (int) (random.nextDouble() * (height * 1d / 3d) + height * 1d / 3d);
					left = new Node(width, split, !isHorizontal, depth - 1);
					right = new Node(width, height - split, !isHorizontal, depth - 1);
				}
			}
		}

	}

	Node root;

	/**
	 * Luo uuden SpacePartitioningin
	 *
	 * @param width  Kartan leveys
	 * @param height Kartan korkeus
	 */
	public SpacePartitioning(int width, int height) {
		this.width = width;
		this.height = height;
		root = new Node(width, height, random.nextBoolean(), 4);
	}

	@FunctionalInterface
	interface Visitor {
		void visit(Node node, int x, int y);
	}


	/**
	 * Rekursiivinen apufunktio puun läpikäymiseen
	 *
	 * @param node    Pitää sisällään solmun rekursiossa. Käyttäjä kutsuu puun juurisolmulla
	 * @param x       Pitää sisällään x-koordinaatin rekursiossa. Käyttäjä kutsuu arvolla 0.
	 * @param y       Pitää sisällään y-koordinaatin rekursiossa. Käyttäjä kutsuu arvolla 0.
	 * @param visitor Funktio, jonka halutaa suoritettavan kunkin lehtisolmun kohdalla. Saa parametriksi {@link Node}, x- sijainnin ja y-sijainnin.
	 */
	static void visit(Node node, int x, int y, Visitor visitor) {
		if (node.left == null) {
			visitor.visit(node, x, y);
		} else {
			visit(node.left, x, y, visitor);
			if (node.isHorizontal) {
				visit(node.right, x + node.left.width, y, visitor);
			} else {
				visit(node.right, x, y + node.left.height, visitor);
			}
		}
	}
}