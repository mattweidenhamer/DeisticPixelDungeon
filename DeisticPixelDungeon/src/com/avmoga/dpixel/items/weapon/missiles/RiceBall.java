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
package com.avmoga.dpixel.items.weapon.missiles;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Drowsy;
import com.avmoga.dpixel.actors.mobs.BlueWraith;
import com.avmoga.dpixel.actors.mobs.DwarfLich;
import com.avmoga.dpixel.actors.mobs.FlyingProtector;
import com.avmoga.dpixel.actors.mobs.Golem;
import com.avmoga.dpixel.actors.mobs.RedWraith;
import com.avmoga.dpixel.actors.mobs.Sentinel;
import com.avmoga.dpixel.actors.mobs.ShadowYog;
import com.avmoga.dpixel.actors.mobs.Skeleton;
import com.avmoga.dpixel.actors.mobs.SpectralRat;
import com.avmoga.dpixel.actors.mobs.Statue;
import com.avmoga.dpixel.actors.mobs.Wraith;
import com.avmoga.dpixel.actors.mobs.Yog;
import com.avmoga.dpixel.actors.mobs.npcs.NPC;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.scrolls.ScrollOfTeleportation;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Random;

public class RiceBall extends MissileWeapon {
	
	public static final float DURATION = 10f;

	{
		name = "rice dumpling";
		image = ItemSpriteSheet.RICEBALL;
		rapperValue = 0;

		MIN = 1;
		MAX = 2;
		
		DLY = 0.25f;

		bones = false; 
	}

	public RiceBall() {
		this(1);
	}

	public RiceBall(int number) {
		super();
		quantity = number;
	}

	@Override
	public String desc() {
		return "A satisfying dumpling lovingly crafted from magic rice. "
		       +"Anything that eats would gladly take a dumpling. ";
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {
		
		
			
		if (defender != null 
				&& !(defender instanceof NPC)
				&& !(defender instanceof BlueWraith)
				&& !(defender instanceof Wraith)
				&& !(defender instanceof RedWraith)
				&& !(defender instanceof Sentinel)
				&& !(defender instanceof FlyingProtector)
				&& !(defender instanceof Golem)
				&& !(defender instanceof Skeleton)
				&& !(defender instanceof DwarfLich)
				&& !(defender instanceof Statue)
				&& !(defender instanceof Yog)
				&& !(defender instanceof ShadowYog)
				&& !(defender instanceof SpectralRat)
				) {
  
			Buff.affect(defender, Drowsy.class);
			defender.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
			
			if  (defender.HP/defender.HT > 0.01f){ 
			int count = 20;
			int pos;
			do {
				pos = Dungeon.level.randomRespawnCell();
				if (count-- <= 0) {
					break;
				}
			} while (pos == -1);

			if (pos == -1) {

				GLog.w(ScrollOfTeleportation.TXT_NO_TELEPORT);

			} else {

				defender.pos = pos;
				defender.sprite.place(defender.pos);
				defender.sprite.visible = Dungeon.visible[pos];
				GLog.i(curUser.name + " teleported " + defender.name
						+ " to somewhere");

			}

		   } else {

			GLog.i("nothing happened");

		  }
	    }
		
		super.proc(attacker, defender, damage);
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 15);
		return this;
	}

	@Override
	public int price() {
		return quantity * 2;
	}
}
