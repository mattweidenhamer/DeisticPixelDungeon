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
import com.avmoga.dpixel.Statistics;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Dewcharge;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.npcs.Blacksmith;
import com.avmoga.dpixel.actors.mobs.npcs.Tinkerer1;
import com.avmoga.dpixel.actors.mobs.npcs.Wandmaker;
import com.avmoga.dpixel.actors.mobs.pets.PET;
import com.avmoga.dpixel.items.DewVial;
import com.avmoga.dpixel.items.DewVial2;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Mushroom;
import com.avmoga.dpixel.items.SanChikarahDeath;
import com.avmoga.dpixel.items.quest.Pickaxe;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.levels.Level;
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

public class WndOtiluke extends Window {

	
	private static final String TXT_DRAW_INFO = "Tell me more about Draw Out Dew";

	private static final String TXT_FARAWELL = "Good luck in your quest, %s!";
	private static final String TXT_FARAWELL_DRAW = "Good luck in your quest, %s! I'll give you a head start drawing out dew!";

    
	private static final int PAGES = 10;
	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndOtiluke(final boolean[] rooms, final Item item) {

		super();
		
		String[] roomNames = new String[PAGES];
		roomNames[0] = "Safe Room";
	
		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		BitmapTextMultiline message = PixelScene.createMultiline(TXT_FARAWELL, 6);
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add(message);
		
		//add each button
		  //after n*BTN_HEIGHT+GAP
		//add port function
		
		RedButton btn1 = new RedButton(roomNames[0]) {
			@Override
			protected void onClick() {
				port(0);
			}
		};
		btn1.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
		add(btn1);
		resize(WIDTH, (int) btn1.bottom());
		
		int buttons=1;
		
		for (int i=1; i<PAGES; i++) {	
			final int portnum=i;
			if (rooms[i]){
				buttons++;
				RedButton btn = new RedButton(roomNames[i]) {
					@Override
					protected void onClick() {
						port(portnum);
					}
				};
				btn.setRect(0, buttons*BTN_HEIGHT + GAP, WIDTH, BTN_HEIGHT);
				add(btn);
				resize(WIDTH, (int) btn.bottom());
			}
		}		
	}

	
	public void port(Integer room){
		
	}
	
}
