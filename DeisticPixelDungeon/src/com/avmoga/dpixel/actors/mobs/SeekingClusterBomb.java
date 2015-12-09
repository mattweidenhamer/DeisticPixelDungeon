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
import com.avmoga.dpixel.actors.buffs.Light;
import com.avmoga.dpixel.actors.buffs.Terror;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.particles.BlastParticle;
import com.avmoga.dpixel.effects.particles.PurpleParticle;
import com.avmoga.dpixel.effects.particles.SmokeParticle;
import com.avmoga.dpixel.items.ArmorKit;
import com.avmoga.dpixel.items.Bomb;
import com.avmoga.dpixel.items.ClusterBomb;
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
import com.avmoga.dpixel.sprites.CrabSprite;
import com.avmoga.dpixel.sprites.MrDestructoSprite;
import com.avmoga.dpixel.sprites.SeekingClusterBombSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SeekingClusterBomb extends Mob {

	

	{
		name = "seeking cluster bomb";
		spriteClass = SeekingClusterBombSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT= 10;
		defenseSkill = 3;	
	}

	
	private static final float SPAWN_DELAY = 0.1f;
	
	@Override
	public int dr() {
		return 1;
	}

	
	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		ClusterBomb cbomb = new ClusterBomb();
		for (int n : Level.NEIGHBOURS8DIST2) {
			int c = pos + n;
			if (Random.Int(3)==0){
			cbomb.explode(c);
			//spend(2f);
			}
		}
			
		yell("KA-BOOM!!! KA-BOOM!!! KA-BOOM!!!");
		
		destroy();
		sprite.die();

		return dmg;
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

	@Override
	public String description() {
		return "This bomb is hunting the dungeon for enemies. ";
	}

	

	public void interact() {

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();
	}
	
		
	
    public static SeekingClusterBomb spawnAt(int pos) {
		
    	SeekingClusterBomb b = new SeekingClusterBomb();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
	
	
    @Override
	public void die(Object cause) {
		ClusterBomb cbomb = new ClusterBomb();
		cbomb.explode(pos);

		super.die(cause);
	}
	

	@Override
	public void beckon(int cell) {
	}
	
	

	
	
			
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ToxicGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
