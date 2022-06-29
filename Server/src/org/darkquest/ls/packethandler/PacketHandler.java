package org.darkquest.ls.packethandler;

import org.apache.mina.common.IoSession;
import org.darkquest.ls.net.Packet;


public interface PacketHandler {
    public void handlePacket(Packet p, IoSession session) throws Exception;
}
