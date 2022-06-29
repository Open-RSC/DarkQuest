package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.model.snapshot.Activity;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.tools.DataConversions;


public class ReportHandler implements PacketHandler {

	/**
	 * World instance
	 */
	public static final World world = World.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if (!player.canReport()) {
			player.getActionSender().sendMessage("You may only send one abuse report per minute.");
			return;
		}
		long temp = -121;
		byte b = 1;
		try {
			temp = p.readLong();
			b = p.readByte();
		}
		catch (Exception e) {
			return;
		}
		finally {
			if(temp == player.getUsernameHash()) {
				player.getActionSender().sendMessage("You can't report yourself!");
				return;
			}
			World.getWorld().getDB().getQueries().submitRepot(player.getUsernameHash(), temp, b, player);
			player.setLastReport();
			world.addEntryToSnapshots(new Activity(player.getUsername(),player.getUsername() + " sent a repot about: " + DataConversions.hashToUsername(temp)));
			player.getActionSender().sendMessage("Your report has been received, thank you.");
		}

	}
}
