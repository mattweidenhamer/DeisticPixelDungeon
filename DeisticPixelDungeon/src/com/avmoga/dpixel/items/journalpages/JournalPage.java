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
package com.avmoga.dpixel.items.journalpages;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroClass;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class JournalPage extends Item {

	private static final String TXT_VALUE = "Journal Page";
	public int room=0;

	{
		name = "journal page";
		image = ItemSpriteSheet.JOURNAL_PAGE;

		stackable = false;
	}

	@Override
	public boolean doPickUp(Hero hero) {
         
		GLog.p("You found a page to Otiluke's Journal! It reads, %s.", TXT_VALUE);
		return super.doPickUp(hero);
	
	}

	@Override
	public String info() {
		return "A loose journal page labled Journal Page.";
	}
}
