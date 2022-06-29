package org.darkquest.client.gui;

import org.darkquest.client.mudclient;
import org.darkquest.client.interfaces.InterfaceGUI;
import org.darkquest.client.interfaces.models.Callback;
import org.darkquest.client.interfaces.models.HoverCallback;
import org.darkquest.client.interfaces.models.ILabel;
import org.darkquest.client.interfaces.models.IPanel;
import org.darkquest.client.interfaces.models.OnMisClickCallBack;
import org.darkquest.client.interfaces.models.RSGuiModels;

public class StatMenu {
	
	public InterfaceGUI i;
	
	public mudclient mc;
	
	private int skillSelected = 0;
	
	private ILabel attack;
	
	private ILabel defense;
	
	private ILabel strength;
	
	private ILabel ranged;
	
	private ILabel prayer;
	
	private ILabel maged;
	
	private ILabel currentCombat;
	
	private ILabel currentXP;
	
	private ILabel currentCombatXp;
	
	private ILabel currentCombatSecondaryXp;
	
	private ILabel[] addXp = new ILabel[6];
	
	private ILabel[] removeXp = new ILabel[6];
	
	public void open() {
		this.i.isVisible = true;
		updateCurrentStats();
	}
	private Long[] xps = new Long[]{ 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L };
    private long optionToXp(int option, int skill) {
    	if(skill > -1 && skill < 3)
    		return xps[option];
    	if(skill > 3 && skill < 7) 
    		return xps[option]*3;
    	else
    		return 0;
    }
	public void updateCurrentStats() {
		attack.label = "  @gre@Attack ("+mc.playerStatBase[0]+")" ;
		defense.label = "  @gre@Defense ("+mc.playerStatBase[1]+")" ;
		strength.label = "  @gre@Strength ("+mc.playerStatBase[2]+")" ;
		ranged.label = "  @red@Ranged ("+mc.playerStatBase[4]+")" ;
		prayer.label = "  @red@Prayer ("+mc.playerStatBase[5]+")" ;
		maged.label = "  @red@Magic ("+mc.playerStatBase[6]+")" ;
		float at = mc.playerStatBase[0];
		float de = mc.playerStatBase[1];
		float st = mc.playerStatBase[2];
		float hp = mc.playerStatBase[3];
		float ra = mc.playerStatBase[4];
		float pr = mc.playerStatBase[5];
		float ma = mc.playerStatBase[6];
		
		
		
		double combatLvl;
		if (ra * 1.5 > st + at) {
			double cmb = (de / 4) + (hp / 4) + (pr / 8) + (ma / 8) + (ra / 2.66); // forumla for a ranged based
			combatLvl = (Math.round(cmb * 100.0) / 100.0);
		} 
		else {
			float cmb = (st / 4) + (de / 4) + (hp / 4) + (at / 4) + (pr / 8) + (ma / 8); 
			combatLvl = cmb;
		}
		currentCombat.label = "Combat level: " + combatLvl + "    Hits: " + mc.playerStatBase[3];
		currentCombatXp.label = "@gre@Spendable Combat xp@whi@: " + mc.cache.get("xppoints");
		currentCombatSecondaryXp.label = "@red@Spendable Combat xp@whi@: " + ((Long)mc.cache.get("xppoints")/3);
		currentXP.label = "Current XP: " + mc.playerStatExperience[skillSelected];
		
		for(int i = 0; i < 6; i++) {
			if(optionToXp(i,skillSelected) > (Long)mc.cache.get("xppoints")) {
				addXp[i].drawMe = false;
			}
			else {
				addXp[i].drawMe = true;
			}
		}
		for(int i = 0; i < 6; i++) {
			if(optionToXp(i,0) > mc.playerStatExperience[skillSelected]) {
				removeXp[i].drawMe = false;
			}
			else {
				removeXp[i].drawMe = true;
			}
		}
	}
	
	public StatMenu(final mudclient mc) {
	
		this.mc = mc;
		
		HoverCallback panelhover = new HoverCallback() {

			@Override
			public void hoverin(RSGuiModels c) {
				if(c instanceof IPanel) {
					((IPanel)c).borderColor = 0x6b5e53;
				}
				
			}
			@Override
			public void hoverout(RSGuiModels c) {
				if(c instanceof IPanel) {
					((IPanel)c).borderColor = 0x463d2e;
				}
				
			}
			
		};
		
			
		/**
		 * Greates a new interface with a 370x195 panel
		 */
		i = new InterfaceGUI(370,195,mc);
		i.titlebar.label = "Update your combat stats!";
		i.titlebar.x = 60;
		i.xbutton.callback = new Callback() {

			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				gui.isVisible = false;
			}
			
		};
		i.outsideclick = new OnMisClickCallBack() {

			@Override
			public void run() {
				i.isVisible = false;
			}
			
		};
		i.isVisible = false;
		
		
		this.i.addLabel("Select the skill to level  ", 10, 5, null, 1);		
		currentXP = this.i.addLabel("", 210, 5, null, 1);		
		currentXP.width = 100;
		currentCombat = this.i.addLabel("", 5, 148, null, 1);	
		currentCombat.width = 370;
		currentCombatXp = this.i.addLabel("", 5, 161, null, 1);	
		currentCombatXp.width = 370;
		currentCombatSecondaryXp = this.i.addLabel("", 5, 174, null, 1);	
		currentCombatSecondaryXp.width = 370;
		
		attack = this.i.addLabel("  Attack  ", 30, 25, new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 0;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				defense.backgroundColor = 0x463d2e;
				strength.backgroundColor = 0x463d2e;
				ranged.backgroundColor = 0x463d2e;
				prayer.backgroundColor = 0x463d2e;
				maged.backgroundColor = 0x463d2e;
				updateCurrentStats();
			}
		}, 1);
		attack.backgroundColor = 0x6b5e53;
		attack.drawBox = true;
		attack.borderColor = 0x6b5e53;
		attack.width = 100;
		
		defense = this.i.addLabel("  Defense  ", 30, 45,  new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 1;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				attack.backgroundColor = 0x463d2e;
				strength.backgroundColor = 0x463d2e;
				ranged.backgroundColor = 0x463d2e;
				prayer.backgroundColor = 0x463d2e;
				maged.backgroundColor = 0x463d2e;
				updateCurrentStats();
			}
		}, 1);
		defense.drawBox = true;
		defense.borderColor = 0x6b5e53;
		defense.width = 100;
		
		strength = this.i.addLabel("  Strength  ", 30, 65,  new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 2;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				attack.backgroundColor = 0x463d2e;
				defense.backgroundColor = 0x463d2e;
				ranged.backgroundColor = 0x463d2e;
				prayer.backgroundColor = 0x463d2e;
				maged.backgroundColor = 0x463d2e;
				updateCurrentStats();			
			}
		}, 1);
		strength.drawBox = true;
		strength.borderColor = 0x6b5e53;
		strength.width = 100;
		
		ranged = this.i.addLabel("  Ranged  ", 30, 85,  new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 4;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				attack.backgroundColor = 0x463d2e;
				defense.backgroundColor = 0x463d2e;
				strength.backgroundColor = 0x463d2e;
				prayer.backgroundColor = 0x463d2e;
				maged.backgroundColor = 0x463d2e;
				updateCurrentStats();
			}
		}, 1);
		ranged.drawBox = true;
		ranged.borderColor = 0x6b5e53;
		ranged.width = 100;
		
		prayer = this.i.addLabel("  Prayer  ", 30, 105,  new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 5;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				attack.backgroundColor = 0x463d2e;
				defense.backgroundColor = 0x463d2e;
				strength.backgroundColor = 0x463d2e;
				ranged.backgroundColor = 0x463d2e;
				maged.backgroundColor = 0x463d2e;
				updateCurrentStats();
			}
		}, 1);
		prayer.drawBox = true;
		prayer.borderColor = 0x6b5e53;
		prayer.width = 100;
		
		maged = this.i.addLabel("  Maged  ", 30, 125,  new Callback() {
			@Override
			public void run(InterfaceGUI gui, RSGuiModels id) {
				skillSelected = 6;
				ILabel x = (ILabel)id;
				x.backgroundColor = 0x6b5e53;
				attack.backgroundColor = 0x463d2e;
				defense.backgroundColor = 0x463d2e;
				strength.backgroundColor = 0x463d2e;
				ranged.backgroundColor = 0x463d2e;
				prayer.backgroundColor = 0x463d2e;
				updateCurrentStats();
			}
		}, 1);
		maged.drawBox = true;
		maged.borderColor = 0x6b5e53;
		maged.width = 100;
		
		
		for(int i = 0; i < 6; i++) {
			final int ifinal = i;
			ILabel z = this.i.addLabel("-", 185, 25+20*i, new Callback() {
				@Override
				public void run(InterfaceGUI gui, RSGuiModels id) {
					removeXP(ifinal);
				}
			}, 1);
			z.drawBox = true;
			z.borderColor = 0x6b5e53;
			z.width = 20;
			removeXp[i] = z;
		}
		for(int i = 0; i < 6; i++) {
			final int ifinal = i;
			ILabel z = this.i.addLabel("+", 315, 25+20*i,  new Callback() {
				@Override
				public void run(InterfaceGUI gui, RSGuiModels id) {
					addXp(ifinal);
				}
			}, 1);
			z.drawBox = true;
			z.borderColor = 0x6b5e53;
			z.width = 20;
			addXp[i] = z;
		}
		
		ILabel oneHundred = this.i.addLabel("100", 210, 25, null, 1);
		//oneHundred.drawBox = true;
		oneHundred.borderColor = 0x6b5e53;
		oneHundred.width = 100;
		
		ILabel oneThousand = this.i.addLabel("1,000", 210, 45, null, 1);
		//oneThousand.drawBox = true;
		oneThousand.borderColor = 0x6b5e53;
		oneThousand.width = 100;
		
		ILabel tenThousand = this.i.addLabel("10,000", 210, 65, null, 1);
		//tenThousand.drawBox = true;
		tenThousand.borderColor = 0x6b5e53;
		tenThousand.width = 100;
		
		ILabel oneHundredThousand = this.i.addLabel("100,000", 210, 85, null, 1);
		//oneHundredThousand.drawBox = true;
		oneHundredThousand.borderColor = 0x6b5e53;
		oneHundredThousand.width = 100;
		
		ILabel oneMillion = this.i.addLabel("1,000,000", 210, 105, null, 1);
		//oneMillion.drawBox = true;
		oneMillion.borderColor = 0x6b5e53;
		oneMillion.width = 100;
		
		ILabel tenMillion = this.i.addLabel("10,000,000", 210, 125, null, 1);
		//tenMillion.drawBox = true;
		tenMillion.borderColor = 0x6b5e53;
		tenMillion.width = 100;
		
		
		
		i.addPanel(10, 25-5, 50, 40).hoverCallback = panelhover;
		
		
		
		
		
		/**
		 * Adds the interface to a list
		 */
		mudclient.getInterfaceHandler().guis.add(i);
		
	}
	
	public void removeXP(int id) {
		mc.streamClass.createPacket(75);
		mc.streamClass.addByte(0);
		mc.streamClass.addByte((byte)skillSelected);
		mc.streamClass.addByte((byte)id);
		mc.streamClass.formatPacket();
	}
	public void addXp(int id) {
		mc.streamClass.createPacket(75);
		mc.streamClass.addByte(1);
		mc.streamClass.addByte((byte)skillSelected);
		mc.streamClass.addByte((byte)id);
		mc.streamClass.formatPacket();
	}
	public void parseQuestions(String[] questionMenuAnswer, int newQuestionMenuCount) {
		i.isVisible = true;
		i.setHeight(newQuestionMenuCount*20+5);
		i.clearLabels();
		
		for (int i = 0; i < newQuestionMenuCount; i++) {
			final int clickId = i;
			Callback onclose = new Callback() {

				@Override
				public void run(InterfaceGUI gui, RSGuiModels id) {
					mc.handleQuestionMenuAnswer(clickId);
				}
				
			};
			ILabel x = this.i.addLabel(questionMenuAnswer[i], 10, 5+ 20*i, onclose, 1);
			
		}
	}
	public void close() {
		this.i.isVisible = false;
		
	}
}
