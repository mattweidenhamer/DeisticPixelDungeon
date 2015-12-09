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
package com.avmoga.dpixel.actors.mobs;

import java.util.ArrayList;
import java.util.HashSet;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Burning;
import com.avmoga.dpixel.actors.buffs.Light;
import com.avmoga.dpixel.actors.buffs.Ooze;
import com.avmoga.dpixel.actors.buffs.Poison;
import com.avmoga.dpixel.actors.buffs.Roots;
import com.avmoga.dpixel.effects.Pushing;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.potions.PotionOfMending;
import com.avmoga.dpixel.items.scrolls.ScrollOfPsionicBlast;
import com.avmoga.dpixel.items.weapon.enchantments.Death;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.levels.features.Door;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.DemonGooSprite;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DemonGoo extends Mob {
	
protected static final float SPAWN_DELAY = 2f;

private int demonGooGeneration = 0;

private static final String DEMONGOOGENERATION = "demonGooGeneration";

	{
		name = "demon goo";
		HP = HT = 200+(adj(0)*Random.NormalIntRange(4, 7));
		EXP = 10;
		defenseSkill = 10+adj(1);
		//10
		spriteClass = DemonGooSprite.class;
		baseSpeed = 2f;
		viewDistance = Light.DISTANCE;

		loot = new PotionOfMending();
		lootChance = 1f;
	}

	private static final float SPLIT_DELAY = 1f;	
	
	@Override
	protected boolean act() {
		boolean result = super.act();
		
		if (Level.water[pos] && HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP++;
		} else if(Level.water[pos] && HP == HT && HT < 200){
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HT=HT+5;
			HP=HT;
		}
		return result;
	}

		
	@Override
	public int damageRoll() {
			return Random.NormalIntRange(30+adj(1), 60+adj(1));
	}

	@Override
	public int attackSkill(Char target) {
		return 35+adj(1);
	}

	@Override
	public int dr() {
		return 20+adj(1);
		//10
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEMONGOOGENERATION, demonGooGeneration);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		demonGooGeneration = bundle.getInt(DEMONGOOGENERATION);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		
		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;

			int[] neighbours = { pos + 1, pos - 1, pos + Level.getWidth(),
					pos - Level.getWidth() };
			for (int n : neighbours) {
				if (passable[n] && Actor.findChar(n) == null) {
					candidates.add(n);
				}
			}

			if (candidates.size() > 0) {
				GLog.n("Demon Goo divides!");
				DemonGoo clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element(candidates);
				clone.state = clone.HUNTING;

				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter(clone.pos);
				}

				GameScene.add(clone, SPLIT_DELAY);
				Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);

				HP -= clone.HP;
			}
		}

		return damage;
	}


	private DemonGoo split() {
		DemonGoo clone = new DemonGoo();
		clone.demonGooGeneration = demonGooGeneration + 1;
		if (buff(Burning.class) != null) {
			Buff.affect(clone, Burning.class).reignite(clone);
		}
		if (buff(Poison.class) != null) {
			Buff.affect(clone, Poison.class).set(2);
		}
		return clone;
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class);
			enemy.sprite.burst(0x000000, 5);
		}				
		return damage;
	}

	@Override
	public void notice() {
		super.notice();
		yell("GLURP-GLURP-GLUURRRRP!");
	}

	@Override
	public String description() {
		return "Demon Goo is pretty angry you killed its buddy in the sewers. ";
	}

	@Override
	public void die(Object cause) {

		super.die(cause);
       	yell("glurp... glurp...");
	}
	
	

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
		
	
}
