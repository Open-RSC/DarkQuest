package org.darkquest.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.builders.ls.PlayerListRequestPacketBuilder;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;


public class PlayerListRequestHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();
   
    private PlayerListRequestPacketBuilder builder = new PlayerListRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Logger.event("LOGIN_SERVER requested player list (uID: " + uID + ")");
		builder.setUID(uID);
		LSPacket temp = builder.getPacket();
		if (temp != null) {
		    session.write(temp);
		}
    }

}