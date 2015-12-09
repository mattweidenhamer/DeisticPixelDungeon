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
package com.avmoga.dpixel.actors.blobs;

import com.avmoga.dpixel.Journal;
import com.avmoga.dpixel.Journal.Feature;
import com.avmoga.dpixel.effects.BlobEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.ActiveMrDestructo;
import com.avmoga.dpixel.items.ActiveMrDestructo2;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.Honeypot;
import com.avmoga.dpixel.items.InactiveMrDestructo;
import com.avmoga.dpixel.items.InactiveMrDestructo2;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.SteelHoneypot;
import com.avmoga.dpixel.items.Stylus;
import com.avmoga.dpixel.items.UpgradeBlobRed;
import com.avmoga.dpixel.items.UpgradeBlobViolet;
import com.avmoga.dpixel.items.UpgradeBlobYellow;
import com.avmoga.dpixel.items.Generator.Category;
import com.avmoga.dpixel.items.Honeypot.ShatteredPot;
import com.avmoga.dpixel.items.SteelHoneypot.SteelShatteredPot;
import com.avmoga.dpixel.items.artifacts.Artifact;
import com.avmoga.dpixel.items.food.Food;
import com.avmoga.dpixel.items.food.PotionOfConstitution;
import com.avmoga.dpixel.items.potions.Potion;
import com.avmoga.dpixel.items.potions.PotionOfHealing;
import com.avmoga.dpixel.items.potions.PotionOfMending;
import com.avmoga.dpixel.items.potions.PotionOfMight;
import com.avmoga.dpixel.items.potions.PotionOfStrength;
import com.avmoga.dpixel.items.rings.Ring;
import com.avmoga.dpixel.items.scrolls.Scroll;
import com.avmoga.dpixel.items.scrolls.ScrollOfMagicalInfusion;
import com.avmoga.dpixel.items.scrolls.ScrollOfUpgrade;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.items.weapon.melee.BattleAxe;
import com.avmoga.dpixel.items.weapon.melee.Dagger;
import com.avmoga.dpixel.items.weapon.melee.Glaive;
import com.avmoga.dpixel.items.weapon.melee.Knuckles;
import com.avmoga.dpixel.items.weapon.melee.Longsword;
import com.avmoga.dpixel.items.weapon.melee.Mace;
import com.avmoga.dpixel.items.weapon.melee.MeleeWeapon;
import com.avmoga.dpixel.items.weapon.melee.Quarterstaff;
import com.avmoga.dpixel.items.weapon.melee.Spear;
import com.avmoga.dpixel.items.weapon.melee.Sword;
import com.avmoga.dpixel.items.weapon.melee.WarHammer;
import com.avmoga.dpixel.plants.Plant;
import com.watabou.utils.Random;

public class WaterOfUpgradeEating extends WellWater {

	@Override
	protected Item affectItem(Item item) {

		if (item.isUpgradable()) {
			item = eatUpgradable((Item) item);
		} else if (item instanceof Scroll
				    || item instanceof Potion
				    || item instanceof Stylus) {
			item = eatStandard((Item) item);
		} else {
			item = null;
		}
		
		return item;

	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(Speck.factory(Speck.CHANGE), 0.2f, 0);
	}

	private Item eatUpgradable(Item w) {

		int ups = w.level;
		
		Item n = null;

		if (Random.Float()<(ups/10)){
			
			n = new UpgradeBlobViolet();
			
		} else if (Random.Float()<(ups/5)) {
			
			n =  new UpgradeBlobRed();
			
        } else if (Random.Float()<(ups/3)) {
			
			n =  new UpgradeBlobYellow();
		
		} else {
			
			n = (Plant.Seed) Generator.random(Category.SEEDRICH);
		}
		
		return n;
	}
	
	private Item eatStandard(Item w) {

		Item n = null;
        
		if (Random.Float()<0.1f){
			n = new UpgradeBlobYellow();
		} else {
			n = (Plant.Seed) Generator.random(Category.SEEDRICH);
		}
		
		return n;
	}
	
	@Override
	public String tileDesc() {
		return "A highly caustic liquid shimmers in a pool. Toss in an item to harvest its power. ";
	}
}
