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

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.npcs.Imp;
import com.avmoga.dpixel.items.DewVial;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.quest.DwarfToken;
import com.avmoga.dpixel.scenes.PixelScene;
import com.avmoga.dpixel.sprites.ItemSprite;
import com.avmoga.dpixel.ui.RedButton;
import com.avmoga.dpixel.ui.Window;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;

public class WndDescend extends Window {

	private static final String TXT_MESSAGE = "Are you sure you want to descend? "
                                             +"Any dew on this level will evaporate and your dew charge might be wasted. "
			                                 +"If you really want to descend click the button below then try the stairs again. ";
	private static final String TXT_REWARD = "Descend";

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;

	public WndDescend() {

		super();
		
		DewVial dewvial = new DewVial();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(dewvial.image(), null));
		titlebar.label(Utils.capitalize(dewvial.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		BitmapTextMultiline message = PixelScene
				.createMultiline(TXT_MESSAGE, 6);
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add(message);

		RedButton btnReward = new RedButton(TXT_REWARD) {
			@Override
			protected void onClick() {
				Dungeon.level.forcedone=true;
				hide();
			}
		};
		btnReward.setRect(0, message.y + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnReward);

		resize(WIDTH, (int) btnReward.bottom());
	}

	
}
