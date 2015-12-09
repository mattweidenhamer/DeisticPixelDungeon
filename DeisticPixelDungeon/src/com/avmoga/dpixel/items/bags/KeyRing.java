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
package com.avmoga.dpixel.items.bags;

import com.avmoga.dpixel.items.Ankh;
import com.avmoga.dpixel.items.CavesKey;
import com.avmoga.dpixel.items.CityKey;
import com.avmoga.dpixel.items.HallsKey;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.PrisonKey;
import com.avmoga.dpixel.items.Rice;
import com.avmoga.dpixel.items.SewersKey;
import com.avmoga.dpixel.items.TenguKey;
import com.avmoga.dpixel.items.food.Blackberry;
import com.avmoga.dpixel.items.food.Blueberry;
import com.avmoga.dpixel.items.food.Cloudberry;
import com.avmoga.dpixel.items.food.FullMoonberry;
import com.avmoga.dpixel.items.food.GoldenNut;
import com.avmoga.dpixel.items.food.Moonberry;
import com.avmoga.dpixel.items.food.Nut;
import com.avmoga.dpixel.items.food.ToastedNut;
import com.avmoga.dpixel.items.keys.Key;
import com.avmoga.dpixel.items.rings.Ring;
import com.avmoga.dpixel.plants.Plant;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;

public class KeyRing extends Bag {

	{
		name = "key ring";
		image = ItemSpriteSheet.KEYRING;

		size = 20;
	}

	@Override
	public boolean grab(Item item) {
		if (item instanceof Key 
			||  item instanceof CavesKey
			||  item instanceof CityKey
			||  item instanceof TenguKey
			||  item instanceof SewersKey
			||  item instanceof HallsKey
			||  item instanceof PrisonKey
				){
			return true;
			} else {
			return false;
			}
	}

	@Override
	public int price() {
		return 50;
	}

	@Override
	public String info() {
		return "This keyring can hold your keys. Very handy!";
	}
}

