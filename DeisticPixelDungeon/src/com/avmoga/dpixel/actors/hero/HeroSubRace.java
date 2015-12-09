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
package com.avmoga.dpixel.actors.hero;

import com.watabou.utils.Bundle;

public enum HeroSubRace {

	NONE(null, null),

	WARLOCK(
			"warlock",
			"A _Warlock_ has an affinity to magic, increasing the duration of additional effects on wands."), MONK(
			"monk",
			"The _Monk_ gains damage penetration when using his fists to deal damage."),

	DEMOLITIONIST("demolitionist",
			"The _demolitionist_ gains access to a wide variety of new bombs."), MERCENARY(
			"mercenary",
			"The _mercenary_ is smart about reading his enemies' attacks, and counterattacks at twice the speed."),

	RED(
			"red wraith",
			"_Red_ _Wraiths_ are surprisingly lucky."), BLUE(
			"blue wraith",
			"_Blue_ _Wraiths_ gain vision of all enemies that they have hit."),

	SHAMAN("shaman",
			"With an affinity to nature, the _Shaman_ gains natural healing when traveling through tall grass."), BRUTE(
			"brute",
			"The warriors of their species, the _Brute_ gains a power buff for all enemies around him.");

	private String title;
	private String desc;

	private HeroSubRace(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}

	public String title() {
		return title;
	}

	public String desc() {
		return desc;
	}

	private static final String SUBCLASS = "subClass";

	public void storeInBundle(Bundle bundle) {
		bundle.put(SUBCLASS, toString());
	}

	public static HeroSubRace restoreInBundle(Bundle bundle) {
		String value = bundle.getString(SUBCLASS);
		try {
			return valueOf(value);
		} catch (Exception e) {
			return NONE;
		}
	}

}
