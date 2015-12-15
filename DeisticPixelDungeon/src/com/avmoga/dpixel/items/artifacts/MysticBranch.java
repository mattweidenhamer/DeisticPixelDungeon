package com.avmoga.dpixel.items.artifacts;

import java.util.ArrayList;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroRace;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.actors.mobs.Wraith;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.artifacts.Artifact.ArtifactBuff;
import com.avmoga.dpixel.items.artifacts.WraithAmulet.WraithRecharge;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.plants.BombFruit;
import com.avmoga.dpixel.scenes.CellSelector;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.ui.QuickSlotButton;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndOptions;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MysticBranch extends Artifact {
	public static final String TXT_YOURSELF = "You can't target yourself.";
	{
		name = "Wooden Effigy";
		image = ItemSpriteSheet.ARTIFACT_ROOT;
		
		level = 0;
		levelCap = 4;
		
		exp = 0;
		charge = 0;
	}
	private static final String TXT_MONSTER = "There is a monster there already.";
	private static final String UPGRADETXT = "Your effigy grows in power!";
	private static final String AC_PLANT = "SEED";
	
	protected static CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target != null && Actor.findChar(target) == null && Level.flamable[target]) {
					Dungeon.level.plant(new BombFruit.Seed(), target);
					return;
			} else if (target != null) {
				GLog.i(TXT_MONSTER);
			}
		}

		@Override
		public String prompt() {
			return "Choose direction to zap";
		}
	};
	
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge == 0 && !cursed && level >= 2 && Dungeon.hero.heroRace() == HeroRace.GNOLL)
			actions.add(AC_PLANT);
		return actions;
	}
	
	
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_PLANT) && Dungeon.hero.heroRace == HeroRace.GNOLL && charge == 0) {
			GameScene.selectCell(listener);
		}
	}
	
	protected ArtifactBuff passiveBuff() {
			return new Affinity();
	}
	
	public class Affinity extends ArtifactBuff {
		public boolean act(){
			if(charge > 0){
				charge--;
			}
			if(cursed){
					if(Random.Int(30)==0){
						new Wraith().pos = Dungeon.level.randomRespawnCell();
						GLog.w("A spirit has been dispatched to avenge the dungeon.");
					}
			}
			updateQuickslot();
			spend(TICK);
			return true;
		}
		public String toString() {
			return "Affinity";
		}
		public void gainEXP(){
			exp += 1;
			if(exp > ((level + 1) * 100)){
				exp = 0;
				if(level < levelCap){
					upgrade();
				}
			}
		}
	}

public String desc() {
	String desc = "This is quite the odd find: A wooden face shaped in an almost clown-like manner. You notice that "
			+ "there is a pure energy flowing from it.\n\n";
	if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
		desc += "You hear a voice whisper to you from the effigy, telling you of the whereabouts of as many characters on this "
				+ "floor as possible. The voice doesn't notice those creatures on the ";
	} else if(Dungeon.hero.heroRace() == HeroRace.GNOLL){
		desc += "You recognize what you have found as a Spirit Head, enabling you to talk to the "
				+ "natural spirits here. While their domain is somewhat limited, they might be able "
				+ "to offer you information, and, later, some offensive power. \n\n";
				if(level < 2){
					 desc += "Maybe you should try to connect to more spirits by travelling through grassy areas. "
					 		+ "That might increase the power of your effigy.";
				}
	}
	else{
		desc += "A disembodied voice starts yelling at you from the effigy; you are so startled that you throw it "
				+ "against the wall! You should probably transmute this into something more useful for you.";
	}
	
	if(isEquipped(Dungeon.hero) && (Dungeon.hero.heroRace == HeroRace.GNOLL || Dungeon.hero.heroRace == HeroRace.HUMAN || cursed)){
		desc += "\n\n";
		if(cursed){
			desc += "The voice cackles in your ear incessantly, it is summoning evil spirits to haunt you!";
		} else if(level < 5 || Dungeon.hero.heroRace() == HeroRace.HUMAN){
			desc += "In your hand, the voice tells you of the locations of the spirits in the area. You recognize that "
					+ "not all of them are dead.";
		} else if (level < 10 && Dungeon.hero.heroRace() == HeroRace.GNOLL){
			desc += "It looks like you could contact the spirits to help you out in combat if needed.";
		} else {
			desc += "You have mastered the totem quite well, it is very adaptive to your will "
					+ "and you have learned to exercise control of it very well.";
		}
	}
	
	return desc;
}
	/* Wooden Effigy
	 * A charge-based ranged artillery mechanism used best by a Gnoll.
	 * Upgrades by trampling grass
	 * Basic: See all mobs that are standing on grass.
	 * Advanced: Spawn a seed bomb at a given (grassed) location. The bomb explodes after 
	 * two turns, spreads grass seeds, and damages mobs.
	 */
}
