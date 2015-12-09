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
package com.avmoga.dpixel.windows;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Chrome;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.npcs.Blacksmith;
import com.avmoga.dpixel.actors.mobs.npcs.Tinkerer1;
import com.avmoga.dpixel.actors.mobs.npcs.Wandmaker;
import com.avmoga.dpixel.items.DewVial;
import com.avmoga.dpixel.items.DewVial2;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Mushroom;
import com.avmoga.dpixel.items.SanChikarahDeath;
import com.avmoga.dpixel.items.quest.Pickaxe;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.scenes.PixelScene;
import com.avmoga.dpixel.sprites.ItemSprite;
import com.avmoga.dpixel.ui.ItemSlot;
import com.avmoga.dpixel.ui.RedButton;
import com.avmoga.dpixel.ui.Window;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndDewVial extends Window {
	
	//if people don't get it after this, I quit. I just quit.

	private static final String TXT_MESSAGE = "The dew vial is much more powerful in this dungeon. "
			                                  +"Filling it all the way will allow you to bless and upgrade your gear. "
			                                  +"The deeper you go in the dungeon, the more the dew will upgrade your gear. "
			                                  +"You can bless your items to +6 or +7 by the time you get to the Goo."; 
	
	private static final String TXT_WATER = "Okay! Let's go find 100 dew drops!";


	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndDewVial(final Item item) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		BitmapTextMultiline message = PixelScene
				.createMultiline(TXT_MESSAGE, 6);
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add(message);

		RedButton btnBattle = new RedButton(TXT_WATER) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnBattle.setRect(0, message.y + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBattle);

		

		resize(WIDTH, (int) btnBattle.bottom());
	}

	
}
