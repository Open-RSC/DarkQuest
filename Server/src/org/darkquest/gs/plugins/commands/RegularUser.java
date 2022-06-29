package org.darkquest.gs.plugins.commands;

import java.util.ArrayList;

import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.World;
import org.darkquest.gs.model.snapshot.Chatlog;
import org.darkquest.gs.plugins.listeners.action.CommandListener;

public class RegularUser implements CommandListener {

	@Override
	public void onCommand(String command, String[] args, Player player) {
		if (command.equals("say")) {
			if(player.isMuted()) { 
				player.getActionSender().sendMessage("You are muted, you cannot send messages");
				return;
			}	
			if(System.currentTimeMillis() - player.getLastSayTime() < 10000 && !player.isPMod()) {
				player.getActionSender().sendMessage("You can only use ::say every 10 seconds");
				return;
			}
			player.setLastSayTime();
		    String newStr = "@gre@";
		    for (int i = 0; i < args.length; i++) {
		    	newStr += args[i] + " "; 
		    }
		    newStr = (player.isPMod() ? "#pmd# " : "") + player.getUsername() + ": " + newStr;
		    World.getWorld().sendWorldAnnouncement(newStr);
		    World.getWorld().addEntryToSnapshots(new Chatlog(player.getUsername(),"(Global) " + newStr, new ArrayList<String>()));
		    return;
		}
		else if (command.equals("help")) {
		    player.getActionSender().sendAlert("::skull (auto-skulls you)  @red@::fatigue  sets your fatigue to 100%   @yel@::ctf && ::leave (for capture the flag events) @cya@::online (shows you @cya@players online) @mag@::mod (shows you staff online) @blu@ ::stuck (teleports @blu@you to lumbridge, with a wait) @cya@::staff (staff list)", true);
		    return;
		}
		else if (command.equals("skull")) {
		    int length = 20;
		    player.addSkull(length * 60000);
		    return;
		}
		else if (command.equals("fatigue")) {
		    player.setFatigue(100);
		    player.getActionSender().sendFatigue();
		    player.getActionSender().sendMessage("Your fatigue is now 100%");
		    return;
		}
		else if (command.equals("online")) {
		    player.getActionSender().sendOnlinePlayers();
		    return;
		}
		else if (command.equals("onlinelist")) {
		    player.getActionSender().sendOnlinePlayersList();
		    return;
		}
		
		else if (command.equals("stuck")) {
		    if (System.currentTimeMillis() - player.getCurrentLogin() < 30000) {
				player.getActionSender().sendMessage("You cannot do this after you have recently logged in");
				return;
		    }
	    	if(!player.canLogout() || System.currentTimeMillis() - player.getLastMoved() < 10000) {
	    		player.getActionSender().sendMessage("You must stand peacefully in one place for 10 seconds!");
	    		return;
	    	}
		    if (player.getLocation().inModRoom() && !player.isMod()) {
		    	player.getActionSender().sendMessage("You cannot use ::stuck here");
		    } 
		    else if (!player.isMod() && System.currentTimeMillis() - player.getLastMoved() < 30000 && System.currentTimeMillis() - player.getCastTimer() < 300000) {
				player.getActionSender().sendMessage("There is a 30 second delay on using ::stuck, please stand still for 30 seconds.");
			}
		    else if (!player.inCombat() && System.currentTimeMillis() - player.getCombatTimer() > 30000 || player.isMod()) {
				player.setCastTimer();
				player.teleport(122, 647, true);
		    }
		    else {
		    	player.getActionSender().sendMessage("You cannot use ::stuck for 30 seconds after combat");
		    }
		    return;
		}
		return;
	}

}
