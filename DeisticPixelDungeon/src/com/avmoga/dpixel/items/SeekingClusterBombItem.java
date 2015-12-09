/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.avmoga.dpixel.items;

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.Statistics;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroClass;
import com.avmoga.dpixel.actors.mobs.MrDestructo;
import com.avmoga.dpixel.actors.mobs.SeekingBomb;
import com.avmoga.dpixel.actors.mobs.SeekingClusterBomb;
import com.avmoga.dpixel.actors.mobs.npcs.MirrorImage;
import com.avmoga.dpixel.actors.mobs.npcs.SeekingBombNPC;
import com.avmoga.dpixel.actors.mobs.npcs.SeekingClusterBombNPC;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.particles.BlastParticle;
import com.avmoga.dpixel.effects.particles.SmokeParticle;
import com.avmoga.dpixel.items.wands.WandOfBlink;
import com.avmoga.dpixel.items.weapon.missiles.RiceBall;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.ItemSprite;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SeekingClusterBombItem extends Item {

	{
		name = "seeking cluster bomb";
		image = ItemSpriteSheet.SEEKING_CLUSTER_BOMB;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
	}

	
	// FIXME using a static variable for this is kinda gross, should be a better
	// way
	private static boolean seek = false;

	private static final String AC_LIGHTTHROW = "Light & Throw";
	

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHTTHROW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_LIGHTTHROW)) {
			seek = true;
			action = AC_THROW;
		} else {
			seek = false;
		}
		super.execute(hero, action);
	}

	@Override
	protected void onThrow(int cell) {
		if (Actor.findChar(cell) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : Level.NEIGHBOURS8)
				if (Level.passable[cell + i])
					candidates.add(cell + i);
			int newCell = candidates.isEmpty() ? cell : Random
					.element(candidates);
			
			   if (!Level.pit[newCell] && seek) {
				  SeekingClusterBombNPC.spawnAt(newCell);
				   } else {
			  Dungeon.level.drop(this, newCell).sprite.drop(cell);
			   }
			   
		} else if (!Level.pit[cell] && seek) {
			   SeekingClusterBombNPC.spawnAt(cell);
			
		} else {
			
			super.onThrow(cell);
		}
	}

	

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}


	@Override
	public int price() {
		return 20 * quantity;
	}

	@Override
	public String info() {
		return "A destructive powerful bomb with internal magic machinery. Releasing it will send it after enemies.";
	}

	
}
