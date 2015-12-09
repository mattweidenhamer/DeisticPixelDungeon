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

import com.avmoga.dpixel.actors.mobs.SeekingBomb;
import com.avmoga.dpixel.actors.mobs.SeekingClusterBomb;
import com.avmoga.dpixel.items.ActiveMrDestructo;
import com.avmoga.dpixel.items.ActiveMrDestructo2;
import com.avmoga.dpixel.items.Ankh;
import com.avmoga.dpixel.items.Bomb;
import com.avmoga.dpixel.items.ClusterBomb;
import com.avmoga.dpixel.items.DizzyBomb;
import com.avmoga.dpixel.items.HolyHandGrenade;
import com.avmoga.dpixel.items.InactiveMrDestructo;
import com.avmoga.dpixel.items.InactiveMrDestructo2;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.OrbOfZot;
import com.avmoga.dpixel.items.SeekingBombItem;
import com.avmoga.dpixel.items.SeekingClusterBombItem;
import com.avmoga.dpixel.items.SmartBomb;
import com.avmoga.dpixel.items.rings.Ring;
import com.avmoga.dpixel.items.scrolls.Scroll;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		name = "scroll holder";
		image = ItemSpriteSheet.HOLDER;

		size = 20;
	}

	@Override
	public boolean grab(Item item) {
		if (item instanceof Scroll 
				||  item instanceof Bomb 
				||  item instanceof DizzyBomb 
				||  item instanceof SmartBomb 
				||  item instanceof SeekingBombItem 
				||  item instanceof ClusterBomb 
				||  item instanceof SeekingClusterBombItem 
				||  item instanceof ActiveMrDestructo
				||  item instanceof ActiveMrDestructo2
				||  item instanceof InactiveMrDestructo
				||  item instanceof InactiveMrDestructo2
				||  item instanceof OrbOfZot
				||  item instanceof HolyHandGrenade
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
		return "This tubular container looks like it would hold an astronomer's charts, but your scrolls will fit just as well.\n\n"
				+ "The holder doesn't look very flammable, so your scrolls should be safe from fire inside it."
				+ "There is a handy little compartment for your bombs and other explosives too. Nice!";
	}
}
