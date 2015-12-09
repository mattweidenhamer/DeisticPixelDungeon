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
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.blobs.Blob;
import com.avmoga.dpixel.actors.blobs.Fire;
import com.avmoga.dpixel.actors.blobs.Water;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.SpellSprite;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRegrowth extends Scroll {

	private static final String TXT_LAYOUT = "Your senses extend as the vegetation grows around you.";
	private static final String TXT_RANSACK = "A wail of agony fills your ears as the dungeon begins to burn!";
	{
		name = "Scroll of Regrowth";
		consumedValue = 15;
	}
	
	public void detonate(Heap heap) {
		ransack();
	}
	
	public void detonateIn(Hero hero) {
		ransack();
	}
	
	protected void ransack(){
		
		for (int i = 0; i < Level.getLength(); i++) {
			if(Level.flamable[i] || Actor.findChar(i) != null){
				GameScene.add(Blob.seed(i, 2, Fire.class));
			} //Yes, I know this could all be one if statement.
			else if(Actor.findChar(i)!= null){
				GameScene.add(Blob.seed(i, 2, Fire.class));
			}//Shut up.
			else if(Dungeon.level.heaps.get(i) != null){
				GameScene.add(Blob.seed(i, 2, Fire.class));
			}
		}
		Dungeon.observe();
		GLog.n(TXT_RANSACK);
	}

	@Override
	protected void doRead() {

		int length = Level.getLength();
		int[] map = Dungeon.level.map;
		boolean[] mapped = Dungeon.level.mapped;
		boolean[] discoverable = Level.discoverable;

		boolean noticed = false;

		for (int i = 0; i < length; i++) {
			
			GameScene.add(Blob.seed(i, (2) * 20, Water.class));
		
			int terr = map[i];

			if (discoverable[i]) {

				mapped[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

					Level.set(i, Terrain.discover(terr));
					GameScene.updateMap(i);

					if (Dungeon.visible[i]) {
						GameScene.discoverTile(i, terr);
						discover(i);

						noticed = true;
					}
				}
			}
		}
		Dungeon.observe();

		GLog.i(TXT_LAYOUT);
		if (noticed) {
			Sample.INSTANCE.play(Assets.SND_SECRET);
		}

		SpellSprite.show(curUser, SpellSprite.MAP);
		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		setKnown();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return "The magic in the scroll feels powerful and inviting."
				+ "The dungeon cries out for you to read it. ";
	}

	@Override
	public int price() {
		return isKnown() ? 25 * quantity : super.price();
	}

	public static void discover(int cell) {
		CellEmitter.get(cell).start(Speck.factory(Speck.DISCOVER), 0.1f, 4);
	}
}
