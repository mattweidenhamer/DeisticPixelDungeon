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
package com.avmoga.dpixel.items.scrolls;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.buffs.Amok;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.actors.buffs.Silence;
import com.avmoga.dpixel.actors.mobs.Mimic;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRage extends Scroll {
	private static final String TXT_DET = "You can't think straight!";

	{
		name = "Scroll of Rage";
		consumedValue = 5;
	}
	public void detonate(Heap heap){
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (Level.distance(heap.pos, mob.pos) <= 3){
				Buff.affect(mob, Silence.class, Silence.duration(mob));
			}
		}
		if (Level.distance(heap.pos, Dungeon.hero.pos) <= 3){
			Buff.affect(Dungeon.hero, Silence.class, Silence.duration(Dungeon.hero));
			GLog.w(TXT_DET);
		}
	}
	public void detonateIn(Hero hero){
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			mob.beckon(Dungeon.hero.pos);
		}
		for (Heap heap : Dungeon.level.heaps.values()){
			if (heap.type == Heap.Type.MIMIC){
				Mimic m = Mimic.spawnAt(heap.pos, heap.items);
				if (m != null){
					m.beckon(Dungeon.hero.pos);
					heap.destroy();
				}
			}
		}
		Buff.affect(hero, Silence.class, Silence.duration(hero));
		GLog.w(TXT_DET);
	}

	@Override
	protected void doRead() {

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			mob.beckon(curUser.pos);
			if (Level.fieldOfView[mob.pos]) {
				Buff.prolong(mob, Amok.class, 5f);
			}
		}

		for (Heap heap : Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.MIMIC) {
				Mimic m = Mimic.spawnAt(heap.pos, heap.items);
				if (m != null) {
					m.beckon(curUser.pos);
					heap.destroy();
				}
			}
		}

		GLog.w("The scroll emits an enraging roar that echoes throughout the dungeon!");
		setKnown();

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f,
				3);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		Invisibility.dispel();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return "When read aloud, this scroll will unleash a great roar "
				+ "that draws all enemies to the reader, and enrages nearby ones.";
	}
}
