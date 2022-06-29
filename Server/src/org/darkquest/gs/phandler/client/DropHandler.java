package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.event.DelayedEvent;
import org.darkquest.gs.model.InvItem;
import org.darkquest.gs.model.Item;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.plugins.PluginHandler;
import org.darkquest.gs.states.Action;


public class DropHandler implements PacketHandler {
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
		player.resetAll();
		final int idx = (int) p.readShort();
		if (idx < 0 || idx >= player.getInventory().size()) {
		    player.setSuspiciousPlayer(true);
		    return;
		}
		final InvItem item = player.getInventory().get(idx);
		if (item == null) {
		    player.setSuspiciousPlayer(true);
		    return;
		}
		if(player.isPMod() && !player.isMod())
		    return;
		player.setStatus(Action.DROPPING_GITEM);
		World.getWorld().getDelayedEventHandler().add(new DelayedEvent(player, 500) {
		    public void run() {
			if (owner.isBusy() || !owner.getInventory().contains(item) || owner.getStatus() != Action.DROPPING_GITEM) {
			    matchRunning = false;
			    return;
			}
			if (owner.hasMoved()) {
			    return;
			}
			if(PluginHandler.getPluginHandler().blockDefaultAction("Drop", new Object[] { owner, item })) {
				return;
			}
			owner.getActionSender().sendSound("dropobject");
			owner.getInventory().remove(item);
			owner.getActionSender().sendInventory();
			world.registerItem(new Item(item.getID(), owner.getX(), owner.getY(), item.getAmount(), owner));
			matchRunning = false;
		    }
		});
    }
}
