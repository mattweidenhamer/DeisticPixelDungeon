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
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.Flag;
import com.avmoga.dpixel.actors.buffs.Terror;
import com.avmoga.dpixel.items.Gold;
import com.avmoga.dpixel.items.food.Meat;
import com.avmoga.dpixel.sprites.BruteSprite;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Brute extends Mob {

	private static final String TXT_ENRAGED = "%s becomes enraged!";

	{
		name = "gnoll brute";
		spriteClass = BruteSprite.class;

		HP = HT = 50+(adj(0)*Random.NormalIntRange(4, 8));
		defenseSkill = 15+adj(0);

		EXP = 8;
		maxLvl = 15;

		loot = Gold.class;
		lootChance = 0.5f;

		lootOther = new Meat();
		lootChanceOther = 0.5f; // by default, see die()
	}

	private boolean enraged = false;

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		enraged = HP < HT / 4;
	}

	@Override
	public int damageRoll() {
		return enraged ? Random.NormalIntRange(30+adj(0), 70+adj(0)) : Random.NormalIntRange(15+adj(0), 40+adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 20+adj(1);
	}

	@Override
	protected float attackDelay() {
		return 1.2f;
	}
	@Override
	public int dr() {
		return 10+adj(0);
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);

		if (isAlive() && !enraged && HP < HT / 4) {
			enraged = true;
			spend(TICK);
			if (Dungeon.visible[pos]) {
				GLog.w(TXT_ENRAGED, name);
				sprite.showStatus(CharSprite.NEGATIVE, "enraged");
			}
		}
	}

	@Override
	public String description() {
		return "Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, "
				+ "they go berserk, inflicting even more damage to their enemies.";
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
