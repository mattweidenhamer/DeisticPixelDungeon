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
import java.util.HashSet;

import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.actors.buffs.Blindness;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.ItemStatusHandler;
import com.avmoga.dpixel.items.artifacts.UnstableSpellbook;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Bundle;

public abstract class Scroll extends Item {

	private static final String TXT_BLINDED = "You can't read a scroll while blinded";

	private static final String TXT_CURSED = "Your cursed spellbook prevents you from invoking this scroll's magic! "
			+ "A scroll of remove curse might be strong enough to still work though...";

	public static final String AC_READ = "READ";

	protected static final float TIME_TO_READ = 1f;

	private static final Class<?>[] scrolls = { ScrollOfIdentify.class,
			ScrollOfMagicMapping.class, ScrollOfRecharging.class,
			ScrollOfRemoveCurse.class, ScrollOfTeleportation.class,
			ScrollOfUpgrade.class, ScrollOfRage.class, ScrollOfTerror.class,
			ScrollOfLullaby.class, ScrollOfMagicalInfusion.class,
			ScrollOfPsionicBlast.class, ScrollOfMirrorImage.class, ScrollOfRegrowth.class };
	private static final String[] runes = { "KAUNAN", "SOWILO", "LAGUZ",
			"YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ", "NAUDIZ", "BERKANAN",
			"NCOSRANE", "TIWAZ", "NENDIL" };
	private static final Integer[] images = { ItemSpriteSheet.SCROLL_KAUNAN,
			ItemSpriteSheet.SCROLL_SOWILO, ItemSpriteSheet.SCROLL_LAGUZ,
			ItemSpriteSheet.SCROLL_YNGVI, ItemSpriteSheet.SCROLL_GYFU,
			ItemSpriteSheet.SCROLL_RAIDO, ItemSpriteSheet.SCROLL_ISAZ,
			ItemSpriteSheet.SCROLL_MANNAZ, ItemSpriteSheet.SCROLL_NAUDIZ,
			ItemSpriteSheet.SCROLL_BERKANAN, ItemSpriteSheet.SCROLL_NCOSRANE,
			ItemSpriteSheet.SCROLL_TIWAZ, ItemSpriteSheet.SCROLL_NENDIL };

	private static ItemStatusHandler<Scroll> handler;

	private String rune;

	public boolean ownedByBook = false;

	public int consumedValue;

	{
		stackable = true;
		defaultAction = AC_READ;
	}

	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<Scroll>(
				(Class<? extends Scroll>[]) scrolls, runes, images);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemStatusHandler<Scroll>(
				(Class<? extends Scroll>[]) scrolls, runes, images, bundle);
	}

	public Scroll() {
		super();
		syncVisuals();
	}

	@Override
	public void syncVisuals() {
		image = handler.image(this);
		rune = handler.label(this);
	};

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}
	public void detonate(Heap heap){
		
	}
	public void detonateIn(Hero hero){
		
	}
	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_READ)) {

			if (hero.buff(Blindness.class) != null) {
				GLog.w(TXT_BLINDED);
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class)
							.isCursed()
					&& !(this instanceof ScrollOfRemoveCurse)) {
				GLog.n(TXT_CURSED);
			} else {
				curUser = hero;
				curItem = detach(hero.belongings.backpack);
				doRead();
			}

		} else {

			super.execute(hero, action);

		}
	}

	abstract protected void doRead();

	public boolean isKnown() {
		return handler.isKnown(this);
	}

	public void setKnown() {
		if (!isKnown() && !ownedByBook) {
			handler.know(this);
		}

		Badges.validateAllScrollsIdentified();
	}
	public void detonates() {
		
	}
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}

	@Override
	public String name() {
		return isKnown() ? name : "scroll \"" + rune + "\"";
	}

	@Override
	public String info() {
		return isKnown() ? desc()
				: "This parchment is covered with indecipherable writing, and bears a title "
						+ "of rune "
						+ rune
						+ ". Who knows what it will do when read aloud?";
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return isKnown();
	}

	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}

	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}

	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}
}