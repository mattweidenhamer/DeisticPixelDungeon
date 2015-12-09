/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.avmoga.dpixel.actors.mobs.npcs;

import java.util.Collection;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.Journal;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.items.EquipableItem;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.SanChikarah;
import com.avmoga.dpixel.items.SanChikarahDeath;
import com.avmoga.dpixel.items.SanChikarahLife;
import com.avmoga.dpixel.items.SanChikarahTranscend;
import com.avmoga.dpixel.items.quest.DarkGold;
import com.avmoga.dpixel.items.quest.Pickaxe;
import com.avmoga.dpixel.items.scrolls.ScrollOfUpgrade;
import com.avmoga.dpixel.items.weapon.melee.Chainsaw;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.levels.Room.Type;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.BlacksmithSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndBlacksmith;
import com.avmoga.dpixel.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blacksmith extends NPC {

	private static final String TXT_GOLD_1 = "Hey human! Wanna be useful, eh? Take dis pickaxe and mine me some _dark gold ore_, _15 pieces_ should be enough. "
			+ "What do you mean, how am I gonna pay? You greedy...\n"
			+ "Ok, ok, I don't have money to pay, but I can do some smithin' for you. Consider yourself lucky, "
			+ "We're the only blacksmiths around.";
	private static final String TXT_BLOOD_1 = "Hey human! Wanna be useful, eh? Take dis pickaxe and _kill a bat_ wit' it, I need its blood on the head. "
			+ "What do you mean, how am I gonna pay? You greedy...\n"
			+ "Ok, ok, I don't have money to pay, but I can do some smithin' for you. Consider yourself lucky, "
			+ "We're the only blacksmiths around.";
	private static final String TXT2 = "Are you kiddin' me? Where is my pickaxe?!";
	private static final String TXT3 = "Dark gold ore. 15 pieces. Seriously, is it dat hard?";
	private static final String TXT4 = "I said I need bat blood on the pickaxe. Chop chop!";
	private static final String TXT_COMPLETED = "Oh, you have returned... Better late dan never.";
	private static final String TXT_GET_LOST = "I'm busy. Get lost!";
	private static final String AYYLMAO = "namenumber";
	private static final String TXT_LOOKS_BETTER = "your %s certainly looks better now";
	private static final String COLLECTED = "Finally, the SanChikarah. I will forge them for you...";

	{
		name = BlacksmithName.getName(Dungeon.names);
		spriteClass = BlacksmithSprite.class;
	}
	

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public void interact() {
		Dungeon.names++;
		sprite.turnTo(pos, Dungeon.hero.pos);
		
		if (checksan()){
			            tell(COLLECTED);	
			            SanChikarah san = new SanChikarah();
			               Dungeon.sanchikarah = true;	
							if (san.doPickUp(Dungeon.hero)) {
								GLog.i(Hero.TXT_YOU_NOW_HAVE, san.name());
							} else {
								Dungeon.level.drop(san, Dungeon.hero.pos).sprite.drop();
							}
			          }

		if (!Quest.given) {

			GameScene.show(new WndQuest(this, Quest.alternative ? TXT_BLOOD_1
					: TXT_GOLD_1) {

				@Override
				public void onBackPressed() {
					super.onBackPressed();

					Quest.given = true;
					Quest.completed = false;

					Pickaxe pick = new Pickaxe();
					if (pick.doPickUp(Dungeon.hero)) {
						GLog.i(Hero.TXT_YOU_NOW_HAVE, pick.name());
					} else {
						Dungeon.level.drop(pick, Dungeon.hero.pos).sprite
								.drop();
					}
				};
			});

			Journal.add(Journal.Feature.TROLL);

		} else if (!Quest.completed) {
			if (Quest.alternative) {

				Pickaxe pick = Dungeon.hero.belongings.getItem(Pickaxe.class);
				if (pick == null) {
					tell(TXT2);
				} else if (!pick.bloodStained) {
					tell(TXT4);
				} else {
					//if (pick.isEquipped(Dungeon.hero)) {
					//	pick.doUnequip(Dungeon.hero, false);
					//}
					//pick.detach(Dungeon.hero.belongings.backpack);
					tell(TXT_COMPLETED);

					Quest.completed = true;
					Quest.reforged = false;
				}

			} else {

				Pickaxe pick = Dungeon.hero.belongings.getItem(Pickaxe.class);
				DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
				if (pick == null) {
					tell(TXT2);
				} else if (gold == null || gold.quantity() < 15) {
					tell(TXT3);
				} else {
					//if (pick.isEquipped(Dungeon.hero)) {
					//	pick.doUnequip(Dungeon.hero, false);
					//}
					//pick.detach(Dungeon.hero.belongings.backpack);
					yell("Keep the pickaxe. It's good for breaking stones in your way.");
					tell(TXT_COMPLETED);

					Quest.completed = true;
					Quest.reforged = false;
				}

			}
		} else if (!Quest.reforged) {

			GameScene.show(new WndBlacksmith(this, Dungeon.hero));

		} else {
			tell(TXT_GET_LOST);
		}
	}

	private void tell(String text) {
		GameScene.show(new WndQuest(this, text));
	}

	public static String verify(Item item1, Item item2) {

		if (item1 == item2) {
			return "Select 2 different items, not the same item twice!";
		}

		//if (item1.getClass() != item2.getClass()) {
		//	return "Select 2 items of the same type!";
		//}

		if (!item1.isIdentified() || !item2.isIdentified()) {
			return "I need to know what I'm working with, identify them first!";
		}

		if (item1.cursed || item2.cursed) {
			return "I don't work with cursed items!";
		}

		if (item1.level < 0 || item2.level < 1) {
			return "It's a junk, the quality is too poor!";
		}
		
		if ((item1.level + item2.level > 15) && !item1.isReinforced()) {
			return "You item needs to be reinforced to handle the upgrades. Take it to my brother!";
		}

		if (!item1.isUpgradable() || !item2.isUpgradable()) {
			return "I can't reforge these items!";
		}

		return null;
	}

	private static float upgradeChance = 0.5f;
	public static void upgrade(Item item1, Item item2) {

		Item first, second;
		
			first = item1;
			second = item2;
		

		Sample.INSTANCE.play(Assets.SND_EVOKE);
		ScrollOfUpgrade.upgrade(Dungeon.hero);
		Item.evoke(Dungeon.hero);

		if (first.isEquipped(Dungeon.hero)) {
			((EquipableItem) first).doUnequip(Dungeon.hero, true);
		}
		
		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (gold!=null){
		upgradeChance = (upgradeChance + (gold.quantity()*0.05f));
		}
		if (first != null) {
                for(int i=0; i<second.level; i++){
				if (i<2){
				  Sample.INSTANCE.play(Assets.SND_EVOKE);
				  first.upgrade();
				} else if (Random.Float()<upgradeChance){
				  first.upgrade();
			      upgradeChance = Math.max(0.5f, upgradeChance-0.1f);
			    }
			}
		}
		
		GLog.p(TXT_LOOKS_BETTER, first.name());
		Dungeon.hero.spendAndNext(2f);
		Badges.validateItemLevelAquired(first);

		if (second.isEquipped(Dungeon.hero)) {
			((EquipableItem) second).doUnequip(Dungeon.hero, false);
		}
		second.detachAll(Dungeon.hero.belongings.backpack);
		if (gold!=null){gold.detachAll(Dungeon.hero.belongings.backpack);}
		Quest.reforged = true;

		Journal.remove(Journal.Feature.TROLL);
	}
	
	public static boolean checksan() {
		SanChikarahDeath san1 = Dungeon.hero.belongings.getItem(SanChikarahDeath.class);
		SanChikarahLife san2 = Dungeon.hero.belongings.getItem(SanChikarahLife.class);
		SanChikarahTranscend san3 = Dungeon.hero.belongings.getItem(SanChikarahTranscend.class);
		
		if (san1!=null && san2!=null && san3!=null){
			san1.detach(Dungeon.hero.belongings.backpack);
			san2.detach(Dungeon.hero.belongings.backpack);
			san3.detach(Dungeon.hero.belongings.backpack);
			return true;			
		} else {
			return false;
		}
		
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public String description() {
		return "This troll blacksmith looks like all trolls look: he is tall and lean, and his skin resembles stone "
				+ "in both color and texture. The troll blacksmith is tinkering with unproportionally small tools.";
	}

	public static class Quest {

		private static boolean spawned;

		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;

		public static void reset() {
			spawned = false;
			given = false;
			completed = false;
			reforged = false;
		}

		private static final String NODE = "blacksmith";

		private static final String SPAWNED = "spawned";
		private static final String ALTERNATIVE = "alternative";
		private static final String GIVEN = "given";
		private static final String COMPLETED = "completed";
		private static final String REFORGED = "reforged";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {
				node.put(ALTERNATIVE, alternative);
				node.put(GIVEN, given);
				node.put(COMPLETED, completed);
				node.put(REFORGED, reforged);
				node.put(AYYLMAO, Dungeon.names);//I couldn't find the bundle in the Dungeon, this was a good alternative. Don't judge.
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
				alternative = node.getBoolean(ALTERNATIVE);
				given = node.getBoolean(GIVEN);
				completed = node.getBoolean(COMPLETED);
				reforged = node.getBoolean(REFORGED);
				Dungeon.names = node.getInt(AYYLMAO);
			} else {
				reset();
			}
		}

		public static boolean spawn(Collection<Room> rooms) {
			//if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth ) == 0) {
			if (!spawned ) {
				    
				Room blacksmith = null;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 4
							&& r.height() > 4) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;

						spawned = true;
						
						Chainsaw saw = Dungeon.hero.belongings.getItem(Chainsaw.class);
						if (saw==null){
						   alternative = Random.Int(2) == 0;
						} else {
							alternative = false;
						}
						given = false;

						break;
					}
				}
			}
			return spawned;
		}
	}
}
