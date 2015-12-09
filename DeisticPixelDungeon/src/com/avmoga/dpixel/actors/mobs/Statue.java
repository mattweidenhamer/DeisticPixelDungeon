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
package com.avmoga.dpixel.actors.mobs;

import java.util.HashSet;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.Journal;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Poison;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.weapon.Weapon;
import com.avmoga.dpixel.items.weapon.Weapon.Enchantment;
import com.avmoga.dpixel.items.weapon.enchantments.Death;
import com.avmoga.dpixel.items.weapon.enchantments.Leech;
import com.avmoga.dpixel.items.weapon.melee.MeleeWeapon;
import com.avmoga.dpixel.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Statue extends Mob {

	{
		name = "animated statue";
		spriteClass = StatueSprite.class;

		EXP = 5 + Dungeon.depth * 2;
		state = PASSIVE;
	}	@Override
	public void notice() {
		super.notice();
		if(!seenHero){
			this.yell(quotes[Random.Int(quotes.length - 1)]);
			this.seenHero = true;
		}
	}
	private boolean seenHero = false;
	
	private String[] quotes = {"Oh God, not you again.", "Hero! Come and fight me on equal ground, so that we may settle this dispute once and for all!",
			"Fianlly! You have no idea how long I've been stuck in this damn blood spot.", "Please, come attack me. I have an itch.",
			"I... AM ALIIIIIVVVVEEEEEE!", "Sacre bleu!", "Strike me down, and you will become more powerful than you could every imagine... variably.",
			"OTHERS SAY MY TACTICS ARE TOO BRUTAL AND VIOLENT. I SAY, \"IS THAT EVEN POSSIBLE?\"", "*incoherent mumbling*", 
			"Do not take your life too seriously. You will never get out of it alive.", "I may be drunk, but in the morning, I will be sober, and you will still be ugly!",
			"A dungeon without sunshine is like a night.", "I am a fake statue who had a fake wife who ran away because he couldn't fake keep a fake job.", "My animated plant statues died because I forgot to animated water statue them.",
			"Only the Gods may judge you, Hero. I am just here to arrange your meeting.", "Too much agreement kills a chat, y'know.", "Hero, in my 1000 years locked in this dungeon, I have learned to resist everything... except temptation,",
			"WHAT YEAR IS IT?", "TEN THOUSAND YEARS will give you such a crick in the neck.", "Prejudice about Heroes is a great time saver. I could form opinions about you for 1000 years without having any actual facts.", 
			"Hero, you need a haircut. You look like a Firebloom.", "Reality continues to ruin my life.", "My life needs editing.", "All the other Animated Statues in this dungeon are such crybabies.",
			"I swear, all I have is a blazing dagger. Please don't hurt me.", "Your Grim Warhammer +3 is in another castle.", "99 hero's skulls on the wall... 99 hero's skulls...", "Hello from the inside."};

	private Weapon weapon;

	public Statue() {
		super();

		do {
			weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
		} while (!(weapon instanceof MeleeWeapon) || weapon.level < 0);

		weapon.identify();
		weapon.enchant(Enchantment.random());

		HP = HT = 15 + Dungeon.depth * 5;
		defenseSkill = 4 + Dungeon.depth * 2;
	}

	private static final String WEAPON = "weapon";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		weapon = (Weapon) bundle.get(WEAPON);
	}

	@Override
	protected boolean act() {
		if (Dungeon.visible[pos]) {
			Journal.add(Journal.Feature.STATUE);
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(weapon.MIN, weapon.MAX);
	}

	@Override
	public int attackSkill(Char target) {
		return (int) ((9 + Dungeon.depth) * weapon.ACU);
	}

	@Override
	protected float attackDelay() {
		return weapon.DLY;
	}

	@Override
	public int dr() {
		return Dungeon.depth;
	}

	@Override
	public void damage(int dmg, Object src) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage(dmg, src);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		weapon.proc(this, enemy, damage);
		return damage;
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void die(Object cause) {
		Dungeon.level.drop(weapon, pos).sprite.drop();
		super.die(cause);
	}

	@Override
	public void destroy() {
		Journal.remove(Journal.Feature.STATUE);
		super.destroy();
	}

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		String desc = "You would think that it's just another one of this dungeon's ugly statues, but its red glowing eyes give it away."
				+ "\n\n";
		
		if(this.state == PASSIVE){
			desc += "You curse silently; his weapon is sheathed. If you want to know what he has, you'll have to attack!";
		} else {
			desc +="Now that it is moving, you can tell that the _"+ weapon.name() + "_, it's wielding, looks real.";
		}
		return desc;
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(Death.class);
		IMMUNITIES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
