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

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Cripple;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.items.SanChikarahTranscend;
import com.avmoga.dpixel.items.artifacts.DriedRose;
import com.avmoga.dpixel.items.artifacts.TimekeepersHourglass;
import com.avmoga.dpixel.levels.RegularLevel;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.scenes.InterlevelScene;
import com.avmoga.dpixel.sprites.MobSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Chasm {

	private static final String TXT_CHASM = "Chasm";
	private static final String TXT_YES = "Yes, I know what I'm doing";
	private static final String TXT_NO = "No, I changed my mind";
	private static final String TXT_JUMP = "Do you really want to jump into the chasm? You can probably die.";

	public static boolean jumpConfirmed = false;

	public static void heroJump(final Hero hero) {
		GameScene.show(new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO) {
			@Override
			protected void onSelect(int index) {
				if (index == 0) {
					jumpConfirmed = true;
					hero.resume();
				}
			};
		});
	}

	public static void heroFall(int pos) {

		jumpConfirmed = false;

		Sample.INSTANCE.play(Assets.SND_FALLING);

		Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null)
			buff.detach();

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
			if (mob instanceof DriedRose.GhostHero)
				mob.destroy();
		
		SanChikarahTranscend san3 = Dungeon.hero.belongings.getItem(SanChikarahTranscend.class);
		if (Dungeon.depth==33 && san3 != null) {			
			san3.detach(Dungeon.hero.belongings.backpack);
			Dungeon.sanchikarahtranscend= false;						
		}

		if (Dungeon.hero.isAlive()) {
			Dungeon.hero.interrupt();
			InterlevelScene.mode = InterlevelScene.Mode.FALL;
			if (Dungeon.level instanceof RegularLevel) {
				Room room = ((RegularLevel) Dungeon.level).room(pos);
				InterlevelScene.fallIntoPit = room != null
						&& room.type == Room.Type.WEAK_FLOOR;
			} else {
				InterlevelScene.fallIntoPit = false;
			}
			Game.switchScene(InterlevelScene.class);
		} else {
			Dungeon.hero.sprite.visible = false;
		}
	}

	public static void heroLand() {

		Hero hero = Dungeon.hero;

		hero.sprite.burst(hero.sprite.blood(), 10);
		Camera.main.shake(4, 0.2f);

		Buff.prolong(hero, Cripple.class, Cripple.DURATION);
			
				
		hero.damage(Random.IntRange(hero.HT / 3, hero.HT / 2), new Hero.Doom() {
			@Override
			public void onDeath() {
				Badges.validateDeathFromFalling();

				Dungeon.fail(ResultDescriptions.FALL);
				GLog.n("You fell to death...");
			}
		});
	}

	public static void mobFall(Mob mob) {
		mob.die(null);

		((MobSprite) mob.sprite).fall();
	}
}
