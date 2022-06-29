package org.darkquest.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.builders.ls.ReportInfoRequestPacketBuilder;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;


public class ReportInfoRequestHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();
    
    private ReportInfoRequestPacketBuilder builder = new ReportInfoRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Logger.event("LOGIN_SERVER requested report information (uID: " + uID + ")");
		Player player = world.getPlayer(p.readLong());
		if (player == null) {
		    return;
		}
		builder.setUID(uID);
		builder.setPlayer(player);
		LSPacket temp = builder.getPacket();
		if (temp != null) {
		    session.write(temp);
		}
    }

}