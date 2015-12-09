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

import com.avmoga.dpixel.actors.buffs.BerryRegeneration;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Haste;
import com.avmoga.dpixel.actors.buffs.Hunger;
import com.avmoga.dpixel.actors.buffs.Levitation;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Random;

public class Cloudberry extends Food {

	{
		name = "dungeon cloud berry";
		image = ItemSpriteSheet.SEED_CLOUDBERRY;
		energy = (Hunger.STARVING - Hunger.HUNGRY)/10;
		message = "Juicy!";
		hornValue = 1;
		bones = false;
	}

	private int duration = 10;
	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(10)) {
			case 0: case 1: case 2: case 3: case 4: case 5: 
				Buff.affect(hero, Haste.class, Haste.DURATION);
				GLog.i("You are moving much faster!");
				break;
			case 6: case 7: case 8: 
				 Buff.affect(hero, Haste.class, Haste.DURATION);
				Buff.affect(hero, Levitation.class, duration);
				GLog.i("You are moving much faster!");
				GLog.i("You float into the air!");
				break;
			 case 9: case 10:
				 Buff.affect(hero, Haste.class, Haste.DURATION);
				Buff.affect(hero, Levitation.class, duration*2);
				GLog.i("You are moving much faster!");
				GLog.i("You float into the air!");
				GLog.w("The berry releases energy into your body!");
				Buff.affect(hero, BerryRegeneration.class).level(hero.HT);
				break;
			}
		}
	}	
	
	@Override
	public String info() {
		return "A delectable and light berry found in the depths of the dungeon. "
				+"This berry seems to float in your hand and it vibrates with a certain energy. ";
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
	
	public Cloudberry() {
		this(1);
	}

	public Cloudberry(int value) {
		this.quantity = value;
	}
}
