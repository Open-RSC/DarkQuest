package org.darkquest.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.builders.ls.StatRequestPacketBuilder;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;


public class StatRequestHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();
    
    private StatRequestPacketBuilder builder = new StatRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Logger.event("LOGIN_SERVER requested stats (uID: " + uID + ")");
		builder.setUID(uID);
		LSPacket temp = builder.getPacket();
		if (temp != null) {
		    session.write(temp);
		}
    }

}