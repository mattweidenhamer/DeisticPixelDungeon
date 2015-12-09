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
package com.avmoga.dpixel.items.food;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.buffs.Blindness;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Hunger;
import com.avmoga.dpixel.actors.buffs.Poison;
import com.avmoga.dpixel.actors.buffs.Slow;
import com.avmoga.dpixel.actors.buffs.Vertigo;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Random;

public class Meat extends Food {
	//Oh, I see how it is, Dachhack. Regular mystery meat isn't good enough for you?
	{
		name = "monster meat";
		image = ItemSpriteSheet.MEAT;
		energy = Hunger.STARVING - Hunger.HUNGRY;
		message = "You managed to eat most of the meat.";
		hornValue = 1;
		bones = false;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(15)) {
			case 0:
				GLog.w("You start feeling nauseous.");
				Buff.prolong(hero, Vertigo.class, Vertigo.duration(hero));
				break;
			case 1:
				GLog.w("Everything goes dark!");
				Buff.prolong(hero, Blindness.class, Random.Int(10, 12));
				Dungeon.observe();
				break;
			case 2:
				GLog.w("You are not feeling well.");
				Buff.affect(hero, Poison.class).set(
						Poison.durationFactor(hero) * hero.HT / 10);
				break;
			case 3:
				GLog.w("You can hardly move!");
				Buff.prolong(hero, Slow.class, Slow.duration(hero));
				break;
			}
		}
	}	
	
	@Override
	public String info() {
		return "Fresh remains of a defeated foe.";
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
		
}
