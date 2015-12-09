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
package com.avmoga.dpixel.actors.mobs.pets;

import java.util.HashSet;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.Blob;
import com.avmoga.dpixel.actors.blobs.Fire;
import com.avmoga.dpixel.actors.blobs.Web;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Frost;
import com.avmoga.dpixel.actors.buffs.Paralysis;
import com.avmoga.dpixel.actors.buffs.Poison;
import com.avmoga.dpixel.actors.buffs.Weakness;
import com.avmoga.dpixel.actors.mobs.Bee;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.particles.SnowParticle;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.BlueDragonSprite;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.MirrorSprite;
import com.avmoga.dpixel.sprites.RedDragonSprite;
import com.avmoga.dpixel.sprites.ScorpionSprite;
import com.avmoga.dpixel.sprites.SpiderSprite;
import com.avmoga.dpixel.sprites.SteelBeeSprite;
import com.avmoga.dpixel.sprites.VelociroosterSprite;
import com.avmoga.dpixel.sprites.WarlockSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Scorpion extends PET {
	
	{
		name = "scorpion";
		spriteClass = ScorpionSprite.class;       
		flying=false;
		state = HUNTING;
		level = 1;
		type = 8;
		cooldown=1000;

	}	
			
	protected int regen = 1;	
	protected float regenChance = 0.1f;	
		

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (2 + level) * 5;
		defenseSkill = 1 + level*2;
	}
	

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {		
		return Random.NormalIntRange(HT/4, HT) ;		
	}

	@Override
	protected boolean act() {
		
		if (cooldown>0){
			cooldown=Math.max(cooldown-(level*level),0);
			if (cooldown==0) {GLog.w("Your scorpion readies its stinger!");}
		}
		
		if (Random.Float()<regenChance && HP<HT){HP+=regen;}

		return super.act();
	}			
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (cooldown>0 && Random.Int(10) == 0) {
			Buff.prolong(enemy, Paralysis.class, Random.Float(1, 1.5f + level));			
		}
		if (cooldown==0) {
			Buff.prolong(enemy, Paralysis.class, Random.Float(1, 1.5f + level));	
			
			sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f,	1);
			sprite.showStatus(CharSprite.POSITIVE,Integer.toString(damage));
			
			HP = Math.min(HT, HP+damage);
			
			Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f,	1);
			Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE,Integer.toString(damage));
			
			Dungeon.hero.HP = Math.min(Dungeon.hero.HT, Dungeon.hero.HP+damage);

			damage+=damage;
			
			yell("Sting!");
			cooldown=1000;
		}

		return damage;
	}
	
@Override
public String description() {
	return "A super sized blood thirsty scorpion. Its tail is tipped with a dangerous stinger. ";
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


}