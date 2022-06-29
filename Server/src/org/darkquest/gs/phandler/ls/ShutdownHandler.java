package org.darkquest.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.util.Logger;


public class ShutdownHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Logger.event("LOGIN_SERVER requested shutdown");
		World.getWorld().getServer().kill();
    }

}