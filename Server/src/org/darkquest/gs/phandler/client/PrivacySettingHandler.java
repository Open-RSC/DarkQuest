package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.builders.ls.PrivacySettingUpdatePacketBuilder;
import org.darkquest.gs.connection.LSPacket;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;


public class PrivacySettingHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();
    
    private PrivacySettingUpdatePacketBuilder builder = new PrivacySettingUpdatePacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
	
		boolean[] newSettings = new boolean[4];
		for (int i = 0; i < 4; i++) {
		    newSettings[i] = p.readByte() == 1;
		}
	
		builder.setPlayer(player);
		for (int i = 0; i < 4; i++) {
		    builder.setIndex(i);
		    if (newSettings[i] && !player.getPrivacySetting(i)) {
		    	builder.setOn(true);
		    } 
		    else if (!newSettings[i] && player.getPrivacySetting(i)) {
		    	builder.setOn(false);
		    } 
		    else {
		    	continue;
		    }
		    LSPacket packet = builder.getPacket();
		    if (packet != null) {
		    	World.getWorld().getServer().getLoginConnector().getSession().write(packet);
		    }
		}
		for (int i = 0; i < 4; i++) {
		    player.setPrivacySetting(i, newSettings[i]);
		}
    }

}
