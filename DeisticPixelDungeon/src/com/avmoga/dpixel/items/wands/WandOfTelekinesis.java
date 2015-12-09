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
package com.avmoga.dpixel.items.wands;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.actors.mobs.npcs.Shopkeeper;
import com.avmoga.dpixel.effects.MagicMissile;
import com.avmoga.dpixel.effects.Pushing;
import com.avmoga.dpixel.items.Dewdrop;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Heap.Type;
import com.avmoga.dpixel.items.potions.PotionOfStrength;
import com.avmoga.dpixel.items.scrolls.ScrollOfUpgrade;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.levels.features.Chasm;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfTelekinesis extends Wand {

	private static final String TXT_YOU_NOW_HAVE = "You have magically transported %s into your backpack";

	{
		name = "Wand of Telekinesis";
		hitChars = false;
	}

	@Override
	protected void onZap(int cell) {

		boolean mapUpdated = false;

		int maxDistance = level() + 4;
		Ballistica.distance = Math.min(Ballistica.distance, maxDistance);

		Char ch;
		Heap heap = null;

		for (int i = 1; i < Ballistica.distance; i++) {

			int c = Ballistica.trace[i];

			int before = Dungeon.level.map[c];

			if ((ch = Actor.findChar(c)) != null) {

				if (i == Ballistica.distance - 1) {

					ch.damage(maxDistance - 1 - i, this);

				} else {

					int next = Ballistica.trace[i + 1];
					if ((Level.passable[next] || Level.avoid[next])
							&& Actor.findChar(next) == null) {

						Actor.addDelayed(new Pushing(ch, ch.pos, next), -1);

						ch.pos = next;
						Actor.freeCell(next);

						if (ch instanceof Shopkeeper)
							ch.damage(0, this);

						if (ch instanceof Mob) {
							Dungeon.level.mobPress((Mob) ch);
							if (Dungeon.level.pit[ch.pos]){
								Chasm.mobFall((Mob) ch);
							}
						} else {
							Dungeon.level.press(ch.pos, ch);
						}

					} else {

						ch.damage(maxDistance - 1 - i, this);

					}
				}
			}

			if (heap == null && (heap = Dungeon.level.heaps.get(c)) != null) {
				switch (heap.type) {
				case HEAP:
					transport(heap);
					break;
				case CHEST:
					open(heap);
					break;
				default:
				}
			}

			Dungeon.level.press(c, null);
			if (before == Terrain.OPEN_DOOR && Actor.findChar(c) == null) {

				Level.set(c, Terrain.DOOR);
				GameScene.updateMap(c);

			} else if (Level.water[c]) {

				GameScene.ripple(c);

			}

			if (!mapUpdated && Dungeon.level.map[c] != before) {
				mapUpdated = true;
			}
		}

		if (mapUpdated) {
			Dungeon.observe();
		}
	}

	private void transport(Heap heap) {
		Item item = heap.pickUp();
		if (item.doPickUp(curUser)) {

			if (item instanceof Dewdrop) {

			} else {

				if ((item instanceof ScrollOfUpgrade && ((ScrollOfUpgrade) item)
						.isKnown())
						|| (item instanceof PotionOfStrength && ((PotionOfStrength) item)
								.isKnown())) {
					GLog.p(TXT_YOU_NOW_HAVE, item.name());
				} else {
					GLog.i(TXT_YOU_NOW_HAVE, item.name());
				}
			}

		} else {
			Dungeon.level.drop(item, curUser.pos).sprite.drop();
		}
	}

	private void open(Heap heap) {
		heap.type = Type.HEAP;
		heap.sprite.link();
		heap.sprite.drop();
	}

	@Override
	protected void fx(int cell, Callback callback) {
		MagicMissile.force(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return "Waves of magic force from this wand will affect all cells on their way triggering traps, trampling high vegetation, "
				+ "opening closed doors and closing open ones. They also push back monsters.";
	}
}
