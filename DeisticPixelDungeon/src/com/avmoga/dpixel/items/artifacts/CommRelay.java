package com.avmoga.dpixel.items.artifacts;

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroRace;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.actors.mobs.npcs.MirrorImage;
import com.avmoga.dpixel.items.Ankh;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.wands.WandOfBlink;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.scenes.CellSelector;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class CommRelay extends Artifact {
	{
		name = "Communications relay";
		image = ItemSpriteSheet.ARTIFACT_RADIO;
		level = 0;
		levelCap = 5;
		
		defaultAction = AC_MERC;
	}
	
	private static final String AC_SUPP = "SUPPORT PACKAGE";
	private static final String AC_MERC = "HIRE MERCENARY";
	private static final String TXT_MERC = "A mercenary teleports adjacent to you!";
	private static final String TXT_BOSS = "Strong magic aura of this place prevents the guilds from teleporting you supplies!";
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
			if (isEquipped(hero) && Dungeon.gold >= 500)
				actions.add(AC_MERC);
			if (isEquipped(hero) && Dungeon.gold >= 2000 && level == 10 && Dungeon.hero.heroRace == HeroRace.HUMAN)
				actions.add(AC_SUPP);
		}
		
		return actions;
	}
	
	protected ArtifactBuff passiveBuff() {
		return new Collection();
	}
	
	protected boolean useable(){
		if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
			return true;
		}
		return false;
	}

	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_MERC)) {
			if(!cursed){
				if (!isEquipped(hero))
					GLog.i("You need to equip this to do that.");
				else if(!useable())
					GLog.i("What are you trying to do?");
				else if (!(Dungeon.gold >= 500))//TODO adjust the gold cost based on current level.
					GLog.i("You are too poor to do that.");
				else {
					ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

					for (int offset : Level.NEIGHBOURS8) {
						int p = Dungeon.hero.pos + offset;
						if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
							respawnPoints.add(p);
						}
						int index = Random.index(respawnPoints);

						MirrorImage merc = new MirrorImage();
						merc.mercenary(level);
						GameScene.add(merc);
						WandOfBlink.appear(merc, respawnPoints.get(index));

						respawnPoints.remove(index);
						Dungeon.gold -= 500;
						GLog.i(TXT_MERC);
						break;
					}
				}
			} else {
				GLog.i("The item will not obey you!");
			}
		} else if (action.equals(AC_SUPP)) {
			if(Dungeon.bossLevel()){
				GLog.i(TXT_BOSS);
			} else{
				GameScene.selectCell(listener);
			}
		}
	}
	
	public String desc() {
		String desc = "You are surprised to find a working communications relay in the dungeon! There is an " 
				+ "instruction pamphlet included along with it, ";
		if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
			desc += "and with a little bit of cross-referencing, you are able to identify its use. "
					+ "\n\nIt appears that it is connected to a database of mercenary guilds. You "
					+ "are sure you would be able to strike a deal with them, in exchange for some coin.";
		} else {
			desc += "but it is written in a strange dialect that you are unable to decipher. "
					+ "You should probably try to transmute it into a more useful artifact.";
		}
		if(isEquipped(Dungeon.hero) && Dungeon.hero.heroRace() == HeroRace.HUMAN){
			desc += "\n\n";
			if(cursed){
				desc += "The relay has turned on in your backpack, and is emitting a loud static "
						+ "that is echoing through the dungeon.";
			} else if(level < 2){
				desc += "You have made radio contact with one of the mercenary guilds, and they say they would be happy "
						+ " to send some help for you, in exchange for some coin to operate thier matter transporter.";
			} else if (level < 10){
				desc += "You see that someone has remotely installed a big red button that says 'support package: 2500'. " 
						+ "You're sure it will cost you a decent bit, but you can't help but wonder what it does.";
			} else {
				desc += "You have familiarized yourself greatly with the guilds on the radio, they are charging you "
						+ "much lower rates for much better services.";
			}
		}
		
		return desc;
	}
	protected static CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer pos) {
			Dungeon.level.drop(Generator.random(Generator.Category.FOOD), pos).type = Heap.Type.SUPDROP;
			int loot = Random.Int(3);
			if (loot == 0) {
				Dungeon.level.drop(Generator.random(Generator.Category.RING), pos);
			} else if (loot == 1) {
				Dungeon.level.drop(Generator.random(Generator.Category.WAND), pos);
			} else {
				Dungeon.level.drop(Generator.random(Random.oneOf(Generator.Category.WEAPON,
						Generator.Category.ARMOR)), pos);
			}
			Dungeon.level.drop(new Ankh(), pos);
			Dungeon.level.drop(Generator.random(Generator.Category.POTION), pos);
			Dungeon.level.drop(Generator.random(Generator.Category.SCROLL), pos);
			Dungeon.gold -= 2500;
			Sample.INSTANCE.play(Assets.SND_TELEPORT);
			GameScene.updateMap(pos);
			Dungeon.observe();
		}

		@Override
		public String prompt() {
			return "Choose location for support materials.";
		}
	};

	
	public class Collection extends ArtifactBuff{
		public void collectGold(int gold){
			exp += gold / 8;
			checkUpgrade();
		}
		public boolean act(){
			if(isCursed()){
				for(Mob mob : Dungeon.level.mobs){
					mob.beckon(Dungeon.hero.pos);
				}
			}
			spend(TICK);
			updateQuickslot();
			return true;
		}
		public void checkUpgrade(){
			while (exp >= 1000 && level < levelCap){
				exp -= 1000;
				upgrade();
			}
		}
	}
}
