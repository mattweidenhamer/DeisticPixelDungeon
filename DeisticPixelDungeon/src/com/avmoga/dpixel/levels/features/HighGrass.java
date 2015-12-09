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
package com.avmoga.dpixel.levels.features;

import com.avmoga.dpixel.Challenges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Barkskin;
import com.avmoga.dpixel.actors.buffs.BerryRegeneration;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Regeneration;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.plants.Flytrap;
import com.avmoga.dpixel.actors.hero.HeroSubClass;
import com.avmoga.dpixel.actors.hero.HeroSubRace;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.particles.LeafParticle;
import com.avmoga.dpixel.items.Dewdrop;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.RedDewdrop;
import com.avmoga.dpixel.items.VioletDewdrop;
import com.avmoga.dpixel.items.YellowDewdrop;
import com.avmoga.dpixel.items.artifacts.MysticBranch;
import com.avmoga.dpixel.items.artifacts.SandalsOfNature;
import com.avmoga.dpixel.items.food.Blackberry;
import com.avmoga.dpixel.items.food.Blueberry;
import com.avmoga.dpixel.items.food.Cloudberry;
import com.avmoga.dpixel.items.food.Moonberry;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.plants.BlandfruitBush;
import com.avmoga.dpixel.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample(Level level, int pos, Char ch) {

		Level.set(pos, Terrain.GRASS);
		GameScene.updateMap(pos);

		if (!Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
			int naturalismLevel = 0;
			int woodLevel = 0;

			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch
						.buff(SandalsOfNature.Naturalism.class);
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.level() + 1;
						naturalism.charge();
					} else {
						naturalismLevel = -1;
					}
				}
				MysticBranch.SlowLevel slowLevel = ch.buff(MysticBranch.SlowLevel.class);
				if(slowLevel != null){
					if(!slowLevel.isCursed()){
						woodLevel = slowLevel.level() + 1;
						slowLevel.checkUpgrade(woodLevel);
					}
				}
			}

			if (naturalismLevel >= 0) {
				// Seed
				if (Random.Int(18 - ((int) (naturalismLevel * 3.34))) == 0) {
					Item seed = Generator.random(Generator.Category.SEED);

					if (seed instanceof BlandfruitBush.Seed) {
						if (Random.Int(15)
								- Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
							level.drop(seed, pos).sprite.drop();
							Dungeon.limitedDrops.blandfruitSeed.count++;
						}
						
					  }	else if (seed instanceof Flytrap.Seed) {
						if (Random.Int(15)
								- Dungeon.limitedDrops.upgradeEaterSeed.count >= 0) {
							level.drop(seed, pos).sprite.drop();
							Dungeon.limitedDrops.upgradeEaterSeed.count++;
						}
						
					  }else if (seed instanceof Blackberry
								|| seed instanceof Cloudberry
								|| seed instanceof Blueberry
								|| seed instanceof Moonberry								
								) {
							if (Random.Int(40)- Dungeon.limitedDrops.berries.count >= 0) {
								level.drop(seed, pos).sprite.drop();
								Dungeon.limitedDrops.berries.count++;
							}
					} else
						level.drop(seed, pos).sprite.drop();
				}
				
				// Mushroom
				if (Dungeon.growLevel(Dungeon.depth) && Random.Int(40 - ((int) (naturalismLevel * 3.34))) == 0) {
					Item mushroom = Generator.random(Generator.Category.MUSHROOM);
					level.drop(mushroom, pos).sprite.drop();
				}
				
				// Dew
				if (Random.Int(3 - naturalismLevel) == 0) {
					if (Random.Int(30 - naturalismLevel) == 0 && naturalismLevel>0) {
						level.drop(new YellowDewdrop(), pos).sprite.drop();
					} else if (Random.Int(50 - naturalismLevel) == 0 && naturalismLevel>2) {
						level.drop(new RedDewdrop(), pos).sprite.drop();
					} else if (Random.Int(100 - naturalismLevel) == 0 && naturalismLevel>4){
						level.drop(new VioletDewdrop(), pos).sprite.drop();
					} else {
					   level.drop(new Dewdrop(), pos).sprite.drop();
					}
				}
			}
		}

		int leaves = 4;

		// Barkskin
		if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
			Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
			leaves = 8;
		}
		if (ch instanceof Hero && ((Hero) ch).subRace == HeroSubRace.SHAMAN) {
			Buff.affect(ch, BerryRegeneration.class).level(ch.HT / 3);
			leaves = 8;
		}

		CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, leaves);
		Dungeon.observe();
	}
}
