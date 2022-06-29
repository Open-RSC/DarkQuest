package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.config.Formulae;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.event.WalkToMobEvent;
import org.darkquest.gs.model.Npc;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.model.snapshot.Activity;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.plugins.PluginHandler;
import org.darkquest.gs.states.Action;



public class TalkToNpcHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if (player.isBusy()) {
		    player.resetPath();
		    return;
		}
		if (System.currentTimeMillis() - player.lastNPCChat < 1500)
		    return;
		player.lastNPCChat = System.currentTimeMillis();
		player.resetAll();
		final Npc affectedNpc = world.getNpc(p.readShort());
		if (affectedNpc == null) {
		    return;
		}
		world.addEntryToSnapshots(new Activity(player.getUsername(), player.getUsername() + " talked to NPC ("+affectedNpc.getID()+") at: " + player.getX() + "/" + player.getY() + "|" + affectedNpc.getX() + "/" + affectedNpc.getY()));
	
	
		player.setFollowing(affectedNpc);
		player.setStatus(Action.TALKING_MOB);
		World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedNpc, 1) {
		    public void arrived() {
				owner.resetFollowing();
				owner.resetPath();
				if (owner.isBusy() || owner.isRanging() || !owner.nextTo(affectedNpc) || owner.getStatus() != Action.TALKING_MOB) {
				    return;
				}
				owner.resetAll();
				if (affectedNpc.isBusy()) {
				    owner.getActionSender().sendMessage(affectedNpc.getDef().getName() + " is currently busy.");
				    return;
				}
				affectedNpc.resetPath();
				
				if (Formulae.getDirection(owner, affectedNpc) != -1) {
				    affectedNpc.setSprite(Formulae.getDirection(owner, affectedNpc));
				    owner.setSprite(Formulae.getDirection(affectedNpc, owner));
				}
				
				
				if(PluginHandler.getPluginHandler().blockDefaultAction("TalkToNpc", new Object[] { owner, affectedNpc })) {
					return;
				}
			}
		});
    }
}
