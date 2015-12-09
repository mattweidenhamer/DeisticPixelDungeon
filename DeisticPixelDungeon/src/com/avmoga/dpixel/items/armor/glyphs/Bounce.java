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
package com.avmoga.dpixel.items.armor.glyphs;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.Pushing;
import com.avmoga.dpixel.items.armor.Armor;
import com.avmoga.dpixel.items.armor.Armor.Glyph;
import com.avmoga.dpixel.levels.Level;
import com.watabou.utils.Random;

public class Bounce extends Glyph {

	private static final String TXT_BOUNCE = "%s of bounce";

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Level.adjacent(attacker.pos, defender.pos)
				&& Random.Int(level + 5) >= 4) {

			for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
				int ofs = Level.NEIGHBOURS8[i];
				if (attacker.pos - defender.pos == ofs) {
					int newPos = attacker.pos + ofs;
					if ((Level.passable[newPos] || Level.avoid[newPos])
							&& Actor.findChar(newPos) == null) {

						Actor.addDelayed(new Pushing(attacker, attacker.pos,
								newPos), -1);

						attacker.pos = newPos;
						Dungeon.level.mobPress((Mob) attacker);
						}

					}
					break;
				}

			}

			return damage;
		}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_BOUNCE, weaponName);
	}

}
