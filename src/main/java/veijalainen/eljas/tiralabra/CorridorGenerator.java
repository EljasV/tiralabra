package veijalainen.eljas.tiralabra;

import veijalainen.eljas.tiralabra.util.Pair;

import java.util.*;

public class CorridorGenerator {
	final HashMap<RoomInfo, Set<Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge>>> possibleConnections;

	final Set<RoomInfo> unconnectedRooms;
	final Map<RoomInfo, Set<Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge>>> connections;


	/**
	 * Käsittelee huoneita verkkona ja luo niistä sokkelon kaltaisen verkon, kuitenkin niin, että jokaisella huoneella on käytävä vähintään kahteen toiseen huoneeseen.
	 *
	 * @param possibleConnections Mahdolliset vierekkäiset huoneet
	 */
	public CorridorGenerator(HashMap<RoomInfo, Set<Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge>>> possibleConnections) {
		this.possibleConnections = possibleConnections;
		this.unconnectedRooms = new HashSet<>(possibleConnections.keySet());
		this.connections = new HashMap<>();

		RoomInfo firstInfo = unconnectedRooms.stream().findAny().get();
		unconnectedRooms.remove(firstInfo);
		while (!unconnectedRooms.isEmpty()) {
			//Etsii sellaisen huoneen, joka ei ole yhdistetty puuhun ja jonka jokin mahdolllinen naapuri tas on
			RoomInfo roomInfo1 = unconnectedRooms.stream().filter(roomInfo -> possibleConnections.get(roomInfo).stream().anyMatch(roomInfo2 -> !unconnectedRooms.contains(roomInfo2.left))).findAny().get();
			unconnectedRooms.remove(roomInfo1);

			Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge> roomInfoHalfEdgePair2 = possibleConnections.get(roomInfo1).stream().filter(roomInfo -> !unconnectedRooms.contains(roomInfo.left)).findAny().get();
			RoomInfo roomInfo2 = roomInfoHalfEdgePair2.left;

			connections.putIfAbsent(roomInfo1, new HashSet<>());
			connections.putIfAbsent(roomInfo2, new HashSet<>());

			connections.get(roomInfo1).add(roomInfoHalfEdgePair2);
			connections.get(roomInfo2).add(new Pair<>(roomInfo1, roomInfoHalfEdgePair2.right.twin));
		}

		while (connections.values().stream().anyMatch(roomInfos -> roomInfos.size() < 2)) {
			Map.Entry<RoomInfo, Set<Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge>>> fromEntry = connections.entrySet().stream().filter(roomInfoSetEntry -> roomInfoSetEntry.getValue().size() < 2).findAny().get();
			Pair<RoomInfo, DoublyConnectedEdgeList.HalfEdge> toRoom = possibleConnections.get(fromEntry.getKey()).stream().filter(roomInfo -> !roomInfo.equals(fromEntry.getValue().stream().findAny().get())).findAny().get();

			fromEntry.getValue().add(toRoom);
			connections.get(toRoom.left).add(new Pair<>(fromEntry.getKey(), toRoom.right.twin));
		}

	}


}
