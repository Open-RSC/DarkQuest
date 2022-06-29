package org.darkquest.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;


public class FriendLogin implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Player player = world.getPlayer(p.readLong());
		if (player == null) {
		    return;
		}
		long friend = p.readLong();
		if (player.isFriendsWith(friend)) {
		    player.getActionSender().sendFriendUpdate(friend, p.readShort());
		}
    }

}