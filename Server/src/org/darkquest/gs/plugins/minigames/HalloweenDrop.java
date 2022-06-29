package org.darkquest.gs.plugins.minigames;

import org.darkquest.gs.event.DelayedEvent;
import org.darkquest.gs.event.MiniEvent;
import org.darkquest.gs.model.Item;
import org.darkquest.gs.model.Player;
import org.darkquest.gs.model.Point;
import org.darkquest.gs.model.World;
import org.darkquest.gs.plugins.listeners.action.CommandListener;
import org.darkquest.gs.plugins.listeners.action.StartupListener;
import org.darkquest.gs.tools.DataConversions;

public class HalloweenDrop implements 
	CommandListener, 
	StartupListener
	{
    /**
     * Is this running?
     */
    private boolean running;
    /*
     * Calls the StartupListener to check every hour to see if the halloween boolean is true or not.
     */
    public void onStartup() {
        World.getWorld().getDelayedEventHandler().add(new DelayedEvent(null, 60000*60) { // Ran every hour
            @Override
            public void run() {
                 if(running) { 
				 halloweendrop(422,1321,422); }
            }}
        );
    }
    /*
     * Command that sets the halloween boolean to true or false
     */
    public void onCommand(String command, String[] args, Player player) {
        if(command.equalsIgnoreCase("starthalloweendrop") && player.isAdmin()) {
            running = true;
            halloweendrop(422,1321,422); 
        }
        else if(command.equalsIgnoreCase("stophalloweendrop") && player.isAdmin()) {
            running = false;
        }
        else if(command.equalsIgnoreCase("halloweendropstatus") && player.isAdmin()) {
            player.getActionSender().sendMessage("Halloweendrop is set to " + running);
        }
    }
    
    /*
     * Spams the global chat area right before the drop occurs
     */
    public void spammsg() {
        World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 11000+15000) {
            @Override
            public void action() {
                World.getWorld().sendWorldAnnouncement("@or2@Dropping in 3 seconds!");
                World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                    @Override
                    public void action() {
                        World.getWorld().sendWorldAnnouncement("@or2@Dropping in 2 seconds!");
                        World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                            @Override
                            public void action() {
                                World.getWorld().sendWorldAnnouncement("@or2@Dropping in 1 second!");
                                World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                                    @Override
                                    public void action() {
                                        World.getWorld().sendWorldAnnouncement("@or2@Happy Halloween from DarkQuest!");
                                    }
                                });
                            }
                        });
                    }
                });
            }    
        });
    
    }
    Point[] droplocs = { new Point(291,568), new Point(219, 642), new Point(136, 507), new Point(220, 432) };
       
    public void halloweendrop(final int firstdrop, final int seconddrop, final int thirddrop) {
        World.getWorld().sendWorldAnnouncement("@or2@Halloween world drop started, dropping in 30 seconds!");
        World.getWorld().sendWorldAnnouncement("@whi@Get to Draynor, Falador (east bank), Varrock or Edgeville!");
        World.getWorld().sendWorldAnnouncement("@or2@Dropping 2 sets of pumpkins every hour on the hour!");
        spammsg();
        World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 30000) {
            @Override
            // Pumpkin drop set #1
            public void action() {
                for(int i = 0; i < 100; i++) {
                    Point coords = droplocs[DataConversions.random(0, droplocs.length-1)];
                    
                    int x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                    int y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                    while(World.getWorld().getTile(x, y).hasGameObject()) {
                        x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                        y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                        }
                    world.registerItem(new Item(firstdrop, x, y, 1, null, System.currentTimeMillis()-DataConversions.random(45000, 60000)));
                }
                spammsg();
                World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 30000) {
                    @Override
                    // XP box drop set #2
                    public void action() {
                        for(int i = 0; i < 100; i++) {
                            Point coords = droplocs[DataConversions.random(0, droplocs.length-1)];
                            
                            int x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                            int y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                            while(World.getWorld().getTile(x, y).hasGameObject()) {
                                x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                }
                            world.registerItem(new Item(seconddrop, x, y, 1, null, System.currentTimeMillis()-DataConversions.random(45000, 60000)));
                        }
                        if(thirddrop == 0) 
                            return;
                        spammsg();
                        World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 30000) {
                            @Override
                            // pumpkin drop set #3
                            public void action() {
                                for(int i = 0; i < 100; i++) {
                                    Point coords = droplocs[DataConversions.random(0, droplocs.length-1)];
                                    
                                    int x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                    int y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                    while(World.getWorld().getTile(x, y).hasGameObject()) {
                                        x = coords.getX()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                        y = coords.getY()+(DataConversions.random(0, 1) == 1 ? -20 : 0)+DataConversions.random(0, 20);
                                        }
                                    if(thirddrop != 0) 
                                    world.registerItem(new Item(thirddrop, x, y, 1, null, System.currentTimeMillis()-DataConversions.random(45000, 60000)));
                                }
                            
                            }
                        });
                    }
                });
            }
        });
    }
}