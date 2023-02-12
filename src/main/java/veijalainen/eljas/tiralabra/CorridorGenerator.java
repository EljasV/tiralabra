package veijalainen.eljas.tiralabra;

import java.util.*;

public class CorridorGenerator {
	final Map<RoomInfo, Set<RoomInfo>> possibleConnections;

	final Set<RoomInfo> unconnectedRooms;
	final Map<RoomInfo, Set<RoomInfo>> connections;


	/**
	 * Käsittelee huoneita verkkona ja luo niistä sokkelon kaltaisen verkon, kuitenkin niin, että jokaisella huoneella on käytävä vähintään kahteen toiseen huoneeseen.
	 *
	 * @param possibleConnections Mahdolliset vierekkäiset huoneet
	 */
	public CorridorGenerator(Map<RoomInfo, Set<RoomInfo>> possibleConnections) {
		this.possibleConnections = possibleConnections;
		this.unconnectedRooms = new HashSet<>(possibleConnections.keySet());
		this.connections = new HashMap<>();

		RoomInfo firstInfo = unconnectedRooms.stream().findAny().get();
		unconnectedRooms.remove(firstInfo);
		while (!unconnectedRooms.isEmpty()) {
			//Etsii sellaisen huoneen, joka ei ole yhdistetty puuhun ja jonka jokin mahdolllinen naapuri tas on
			RoomInfo roomInfo1 = unconnectedRooms.stream().filter(roomInfo -> possibleConnections.get(roomInfo).stream().anyMatch(roomInfo2 -> !unconnectedRooms.contains(roomInfo2))).findAny().get();
			unconnectedRooms.remove(roomInfo1);

			RoomInfo roomInfo2 = possibleConnections.get(roomInfo1).stream().filter(roomInfo -> !unconnectedRooms.contains(roomInfo)).findAny().get();

			connections.putIfAbsent(roomInfo1, new HashSet<>());
			connections.putIfAbsent(roomInfo2, new HashSet<>());

			connections.get(roomInfo1).add(roomInfo2);
			connections.get(roomInfo2).add(roomInfo1);
		}

		while (connections.values().stream().anyMatch(roomInfos -> roomInfos.size() < 2)) {
			Map.Entry<RoomInfo, Set<RoomInfo>> fromEntry = connections.entrySet().stream().filter(roomInfoSetEntry -> roomInfoSetEntry.getValue().size() < 2).findAny().get();
			RoomInfo toRoom = possibleConnections.get(fromEntry.getKey()).stream().filter(roomInfo -> roomInfo != fromEntry.getValue().stream().findAny().get()).findAny().get();

			fromEntry.getValue().add(toRoom);
			connections.get(toRoom).add(fromEntry.getKey());
		}

	}


}
