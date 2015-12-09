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

import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Rice;
import com.avmoga.dpixel.items.food.Blackberry;
import com.avmoga.dpixel.items.food.Blueberry;
import com.avmoga.dpixel.items.food.Cloudberry;
import com.avmoga.dpixel.items.food.FullMoonberry;
import com.avmoga.dpixel.items.food.GoldenNut;
import com.avmoga.dpixel.items.food.Moonberry;
import com.avmoga.dpixel.items.food.Nut;
import com.avmoga.dpixel.items.food.ToastedNut;
import com.avmoga.dpixel.plants.Plant;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;

public class SeedPouch extends Bag {

	{
		name = "seed pouch";
		image = ItemSpriteSheet.POUCH;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		if (item instanceof GoldenNut ||  item instanceof ToastedNut || item instanceof Nut 
			|| item instanceof Blackberry
			|| item instanceof Blueberry
			|| item instanceof Cloudberry
			|| item instanceof Moonberry
			|| item instanceof FullMoonberry
			|| item instanceof Plant.Seed
			|| item instanceof Rice){
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
		return "This small velvet pouch allows you to store any number of seeds in it. Very convenient.";
	}
}
