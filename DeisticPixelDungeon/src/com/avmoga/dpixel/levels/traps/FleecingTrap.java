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
package com.avmoga.dpixel.levels.traps;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.npcs.SheepSokoban;
import com.avmoga.dpixel.actors.mobs.npcs.SheepSokobanCorner;
import com.avmoga.dpixel.actors.mobs.npcs.SheepSokobanSwitch;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Lightning;
import com.avmoga.dpixel.effects.particles.ShadowParticle;
import com.avmoga.dpixel.effects.particles.SparkParticle;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.armor.Armor;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class FleecingTrap {

	private static final String name = "fleecing trap";
	
	//Are we fleecing... the character?
	//Ew.

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanSwitch ){
			Camera.main.shake(2, 0.3f);
			ch.sprite.emitter().burst(ShadowParticle.UP, 5);
			ch.destroy();
		
		} else if (ch != null) {
			
			int dmg = ch.HT;			
						
			if (ch == Dungeon.hero) {
				
				Armor armor = Dungeon.hero.belongings.armor; 
				if (armor!=null){
					 Dungeon.hero.belongings.armor=null;
					 GLog.n("The fleecing trap destroys your armor!");
					 dmg=dmg/2;
				}

				ch.damage((dmg),null);
				Camera.main.shake(2, 0.3f);
				ch.sprite.emitter().burst(ShadowParticle.UP, 5);

				if (!ch.isAlive()) {
					Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
					GLog.n("You were killed by a discharge of a fleecing trap...");
				} 
		    }
		}

	}

	
}
