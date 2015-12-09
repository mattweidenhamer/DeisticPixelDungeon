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

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.blobs.Blob;
import com.avmoga.dpixel.actors.blobs.Fire;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.actors.mobs.npcs.MirrorImage;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.particles.FlameParticle;
import com.avmoga.dpixel.items.wands.WandOfBlink;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfMirrorImage extends Scroll {

	private static final int NIMAGES = 3;

	{
		name = "Scroll of Mirror Image";
		consumedValue = 5;
	}
	
	public void detonate(Heap heap){
		int count = 0;
		int toSpawn = Random.Int(3) + 1;
		while(count < toSpawn){
			for(int cell = 0; cell < Level.getLength(); cell++){
				if(Level.fieldOfView[cell] && Random.Int(5) == 2){
					if(Level.flamable[cell]
							|| Actor.findChar(cell) != null
							|| Dungeon.level.heaps.get(cell) != null){
						GameScene.add(Blob.seed(cell, 2, Fire.class));
					} else {
						CellEmitter.get(cell).burst(FlameParticle.FACTORY, 2);
					}
					count++;
					if(count >= toSpawn){
						break;
					}
				}
			}
			break;
		}
		Dungeon.observe();
	}
	public void detonateIn(Hero hero){
		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

		for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
			int p = Dungeon.hero.pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add(p);
			}
		}

		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index(respawnPoints);

			MirrorImage mob = new MirrorImage();
			mob.duplicate(Dungeon.hero);
			GameScene.add(mob);
			WandOfBlink.appear(mob, respawnPoints.get(index));

			respawnPoints.remove(index);
			nImages--;
		}

		if (nImages < NIMAGES) {
			setKnown();
		}

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();
	}

	@Override
	protected void doRead() {

		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

		for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
			int p = curUser.pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null
					&& (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add(p);
			}
		}

		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index(respawnPoints);

			MirrorImage mob = new MirrorImage();
			mob.duplicate(curUser);
			GameScene.add(mob);
			WandOfBlink.appear(mob, respawnPoints.get(index));

			respawnPoints.remove(index);
			nImages--;
		}

		if (nImages < NIMAGES) {
			setKnown();
		}

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return "The incantation on this scroll will create illusionary twins of the reader, which will chase his enemies.";
	}
}
