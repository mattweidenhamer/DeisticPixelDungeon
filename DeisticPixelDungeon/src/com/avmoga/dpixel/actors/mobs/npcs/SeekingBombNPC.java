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
package com.avmoga.dpixel.actors.mobs.npcs;

import java.util.HashSet;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Burning;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.actors.mobs.MrDestructo;
import com.avmoga.dpixel.actors.mobs.Rat;
import com.avmoga.dpixel.items.Bomb;
import com.avmoga.dpixel.items.ClusterBomb;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.ClusterBomb.Fuse;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.BatSprite;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.MirrorSprite;
import com.avmoga.dpixel.sprites.SeekingBombSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SeekingBombNPC extends NPC {

	{
		name = "seeking bomb";
		spriteClass = SeekingBombSprite.class;

		state = HUNTING;

	}
	
	private static final float SPAWN_DELAY = 0.1f;
	
	@Override
	public int attackSkill(Char target) {
		return 99;
	}
	
	
	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		Bomb bomb = new Bomb();
		bomb.explode(pos);
		yell("KA-BOOM!!!");

		
		
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

	
	@Override
	public void interact() {

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();
	}

    public static SeekingBombNPC spawnAt(int pos) {
		
    	SeekingBombNPC b = new SeekingBombNPC();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
	
	@Override
	public void die(Object cause) {
		Bomb bomb = new Bomb();
		bomb.explode(pos);

		super.die(cause);
	}
	
}