package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.model.snapshot.Activity;
import org.darkquest.gs.phandler.PacketHandler;


public class FollowRequest implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	Player player = (Player) session.getAttachment();
	Player affectedPlayer = world.getPlayer(p.readShort());
	if (affectedPlayer == null) {
	    player.setSuspiciousPlayer(true);
	    return;
	}
	if (player.isBusy()) {
	    player.resetPath();
	    return;
	}
	if (System.currentTimeMillis() - player.getLastRun() < 3000)
	    return;
	player.resetAll();
	player.setFollowing(affectedPlayer, 1);
	player.getActionSender().sendMessage("Now following " + affectedPlayer.getUsername());
	world.addEntryToSnapshots(new Activity(player.getUsername(), player.getUsername() + " started to follow " + affectedPlayer.getUsername() + " at: " + player.getX() + "/" + player.getY() + " | " + affectedPlayer.getX() + "/" + affectedPlayer.getY()));

    }
}
