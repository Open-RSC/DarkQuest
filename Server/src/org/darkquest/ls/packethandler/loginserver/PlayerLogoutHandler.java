package org.darkquest.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import org.darkquest.ls.model.World;
import org.darkquest.ls.net.Packet;
import org.darkquest.ls.packethandler.PacketHandler;


public class PlayerLogoutHandler implements PacketHandler {

    public void handlePacket(Packet p, IoSession session) throws Exception {
	long user = p.readLong();
	World world = (World) session.getAttachment();
	world.unregisterPlayer(user);
    }
}
