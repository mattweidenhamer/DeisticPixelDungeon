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

import java.util.HashSet;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Burning;
import com.avmoga.dpixel.actors.buffs.Light;
import com.avmoga.dpixel.actors.buffs.Roots;
import com.avmoga.dpixel.actors.buffs.Terror;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.particles.BlastParticle;
import com.avmoga.dpixel.effects.particles.PurpleParticle;
import com.avmoga.dpixel.effects.particles.SmokeParticle;
import com.avmoga.dpixel.items.ArmorKit;
import com.avmoga.dpixel.items.Gold;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.InactiveMrDestructo;
import com.avmoga.dpixel.items.RedDewdrop;
import com.avmoga.dpixel.items.keys.SkeletonKey;
import com.avmoga.dpixel.items.scrolls.ScrollOfRecharging;
import com.avmoga.dpixel.items.weapon.enchantments.Death;
import com.avmoga.dpixel.items.weapon.enchantments.Leech;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.BrokenRobotSprite;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.MrDestructoSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Lichen extends Mob {
	

	{
		name = "lichen";
		spriteClass = MrDestructoSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT= 100;
		defenseSkill = 3;	
		viewDistance = 1;
	}

	
	private static final float SPAWN_DELAY = 0.1f;
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 3);
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(1) == 0) {
			Buff.prolong(enemy, Roots.class, 10);
		}

		return damage;
	}
	
	@Override
	public void move(int step) {		
	}
		
	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	public static void spawnAroundChance(int pos) {
		for (int n : Level.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null && Random.Float()<0.25f) {
				spawnAt(cell);
				GLog.i("You shed a lichen from you body. ");
			}
		}
	}
		
    public static Lichen spawnAt(int pos) {
		
    	Lichen b = new Lichen();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
		
	@Override
	public int attackSkill(Char target) {
		return 20+(Dungeon.depth);
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}
		
	
	@Override
	public void beckon(int cell) {
	}
	
	@Override
	public String description() {
		return "The contraption has sprung to life! "
				+ "It is blowing away nearby mobs!";
	}

	
			
}
