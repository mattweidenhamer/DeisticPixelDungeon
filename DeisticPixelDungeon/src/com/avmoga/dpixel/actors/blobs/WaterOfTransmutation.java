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
import com.avmoga.dpixel.items.Ankh;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.Honeypot;
import com.avmoga.dpixel.items.InactiveMrDestructo;
import com.avmoga.dpixel.items.InactiveMrDestructo2;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.SteelHoneypot;
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
import com.avmoga.dpixel.items.potions.PotionOfOverHealing;
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

public class WaterOfTransmutation extends WellWater {

	@Override
	protected Item affectItem(Item item) {

		if (item instanceof MeleeWeapon) {
			item = changeWeapon((MeleeWeapon) item);
		} else if (item instanceof Scroll) {
			item = changeScroll((Scroll) item);
		} else if (item instanceof Potion) {
			item = changePotion((Potion) item);
		} else if (item instanceof Ring) {
			item = changeRing((Ring) item);
		} else if (item instanceof Wand) {
			item = changeWand((Wand) item);
		} else if (item instanceof Plant.Seed) {
			item = changeSeed((Plant.Seed) item);
		} else if (item instanceof Artifact) {
			item = changeArtifact((Artifact) item);
		} else if (item instanceof ShatteredPot) {
			item = changeHoneypot((ShatteredPot) item);
		} else if (item instanceof InactiveMrDestructo) {
			item = rechargeDestructo((InactiveMrDestructo) item);
		} else if (item instanceof ActiveMrDestructo) {
			item = upgradeDestructo((ActiveMrDestructo) item);
		} else if (item instanceof InactiveMrDestructo2) {
			item = rechargeDestructo2((InactiveMrDestructo2) item);
		} else if (item instanceof SteelShatteredPot) {
			item = changeHoneypot((SteelShatteredPot) item);
		} else if (item instanceof Honeypot) {
			item = changeHoneypot((Honeypot) item);
		} else if (item instanceof Ankh) {
			item = changeAnkh((Ankh) item);
		} else {
			item = null;
		}

		if (item != null) {
			Journal.remove(Feature.WELL_OF_TRANSMUTATION);
		}

		return item;

	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(Speck.factory(Speck.CHANGE), 0.2f, 0);
	}

	private MeleeWeapon changeWeapon(MeleeWeapon w) {

		MeleeWeapon n = null;

		if (w instanceof Knuckles) {
			n = new Dagger();
		} else if (w instanceof Dagger) {
			n = new Knuckles();
		}

		else if (w instanceof Spear) {
			n = new Quarterstaff();
		} else if (w instanceof Quarterstaff) {
			n = new Spear();
		}

		else if (w instanceof Sword) {
			n = new Mace();
		} else if (w instanceof Mace) {
			n = new Sword();
		}

		else if (w instanceof Longsword) {
			n = new BattleAxe();
		} else if (w instanceof BattleAxe) {
			n = new Longsword();
		}

		else if (w instanceof Glaive) {
			n = new WarHammer();
		} else if (w instanceof WarHammer) {
			n = new Glaive();
		}

		if (n != null) {

			int level = w.level;
			if (level > 0) {
				n.upgrade(level);
			} else if (level < 0) {
				n.degrade(-level);
			}

			n.enchantment = w.enchantment;
			n.levelKnown = w.levelKnown;
			n.cursedKnown = w.cursedKnown;
			n.cursed = w.cursed;

			return n;
		} else {
			return null;
		}
	}

	private Ring changeRing(Ring r) {
		Ring n;
		do {
			n = (Ring) Generator.random(Category.RING);
		} while (n.getClass() == r.getClass());

		n.level = 0;

		int level = r.level;
		if (level > 0) {
			n.upgrade(level);
		} else if (level < 0) {
			n.degrade(-level);
		}

		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;

		return n;
	}

	private Artifact changeArtifact(Artifact a) {
		Artifact n = Generator.randomArtifact();

		if (n != null) {
			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
		}

		return n;
	}

	private Wand changeWand(Wand w) {

		Wand n;
		do {
			n = (Wand) Generator.random(Category.WAND);
		} while (n.getClass() == w.getClass());

		n.level = 0;
		n.updateLevel();
		n.upgrade(w.level);

		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;

		return n;
	}

	private Plant.Seed changeSeed(Plant.Seed s) {

		Plant.Seed n;

		do {
			n = (Plant.Seed) Generator.random(Category.SEED2);
		} while (n.getClass() == s.getClass());

		return n;
	}

	private Scroll changeScroll(Scroll s) {
		if (s instanceof ScrollOfUpgrade) {

			return new ScrollOfMagicalInfusion();

		} else if (s instanceof ScrollOfMagicalInfusion) {

			return new ScrollOfUpgrade();

		} else {

			Scroll n;
			do {
				n = (Scroll) Generator.random(Category.SCROLL);
			} while (n.getClass() == s.getClass());
			return n;
		}
	}

	private Potion changePotion(Potion p) {
		if (p instanceof PotionOfStrength) {

			return new PotionOfMight();

		} else if (p instanceof PotionOfMight) {

			return new PotionOfStrength();
		
		} else if (p instanceof PotionOfMending){
		
			return new PotionOfHealing();

		} else {

			Potion n;
			do {
				n = (Potion) Generator.random(Category.POTION);
			} while (n.getClass() == p.getClass());
			return n;
		}
	}
	
	private Potion changeAnkh(Ankh a) { //So you can change an Ankh into a... potion of overhealing? Why? Ankhs are totally limited.
		return new PotionOfOverHealing();
	}

	private Food changeHoneypot(ShatteredPot s) {
		return new PotionOfConstitution();
	}
	
	private Item changeHoneypot(SteelShatteredPot s) {
		return new SteelHoneypot();
	}
	
	private Item changeHoneypot(Honeypot d) {
		return new SteelHoneypot();
	}
	
	private Item rechargeDestructo(InactiveMrDestructo d) {
		return new ActiveMrDestructo();
	}
	
	private Item upgradeDestructo(ActiveMrDestructo d) {
		return new ActiveMrDestructo2();
	}
	
	private Item rechargeDestructo2(InactiveMrDestructo2 d) {
		return new ActiveMrDestructo2();
	}
	
	
	
	@Override
	public String tileDesc() {
		return "Power of change radiates from the water of this well. "
				+ "Throw an item into the well to turn it into something else.";
	}
}
