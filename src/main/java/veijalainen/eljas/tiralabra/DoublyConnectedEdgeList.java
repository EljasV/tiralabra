package veijalainen.eljas.tiralabra;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DoublyConnectedEdgeList {

	public Set<HalfEdge> edges;

	Set<Vertex> vertices;

	private Set<Face> faces;

	public DoublyConnectedEdgeList() {
		edges = new HashSet<>();
		vertices = new HashSet<>();
		faces = new HashSet<>();
	}

	class HalfEdge {
		Face face;
		HalfEdge next;
		HalfEdge previous;
		Vertex origin;
		HalfEdge twin;

		public HalfEdge(Face face, Vertex origin) {
			this.face = face;
			this.origin = origin;
		}
	}

	class Face {
		int x, y;
		Vertex topLeft;
		Vertex topRight;
		Vertex bottomRight;
		Vertex bottomLeft;

		public Face(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	class Vertex {
		int x;
		int y;

		public Vertex(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

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

	void addRectangle(int x, int y, int width, int height) {
		Face face = new Face(x + width / 2, y + height / 2);
		faces.add(face);

		ArrayList<Vertex> vertexArrayList = new ArrayList<>();
		face.topLeft = getVertex(x, y);
		face.topRight = getVertex(x + width, y);
		face.bottomRight = getVertex(x + width, y + height);
		face.bottomLeft = getVertex(x, y + height);

		createEdges();

		correctTwins();
	}

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


	private void createEdges() {
		edges = new HashSet<>();
		for (Face face :
				  faces) {
			List<Vertex> vertices = new ArrayList<>();
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.y == face.topLeft.y && vertex.x >= face.topLeft.x && vertex.x < face.topRight.x).sorted(Comparator.comparingInt(o -> o.x)).collect(Collectors.toList()));
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.x == face.topRight.x && vertex.y >= face.topRight.y && vertex.y < face.bottomRight.y).sorted(Comparator.comparingInt(o -> o.y)).collect(Collectors.toList()));
			vertices.addAll(this.vertices.stream().filter(vertex -> vertex.y == face.bottomRight.y && vertex.x > face.bottomLeft.x && vertex.x <= face.bottomRight.x).sorted((o1, o2) -> o2.x - o1.x).collect(Collectors.toList()));
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
