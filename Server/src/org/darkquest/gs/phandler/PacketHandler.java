package org.darkquest.gs.phandler;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;


public interface PacketHandler {
    public void handlePacket(Packet p, IoSession session) throws Exception;
}
