package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Mob;
import org.darkquest.gs.model.Npc;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;


public class NpcCommand implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		int serverIndex = p.readShort();
		final Player player = (Player) session.getAttachment();
		if (player.isBusy()) {
		    return;
		}
	
		final Mob affectedMob = world.getNpc(serverIndex);
		final Npc affectedNpc = (Npc) affectedMob;
		if (affectedNpc == null || affectedMob == null || player == null)
		    return;
	
	    player.getActionSender().sendMessage("This feature is not implemented at the moment!");
	    return;
    }

}
