package veijalainen.eljas.tiralabra;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Tietorakenne, jonka avulla tiedämme mitkä huoneet ovat vierekkäin.
 * Toteutus olettaa, että käytämme vain suorakulmion muotoisia alueita ja alueet eivät ole toistensa päällä. Muodostamme verkon aina uudelleen, kun rakenteeseen lisätään suorakulmio.
 */
public class DoublyConnectedEdgeList {

	public Set<HalfEdge> edges;

	Set<Vertex> vertices;

	Set<Face> faces;

	public DoublyConnectedEdgeList() {
		edges = new HashSet<>();
		vertices = new HashSet<>();
		faces = new HashSet<>();
	}

	public class HalfEdge {
		public Face face;
		public HalfEdge next;
		HalfEdge previous;
		public Vertex origin;
		public HalfEdge twin;

		public HalfEdge(Face face, Vertex origin) {
			this.face = face;
			this.origin = origin;
		}
	}

	public class Face {
		public RoomInfo roomInfo;
		Vertex topLeft;
		Vertex topRight;
		Vertex bottomRight;
		Vertex bottomLeft;

		public Face() {
		}
	}

	public class Vertex {
		public int x;
		public int y;

		public Vertex(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Vertex vertex = (Vertex) o;
			return x == vertex.x && y == vertex.y;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}
	}


	/**
	 * Lisää suorakulmio rakenteeseen.
	 * 1. Luo tahkon ja lisää sen tiedettyihin tahkoihin
	 * 2. Hakee suorakulmion kulmat ja tallentaa ne tahkoon
	 * 3. Uudelleenluo puolisärmät
	 * 4. Etsii puolisärmien kaksoset
	 *
	 * @param x      Suorakulmion vasemman yläkulman x- koordinaatti
	 * @param y      Suorakulmion vasemman yläkulman y-koordinaatti
	 * @param width  Suorakulmion leveys
	 * @param height Suorakulmion korkeus
	 * @return
	 */
	public Face addRectangle(int x, int y, int width, int height) {
		Face face = new Face();
		faces.add(face);

		face.topLeft = getVertex(x, y);
		face.topRight = getVertex(x + width, y);
		face.bottomRight = getVertex(x + width, y + height);
		face.bottomLeft = getVertex(x, y + height);

		createEdges();

		correctTwins();
		return face;
	}

	/**
	 * Etsii puolisärmien kaksoset ja tallentaa ne tietorakenteeseen
	 */
	private void correctTwins() {
		for (HalfEdge halfEdge1 :
				  edges) {
			for (HalfEdge halfEdge2 :
					  edges) {
				if (halfEdge1.origin == halfEdge2.next.origin && halfEdge1.next.origin == halfEdge2.origin) {
					halfEdge1.twin = halfEdge2;
					halfEdge2.twin = halfEdge1;
				}
			}

		}
	}


	/**
	 * Uudelleenluo puolisärmät
	 *
	 * Luo uuden särmäjoukon vanhan tilalla
	 * Käy läpi jokaisen tahkon
	 * Lisää listaan jokaisen tahkon rajan alueella olevan kärjen
	 * Luo särmän jokaisen kärjen välille
	 */
	private void createEdges() {
		edges = new HashSet<>();
		for (Face face :
				  faces) {
			List<Vertex> vertices = new ArrayList<>();
			// Yläsärmä, sisältää kaikki kärjet, jotka ovat yläsärmän alueella, poislukien oikea yläkulma
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.y == face.topLeft.y && vertex.x >= face.topLeft.x && vertex.x < face.topRight.x).sorted(Comparator.comparingInt(o -> o.x)).collect(Collectors.toList()));
			// Oikea särmä, sisältää kaikki kärjet, jotka ovat oikean särmän alueella, poislukien oikea alakulma
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.x == face.topRight.x && vertex.y >= face.topRight.y && vertex.y < face.bottomRight.y).sorted(Comparator.comparingInt(o -> o.y)).collect(Collectors.toList()));
			// Alasärmä, sisältää kaikki kärjet, jotka ovat alasärmän alueella, poislukien vasen alakulma
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.y == face.bottomRight.y && vertex.x > face.bottomLeft.x && vertex.x <= face.bottomRight.x).sorted((o1, o2) -> o2.x - o1.x).collect(Collectors.toList()));
			// Vasen särmä, sisältää kaikki kärjet, jotka ovat vasemman särmän alueella, poislukien vasen yläkulma
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.x == face.bottomLeft.x && vertex.y > face.topLeft.y && vertex.y <= face.bottomLeft.y).sorted((o1, o2) -> o2.y - o1.y).collect(Collectors.toList()));

			HalfEdge firstEdge = new HalfEdge(face, vertices.get(0));
			edges.add(firstEdge);
			HalfEdge previous = firstEdge;
			for (int i = 1; i < vertices.size(); i++) {
				Vertex origin = vertices.get(i);
				HalfEdge edge = new HalfEdge(face, origin);
				previous.next = edge;
				edge.previous = previous;

				previous = edge;
				edges.add(edge);
			}
			previous.next = firstEdge;
			firstEdge.previous = previous;
		}
	}

	/**
	 * Apumetodi, jonka avulla estetään duplikaatit ja pidetään Javan viittaukset kunnossa.
	 *
	 * @param x Kärjen x-koordinaatti
	 * @param y Kärjen y-koordinaatti
	 * @return Olemassa oleva kärki tai uusi kärki, joka on lisätty muiden kärkien joukkoon.
	 */
	private Vertex getVertex(int x, int y) {
		Optional<Vertex> optional = this.vertices.stream().filter(vertex -> vertex.x == x && vertex.y == y).findFirst();
		Vertex vertex;
		if (optional.isPresent()) {
			return optional.get();
		} else {
			vertex = new Vertex(x, y);
			this.vertices.add(vertex);
			return vertex;
		}
	}
}
