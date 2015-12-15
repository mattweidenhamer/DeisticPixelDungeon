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
package com.avmoga.dpixel.items;

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.buffs.Blindness;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Fury;
import com.avmoga.dpixel.actors.buffs.Madness;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroSubClass;
import com.avmoga.dpixel.actors.hero.HeroSubRace;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.SpellSprite;
import com.avmoga.dpixel.items.rings.RingOfWealth;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.avmoga.dpixel.windows.WndChooseWay;
import com.watabou.noosa.audio.Sample;

public class TomeOfSpecialty extends Item {

	private static final String TXT_BLINDED = "You can't read while blinded";

	public static final float TIME_TO_READ = 10;

	public static final String AC_READ = "READ";

	{
		stackable = false;
		name = "Tome of Specialty";
		image = ItemSpriteSheet.SPECIALTY;

		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_READ)) {

			if (hero.buff(Blindness.class) != null) {
				GLog.w(TXT_BLINDED);
				return;
			}

			curUser = hero;

			HeroSubRace way1 = null;
			HeroSubRace way2 = null;
			switch (hero.heroRace) {
			case HUMAN:
				way1 = HeroSubRace.DEMOLITIONIST;
				way2 = HeroSubRace.MERCENARY;
				break;
			case GNOLL:
				way1 = HeroSubRace.SHAMAN;
				way2 = HeroSubRace.BRUTE;
				break;
			case DWARF:
				way1 = HeroSubRace.WARLOCK;
				way2 = HeroSubRace.MONK;
				break;
			case WRAITH:
				way1 = HeroSubRace.RED;
				way2 = HeroSubRace.BLUE;
				break;
			}
			GameScene.show(new WndChooseWay(this, way1, way2));

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean doPickUp(Hero hero) {
		Badges.validateRace(hero.heroRace);
		return super.doPickUp(hero);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return "This worn leather book is not that thick, but you feel somehow, "
				+ "that you can gather a lot from it. Remember though that reading "
				+ "this tome may require some time.";
	}

	public void choose(HeroSubRace way) {

		detach(curUser.belongings.backpack);

		curUser.spend(TomeOfSpecialty.TIME_TO_READ);
		curUser.busy();

		curUser.subRace = way;

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_MASTERY);

		SpellSprite.show(curUser, SpellSprite.MASTERY);
		curUser.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);
		GLog.w("You have chosen the way of the %s!",
				Utils.capitalize(way.title()));

		if (way == HeroSubRace.RED) {
			RingOfWealth ring = new RingOfWealth(); 
			ring.degrade(); ring.degrade(); ring.degrade(); ring.collect();
			//If you want to use this, you gotta work for it.
		}
		else if (way == HeroSubRace.BRUTE) {
			Dungeon.hero.buff(Madness.class);
			
		}
	}
}
