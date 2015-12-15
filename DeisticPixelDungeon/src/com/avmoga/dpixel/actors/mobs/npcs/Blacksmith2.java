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
import com.avmoga.dpixel.items.AdamantArmor;
import com.avmoga.dpixel.items.AdamantRing;
import com.avmoga.dpixel.items.AdamantWand;
import com.avmoga.dpixel.items.AdamantWeapon;
import com.avmoga.dpixel.items.EquipableItem;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.SanChikarah;
import com.avmoga.dpixel.items.SanChikarahDeath;
import com.avmoga.dpixel.items.SanChikarahLife;
import com.avmoga.dpixel.items.SanChikarahTranscend;
import com.avmoga.dpixel.items.armor.Armor;
import com.avmoga.dpixel.items.quest.DarkGold;
import com.avmoga.dpixel.items.quest.Pickaxe;
import com.avmoga.dpixel.items.rings.Ring;
import com.avmoga.dpixel.items.scrolls.ScrollOfUpgrade;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.items.weapon.melee.MeleeWeapon;
import com.avmoga.dpixel.items.weapon.missiles.Boomerang;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.levels.Room.Type;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.BlacksmithSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndBlacksmith;
import com.avmoga.dpixel.windows.WndBlacksmith2;
import com.avmoga.dpixel.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blacksmith2 extends NPC {


	private static final String TXT_LOOKS_BETTER = "your %s pulsates with magical energy. ";
	private static final String TXT_GET_LOST = "I'm busy. Get lost!";
	private static final String TXT2 = "My brother and I make all the items in this dungeon. "
			                          +"He melts down two upgraded items to enhance one of them. "
			                          +"My specialty is reinforcing items with adamantite. "
			                          +"Come back to me when you have 50 dark gold and some adamantite for me to work with. " ;
	
	private static final String TXT3 = "Oh ho! Looks like you have some adamantite there. "
                                     +"I can reinforce an item with adamantite if you wish. "
                                     +"Reinforced items can handle higher levels of magical upgrade. "
                                     +"It'll cost you though!. "
                                     +"Come back to me when you have 50 dark gold. " ;
	
	

	{
		name = "Troll Blacksmith named Bop";
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
		this.name = BlacksmithName.getName(Dungeon.names);
		sprite.turnTo(pos, Dungeon.hero.pos);
		
		
		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (!checkAdamant()) {
			tell(TXT2);
		} else if (gold == null || gold.quantity() < 50) {
			tell(TXT3);
		} else if (checkAdamant() && gold != null && gold.quantity() > 49){
		GameScene.show(new WndBlacksmith2(this, Dungeon.hero));
		} else {
			tell(TXT2);
		}
		
	}

	public static String verify(Item item1, Item item2) {
	
		if (item1 == item2) {
			return "Select 2 different items, not the same item twice!";
		}

		if (!item1.isIdentified()) {
			return "I need to know what I'm working with, identify first!";
		}

		if (item1.cursed) {
			return "I don't work with cursed items!";
		}
		
		if (item1.reinforced) {
			return "This is already as strong as it gets!";
		}

		if (item1.level < 0) {
			return "This is junk, the quality is too poor!";
		}

		if (!item1.isUpgradable()) {
			return "I can't reforge these items!";
		}
		
		if(item1 instanceof Armor && item2 instanceof AdamantArmor){
			return null;			
		}
		
		if(item1 instanceof MeleeWeapon && item2 instanceof AdamantWeapon){
			return null;
		}
		
		if(item1 instanceof Boomerang && item2 instanceof AdamantWeapon){
			return null;
		}
		
		if(item1 instanceof Wand && item2 instanceof AdamantWand){
			return null;
		}
		
		if(item1 instanceof Ring && item2 instanceof AdamantRing){
			return null;
		}
		
		return "This won't work. Pick and item and a matching adamantite item. ";
		
	}
	
	public static void upgrade(Item item1, Item item2) {
		
		item1.reinforced=true;
		item2.detach(Dungeon.hero.belongings.backpack);
		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (gold == null || gold.quantity() > 49) {
			gold.detach(Dungeon.hero.belongings.backpack,50);
			if(!(Dungeon.hero.belongings.getItem(DarkGold.class).quantity() > 0)){
				gold.detachAll(Dungeon.hero.belongings.backpack);
			}
		}
		
		GLog.p(TXT_LOOKS_BETTER, item1.name());
		Dungeon.hero.spendAndNext(2f);
		Badges.validateItemLevelAquired(item1);
		
	}
	
	
	private void tell(String text) {
		GameScene.show(new WndQuest(this, text));		
	}

	
	public static boolean checkAdamant() {
		AdamantArmor armor1 = Dungeon.hero.belongings.getItem(AdamantArmor.class);
		AdamantWeapon weapon1 = Dungeon.hero.belongings.getItem(AdamantWeapon.class);
		AdamantRing ring1 = Dungeon.hero.belongings.getItem(AdamantRing.class);
		AdamantWand wand1 = Dungeon.hero.belongings.getItem(AdamantWand.class);
		
		if(armor1!=null ||  weapon1!=null || ring1!=null || wand1!=null) {
			return true;
		}
		   return false;		
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

	
}
