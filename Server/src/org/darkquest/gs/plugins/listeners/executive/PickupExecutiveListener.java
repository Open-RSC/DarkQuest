package org.darkquest.gs.plugins.listeners.executive;

import org.darkquest.gs.model.InvItem;
import org.darkquest.gs.model.Player;

public interface PickupExecutiveListener {
	/**
	 * Return true if you wish to prevent a user from picking up an item
	 */
	public boolean blockPickup(Player p, InvItem i);
}
