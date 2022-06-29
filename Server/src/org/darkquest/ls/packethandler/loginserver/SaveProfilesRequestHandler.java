package org.darkquest.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import org.darkquest.ls.model.World;
import org.darkquest.ls.net.LSPacket;
import org.darkquest.ls.net.Packet;
import org.darkquest.ls.packetbuilder.loginserver.ReplyPacketBuilder;
import org.darkquest.ls.packethandler.PacketHandler;


public class SaveProfilesRequestHandler implements PacketHandler {
    private ReplyPacketBuilder builder = new ReplyPacketBuilder();

    public void handlePacket(Packet p, final IoSession session) throws Exception {
	final long uID = ((LSPacket) p).getUID();
	World world = (World) session.getAttachment();
	System.out.println("World " + world.getID() + " requested we save all profiles");
	try {
	    Runtime.getRuntime().exec("/home/rsca/unblock");
	} catch (Exception err) {
	    System.out.println(err);
	}

	boolean success = true;
	// Iterator iterator = world.getAssosiatedSaves().iterator();
	// while(iterator.hasNext()) {
	// PlayerSave profile = ((Entry<Long,
	// PlayerSave>)iterator.next()).getValue();
	// profile.save();
	// iterator.remove();
	// }

	builder.setUID(uID);
	builder.setSuccess(success);

	LSPacket packet = builder.getPacket();
	if (packet != null) {
	    session.write(packet);
	}
    }

}
