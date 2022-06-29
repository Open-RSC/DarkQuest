package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.plugins.PluginHandler;


public class PlayerLogoutRequest implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		
		if(PluginHandler.getPluginHandler().blockDefaultAction("PlayerLogout", new Object[] { player }, false)) {
			player.getActionSender().sendCantLogout();
			return;
		}
		
		if (player.canLogout()) {
			/**
		     * Destroy player
		     */
			player.destroy(true);
		} 
		else {
		    player.getActionSender().sendCantLogout();
		}
    }
}
