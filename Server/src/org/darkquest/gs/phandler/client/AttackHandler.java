package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.config.Constants;
import org.darkquest.config.Formulae;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.connection.RSCPacket;
import org.darkquest.gs.event.FightEvent;
import org.darkquest.gs.event.RangeEvent;
import org.darkquest.gs.event.WalkToMobEvent;
import org.darkquest.gs.model.InvItem;
import org.darkquest.gs.model.Mob;
import org.darkquest.gs.model.Npc;
import org.darkquest.gs.model.PathGenerator;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;
import org.darkquest.gs.plugins.PluginHandler;
import org.darkquest.gs.states.Action;



public class AttackHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		int pID = ((RSCPacket) p).getID();
		if (player.isBusy()) {
		    player.resetPath();
		    return;
		}
		player.resetAll();
		Mob affectedMob = null;
		int serverIndex = p.readShort();
		if (pID == 57) {
		    affectedMob = world.getPlayer(serverIndex);
		}
		else if (pID == 73) {
		    affectedMob = world.getNpc(serverIndex);
		}
		if(player.isPMod() && !player.isMod())
		    return;
		if (affectedMob == null || affectedMob.equals(player)) {
		    player.resetPath();
		    return;
		}
		if (affectedMob instanceof Player) {
		    Player pl = (Player) affectedMob;
		    if (pl.inCombat() && player.getRangeEquip() < 0) { 
		    	return;
		    }
		    if (pl.getLocation().inWilderness() && System.currentTimeMillis() - pl.getLastRun() < 3000) {
		    	return;
		    }
		}
	
		player.setFollowing(affectedMob);
		player.setStatus(Action.ATTACKING_MOB);
	
		if (player.getRangeEquip() < 0) {
		    World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, affectedMob instanceof Npc ? 1 : 2) {
				public void arrived() {
				    owner.resetPath();
				    owner.resetFollowing();
					if (owner.isBusy() || affectedMob.isBusy() || !owner.nextTo(affectedMob) || !owner.checkAttack(affectedMob, false) || owner.getStatus() != Action.ATTACKING_MOB) {
					    return;
					}
					if(affectedMob instanceof Player) {
						if(PluginHandler.getPluginHandler().blockDefaultAction("PlayerAttack", new Object[] { owner, affectedMob })) {
							return;
						}
					}
					
					
		
				    owner.resetAll();
				    owner.setStatus(Action.FIGHTING_MOB);
				    if (affectedMob instanceof Player) {
						Player affectedPlayer = (Player) affectedMob;
						owner.setSkulledOn(affectedPlayer);
						affectedPlayer.resetAll();
						affectedPlayer.setStatus(Action.FIGHTING_MOB);
						affectedPlayer.getActionSender().sendSound("underattack");
						affectedPlayer.getActionSender().sendMessage("You are under attack!");
						if (affectedPlayer.isSleeping()) {
							affectedPlayer.getActionSender().sendWakeUp(false);
						}
				    }
				    affectedMob.resetPath();
		
				    owner.setLocation(affectedMob.getLocation(), true);
				    
				    for (Player p : owner.getViewArea().getPlayersInView()) {
				    	p.removeWatchedPlayer(owner);
				    }
		
				    owner.setBusy(true);
				    owner.setSprite(9);
				    owner.setOpponent(affectedMob);
				    owner.setCombatTimer();
				    affectedMob.setBusy(true);
				    affectedMob.setSprite(8);
				    affectedMob.setOpponent(owner);
				    affectedMob.setCombatTimer();
				    FightEvent fighting = new FightEvent(owner, affectedMob);
				    fighting.setLastRun(0);
				    World.getWorld().getDelayedEventHandler().add(fighting);
				}
		    });
		} 
		else {
		    if (!new PathGenerator(player.getX(), player.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
				player.getActionSender().sendMessage("I can't get a clear shot from here");
				player.resetPath();
				return;
		    }
		    if(Constants.GameServer.F2P_WILDY && player.getLocation().inWilderness()) {
				for(InvItem i : player.getInventory().getItems()) {
				    if(i.getID() == 638 || i.getID() == 640 || i.getID() == 642 || i.getID() == 644 || i.getID() == 646) {
					player.getActionSender().sendMessage("You can not have any P2P arrows in your inventory in a F2P wilderness");
					return;
				    }
				}
			
		    }
			if(affectedMob instanceof Player) {
				if(PluginHandler.getPluginHandler().blockDefaultAction("PlayerRange", new Object[] { player, affectedMob })) {
					return;
				}
			}	
			else {
				if(PluginHandler.getPluginHandler().blockDefaultAction("PlayerRangeNpc", new Object[] { player, affectedMob })) {
					return;
				}
			}
		    int radius = 7;
		    if (player.getRangeEquip() == 59 || player.getRangeEquip() == 60)
		    	radius = 5;
		    if (player.getRangeEquip() == 189)
		    	radius = 4;
		    World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, radius) {
				public void arrived() {
				    owner.resetPath();
				    if (owner.isBusy() || !owner.checkAttack(affectedMob, true) || owner.getStatus() != Action.ATTACKING_MOB) {
				    	return;
				    }
				    if (!new PathGenerator(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
						owner.getActionSender().sendMessage("I can't get a clear shot from here");
						owner.resetPath();
						return;
				    }
				    owner.resetAll();
				    owner.setStatus(Action.RANGING_MOB);
				    if (affectedMob instanceof Player) {
						Player affectedPlayer = (Player) affectedMob;
						owner.setSkulledOn(affectedPlayer);
						affectedPlayer.resetTrade();
						if (affectedPlayer.getMenuHandler() != null) {
						    affectedPlayer.resetMenuHandler();
						}
						if (affectedPlayer.accessingBank()) {
						    affectedPlayer.resetBank();
						}
						if (affectedPlayer.accessingShop()) {
						    affectedPlayer.resetShop();
						}
						if (affectedPlayer.getNpc() != null) {
						    affectedPlayer.getNpc().unblock();
						    affectedPlayer.setNpc(null);
						}
				    }
				    if (Formulae.getRangeDirection(owner, affectedMob) != -1)
				    	owner.setSprite(Formulae.getRangeDirection(owner, affectedMob));
				    owner.setRangeEvent(new RangeEvent(owner, affectedMob));
				}
		    });
		}
    }
}