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
package com.avmoga.dpixel.plants;


import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Strength;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroSubClass;
import com.avmoga.dpixel.items.potions.PotionOfExperience;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Starflower extends Plant {

	private static final String TXT_DESC ="An extremely rare plant, " +
			"Starflower is said to grant celestial power to whomever touches it.";

	{
		image = 11;
		plantName = "Starflower";
	}
	
	@Override
	public void activate(Char ch) {
		if (ch==null){
			super.activate(ch);
		}
		if (ch instanceof Hero) {
		  Buff.affect(ch, Strength.class);
		}
		if (Random.Int(3)==0){super.activate(ch);}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed{

		{
			plantName = "Starflower";

			name = "Seed of " + plantName;
			image = ItemSpriteSheet.SEED_STARFLOWER;

			plantClass = Starflower.class;
			alchemyClass = PotionOfExperience.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
