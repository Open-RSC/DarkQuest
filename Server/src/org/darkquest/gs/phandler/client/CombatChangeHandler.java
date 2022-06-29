package org.darkquest.gs.phandler.client;

import org.apache.mina.common.IoSession;
import org.darkquest.gs.connection.Packet;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.phandler.PacketHandler;


public class CombatChangeHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = World.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		/**
		 * 1 adds, 0 removes
		 */
		int idx = (int) p.readByte();
		if (idx < 0 || idx > 1) {
		    player.setSuspiciousPlayer(true);
		    return;
		}
		int skill = (int) p.readByte();
		if (skill < 0 || skill > 7 || skill == 3) {
		    player.setSuspiciousPlayer(true);
		    return;
		}
		int option = (int) p.readByte();
		if (option < 0 || option > 5) {
		    player.setSuspiciousPlayer(true);
		    return;
		}
		if(!player.getCache().hasKey("lastXpPointsUpdate")) {
			player.getCache().store("lastXpPointsUpdate", 0L);
		}
		if(player.getLocation().inWilderness()) {
			player.getActionSender().sendMessage("Please leave the wilderness to change your stats!");
			return;
		}
		if(player.getCurStat(3) < player.getMaxStat(3)) {
			player.getActionSender().sendMessage("You can only change your stats when you are full HP.");
			return;
		}
		if(player.getCombatLevel() >= 30 && System.currentTimeMillis() - player.getCache().getLong("lastXpPointsUpdate") > 60000 * 2&& System.currentTimeMillis() - player.getCache().getLong("lastXpPointsUpdate") < 60000 * 20) {
			player.getActionSender().sendMessage("You cannot change your stats for another: @red@"+ (20 - (System.currentTimeMillis() - player.getCache().getLong("lastXpPointsUpdate"))/60000) + " minutes.");
			return;
		}
		if(player.getCombatLevel() >= 30 && System.currentTimeMillis() - player.getCache().getLong("lastXpPointsUpdate") > 60000 * 20) {
			player.getActionSender().sendMessage("@red@You have 2 minutes to change your stats, before a 20 minute cool-down period starts!");
			player.getCache().update("lastXpPointsUpdate", System.currentTimeMillis());	
			return;
		}
		
		
		/**
		 * Remove XP
		 */
		if(idx == 0) {
			if(player.getExp(skill) < optionToXp(option, 0)) {
				return;
			}
			player.getCache().update("xppoints", player.getCache().getLong("xppoints") + optionToXp(option, skill));
			player.getActionSender().sendCacheableLong("xppoints", player.getCache().getLong("xppoints"));
			
			player.incExp(skill, 0 - ((int)(optionToXp(option, 0))));
			player.getActionSender().sendStat(skill);
			
			player.setCurStat(skill, player.getMaxStat(skill));
			
			if(skill > -1 && skill < 3) {
				player.incExp(3, 0 - ((int)(optionToXp(option, 0))/3));
			}
			player.getActionSender().sendStat(3);
		}
		/**
		 * Add XP
		 */
		else if(idx == 1) {
			if(player.getCache().getLong("xppoints") < optionToXp(option, skill)) {
				return;
			}
			player.getCache().update("xppoints", player.getCache().getLong("xppoints") - optionToXp(option, skill));
			player.getActionSender().sendCacheableLong("xppoints", player.getCache().getLong("xppoints"));
			
			player.incExp(skill, (int)(optionToXp(option, 0)));
			player.getActionSender().sendStat(skill);
			
			player.setCurStat(skill, player.getMaxStat(skill));
			
			if(skill > -1 && skill < 3) {
				player.incExp(3, ((int)(optionToXp(option, 0))/3));
			}
			player.getActionSender().sendStat(3);
		}
		
    }
    private Long[] xps = new Long[]{ 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L };
    public long optionToXp(int option, int skill) {
    	if(skill > -1 && skill < 3)
    		return xps[option];
    	if(skill > 3 && skill < 7) 
    		return xps[option]*3;
    	else
    		return 0;
    }
}