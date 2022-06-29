package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;


public class ExceptionHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
    	try {
	    	Player player = (Player) session.getAttachment();
			Logger.error("[CLIENT] Exception from " + player.getUsername() + ": " + p.readString());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}