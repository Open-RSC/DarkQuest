package org.darkquest.gs.plugins.minigames;

import org.darkquest.gs.event.MiniEvent;
import org.darkquest.gs.model.Item;
import org.darkquest.gs.model.Point;
import org.darkquest.gs.model.World;
import org.darkquest.gs.tools.DataConversions;

public class WorldDrop {
	public void spammsg() {
    	World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 11000+15000) {
    		@Override
			public void action() {
				World.getWorld().sendWorldAnnouncement("@red@Dropping in 3 seconds!");
				World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
		    		@Override
					public void action() {
						World.getWorld().sendWorldAnnouncement("@red@Dropping in 2 seconds!");
						World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
				    		@Override
							public void action() {
								World.getWorld().sendWorldAnnouncement("@red@Dropping in 1 seconds!");
								World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 1000) {
						    		@Override
									public void action() {
										World.getWorld().sendWorldAnnouncement("@red@Happy New Years from DarkQuest!");
									}
						    	});
							}
				    	});
					}
		    	});
			}	
    	});
    
    }
	static Point[] droplocs = { new Point(291,568), new Point(219, 642), new Point(136, 507), new Point(220, 432) };
	   
    public void worlddrop(final int firstdrop, final int seconddrop, final int thirddrop) {
    	
    	World.getWorld().sendWorldAnnouncement("@red@World drop started, dropping in 30 seconds!");
    	World.getWorld().sendWorldAnnouncement("@whi@Get to Draynor, Falador (east bank), Varrock or Edgeville!");
    	World.getWorld().sendWorldAnnouncement("@red@Dropping 2 sets of Candy Canes every 4 hours!");
    	spammsg();
    	World.getWorld().getDelayedEventHandler().add(new MiniEvent(null, 30000) {
			@Override
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
