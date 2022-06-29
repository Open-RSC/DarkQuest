package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.connection.RSCPacket;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.model.snapshot.Activity;
import org.darkquest.gs.phandler.PacketHandler;


public class Sleepword implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		long now = System.currentTimeMillis();

		try {
		    String sleepword = ((RSCPacket) p).readString().trim();
		    if (System.currentTimeMillis() - player.getLastSleepTime() < 1000)
		    	return;
		    if (sleepword.equalsIgnoreCase("-null-")) {
		    	player.getActionSender().sendEnterSleep();
		    	player.setLastSleepTime(now);
		    } 
		    else {
				if (!player.isSleeping) { 
					return; 
				}
				if (sleepword.equalsIgnoreCase(player.getSleepword()))
				    player.getActionSender().sendWakeUp(true);
				else {
				    world.addEntryToSnapshots(new Activity(player.getUsername(),player.getUsername() + " failed a sleepword"));
				    player.getActionSender().sendIncorrectSleepword();
				    player.getActionSender().sendEnterSleep();
				    player.setLastSleepTime(now);
				}
		    }
		} 
		catch (Exception e) {
		    if (player.isSleeping) {
				player.getActionSender().sendIncorrectSleepword();
				player.getActionSender().sendEnterSleep();
		    }
		}
    }
}
