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
package com.avmoga.dpixel.actors.hero;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Challenges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ShatteredPixelDungeon;
import com.avmoga.dpixel.items.Bomb;
import com.avmoga.dpixel.items.MegaKey;
import com.avmoga.dpixel.items.ReturnBeacon;
import com.avmoga.dpixel.items.SewersKey;
import com.avmoga.dpixel.items.TomeOfMastery;
import com.avmoga.dpixel.items.TomeOfSpecialty;
import com.avmoga.dpixel.items.armor.Armor;
import com.avmoga.dpixel.items.armor.ClothArmor;
import com.avmoga.dpixel.items.armor.glyphs.Viscosity;
import com.avmoga.dpixel.items.artifacts.CloakOfShadows;
import com.avmoga.dpixel.items.artifacts.CommRelay;
import com.avmoga.dpixel.items.artifacts.Rapper;
import com.avmoga.dpixel.items.bags.ArtifactBox;
import com.avmoga.dpixel.items.bags.KeyRing;
import com.avmoga.dpixel.items.food.Food;
import com.avmoga.dpixel.items.potions.PotionOfMindVision;
import com.avmoga.dpixel.items.potions.PotionOfStrength;
import com.avmoga.dpixel.items.scrolls.ScrollOfIdentify;
import com.avmoga.dpixel.items.scrolls.ScrollOfMagicMapping;
import com.avmoga.dpixel.items.scrolls.ScrollOfPsionicBlast;
import com.avmoga.dpixel.items.wands.WandOfFirebolt;
import com.avmoga.dpixel.items.wands.WandOfMagicMissile;
import com.avmoga.dpixel.items.weapon.melee.Dagger;
import com.avmoga.dpixel.items.weapon.melee.Knuckles;
import com.avmoga.dpixel.items.weapon.melee.ShortSword;
import com.avmoga.dpixel.items.weapon.missiles.Boomerang;
import com.avmoga.dpixel.items.weapon.missiles.CurareDart;
import com.avmoga.dpixel.items.weapon.missiles.Dart;
import com.avmoga.dpixel.items.weapon.missiles.ForestDart;
import com.avmoga.dpixel.items.weapon.missiles.Shuriken;
import com.avmoga.dpixel.items.weapon.missiles.Tamahawk;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR("warrior"), MAGE("mage"), ROGUE("rogue"), HUNTRESS("huntress");

	private String title;

	private HeroClass(String title) {
		this.title = title;
	}

	public static final String[] WAR_PERKS = {
			"Warriors start with 11 points of Strength.",
			"Warriors start with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
			"Warriors are less proficient with missile weapons.",
			"Any piece of food restores more health when eaten.",
			"Potions of Strength are identified from the beginning.", };

	public static final String[] MAG_PERKS = {
			"Mages start with a unique Wand of Magic Missile. This wand can be later \"disenchanted\" to upgrade another wand.",
			"Mages recharge their wands faster.",
			"When eaten, any piece of food restores 1 charge for all wands in the inventory.",
			"Mages can use wands as a melee weapon.",
			"Dew blessings are more effective for mages.",
			"Scrolls of Identify are identified from the beginning." };

	public static final String[] ROG_PERKS = {
			"Rogues start with a unique Cloak of Shadows.",
			"Rogues identify a type of a ring on equipping it.",
			"Rogues are proficient with light armor, dodging better while wearing one.",
			"Rogues are proficient in detecting hidden doors and traps.",
			"Rogues can go without food longer.",
			"Scrolls of Magic Mapping are identified from the beginning." };

	public static final String[] HUN_PERKS = {
			"Huntresses start with 15 points of Health.",
			"Huntresses start with a unique upgradeable boomerang.",
			"Huntresses are proficient with missile weapons, getting bonus damage from excess strength.",
			"Huntresses are able to recover a single used missile weapon from each enemy.",
			"Huntresses gain more health from dewdrops.",
			"Huntresses sense neighbouring monsters even if they are hidden behind obstacles.",
			"Potions of Mind Vision are identified from the beginning." };

	public void initHero(Hero hero) {

		hero.heroClass = this;

		initCommon(hero);

		switch (this) {
		case WARRIOR:
			initWarrior(hero);
			break;

		case MAGE:
			initMage(hero);
			break;

		case ROGUE:
			initRogue(hero);
			break;

		case HUNTRESS:
			initHuntress(hero);
			break;
		}

		if (Badges.isUnlocked(masteryBadge())) {
			new TomeOfMastery().collect();
		}

		hero.updateAwareness();
	}

	private static void initCommon(Hero hero) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
			(hero.belongings.armor = new ClothArmor()).identify();

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			new Food().identify().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
		case MAGE:
			return Badges.Badge.MASTERY_MAGE;
		case ROGUE:
			return Badges.Badge.MASTERY_ROGUE;
		case HUNTRESS:
			return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior(Hero hero) {
		hero.STR = hero.STR + 1;
		

		(hero.belongings.weapon = new ShortSword()).identify();
		Dart darts = new Dart(8);
		darts.identify().collect();
		    	
		Dungeon.quickslot.setSlot(0, darts);
		
		KeyRing keyring = new KeyRing(); keyring.collect();

		new PotionOfStrength().setKnown();
		
		//playtest(hero);
	}

	private static void initMage(Hero hero) {
		(hero.belongings.weapon = new Knuckles()).identify();

		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();
		
		KeyRing keyring = new KeyRing(); keyring.collect();
		
		Dungeon.quickslot.setSlot(0, wand);

		new ScrollOfIdentify().setKnown();
		
		//playtest(hero);
	}

	private static void initRogue(Hero hero) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate(hero);

		Dart darts = new Dart(10);
		darts.identify().collect();
		
		KeyRing keyring = new KeyRing(); keyring.collect();

		Dungeon.quickslot.setSlot(0, cloak);
		if (ShatteredPixelDungeon.quickSlots() > 1)
			Dungeon.quickslot.setSlot(1, darts);
		
		Bomb bomb = new Bomb(); bomb.collect();
		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress(Hero hero) {

		hero.HP = (hero.HT -= 5);

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();
		
		KeyRing keyring = new KeyRing(); keyring.collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}

	public void playtest(Hero hero) {
		if (!Dungeon.playtest){//TODO: Use me to playtest from now on.
		//Playtest
		//TomeOfMastery tome = new TomeOfMastery(); tome.collect();
				
				hero.HT=hero.HP=999;
				hero.STR = hero.STR + 20;

				new SewersKey().collect();
				new MegaKey().collect();
				for(int i = 0; i < 4; i++){
					new ScrollOfMagicMapping().identify().collect();
				}
		}
	}
	
	public String title() {
		return title;
	}

	public String spritesheet() {

		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}

		return null;
	}

	public String[] perks() {

		switch (this) {
		case WARRIOR:
			return WAR_PERKS;
		case MAGE:
			return MAG_PERKS;
		case ROGUE:
			return ROG_PERKS;
		case HUNTRESS:
			return HUN_PERKS;
		}

		return null;
	}

	private static final String CLASS = "class";

	public void storeInBundle(Bundle bundle) {
		bundle.put(CLASS, toString());
	}

	public static HeroClass restoreInBundle(Bundle bundle) {
		String value = bundle.getString(CLASS);
		return value.length() > 0 ? valueOf(value) : ROGUE;
	}
}
