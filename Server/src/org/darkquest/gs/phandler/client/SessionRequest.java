package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.config.Formulae;
import org.darkquest.gs.builders.RSCPacketBuilder;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;



public class SessionRequest implements PacketHandler {

	/**
	 * World instance
	 */
	public static final World world = World.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if(player.isInitialized()) {
			Logger.println("[WARNING] SessionRequest for already Initialized player!");
			return;
		}
		byte userByte = p.readByte();
		player.setClassName(p.readString().trim());
		long serverKey = Formulae.generateSessionKey(userByte);
		player.setServerKey(serverKey);
		RSCPacketBuilder pb = new RSCPacketBuilder();
		pb.setBare(true);
		pb.addLong(serverKey);
		session.write(pb.toPacket());
	}
}
