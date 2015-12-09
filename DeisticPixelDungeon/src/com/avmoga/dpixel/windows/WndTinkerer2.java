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
import com.avmoga.dpixel.actors.mobs.npcs.Tinkerer2;
import com.avmoga.dpixel.actors.mobs.npcs.Wandmaker;
import com.avmoga.dpixel.items.ActiveMrDestructo;
import com.avmoga.dpixel.items.ActiveMrDestructo2;
import com.avmoga.dpixel.items.DewVial;
import com.avmoga.dpixel.items.DewVial2;
import com.avmoga.dpixel.items.InactiveMrDestructo;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Mushroom;
import com.avmoga.dpixel.items.SanChikarah;
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

public class WndTinkerer2 extends Window {

	private static final String TXT_MESSAGE1 = "Thanks for the Toadstool Mushroom! "
			                                  +"In return, I can upgrade your mr destructo for you, "
			                                  +"or I can recharge your old one and give you another. ";
	
	private static final String TXT_MESSAGE2 = "Thanks for the Toadstool Mushroom! "
                                               +"In return, I can upgrade your mr destructo for you. "
                                               +"You can also have this other one I've managed to fix up. ";
	
	private static final String TXT_MESSAGE3 = "Thanks for the Toadstool Mushroom! "
                                               +"In return, you can have this Mr Destructo if you like. ";

	private static final String TXT_UPGRADE = "Upgrade my Mr Destructo";
	private static final String TXT_RECHARGE = "Recharge my Mr Desructo";
	private static final String TXT_NEW = "I'll take the new Mr Desructo";

	private static final String TXT_FARAWELL = "Good luck in your quest, %s!";

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;
	
	
	public WndTinkerer2(final Tinkerer2 tinkerer, final Item item, final Item mrd) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		if (mrd instanceof InactiveMrDestructo) {


			BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE1, 6);
			message.maxWidth = WIDTH;
			message.measure();
			message.y = titlebar.bottom() + GAP;
			add(message);

			RedButton btnUpgrade = new RedButton(TXT_UPGRADE) {
				@Override
				protected void onClick() {
					selectUpgrade(tinkerer);
				}
			};
			btnUpgrade.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
			add(btnUpgrade);

			RedButton btnRecharge = new RedButton(TXT_RECHARGE) {
				@Override
				protected void onClick() {
					selectRecharge(tinkerer);
				}
			};
			btnRecharge.setRect(0, btnUpgrade.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(btnRecharge);

			resize(WIDTH, (int) btnRecharge.bottom());
			

		} else if (mrd instanceof ActiveMrDestructo) {
			BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE2, 6);
			message.maxWidth = WIDTH;
			message.measure();
			message.y = titlebar.bottom() + GAP;
			add(message);

			RedButton btnUpgrade = new RedButton(TXT_UPGRADE) {
				@Override
				protected void onClick() {
					selectUpgradePlus(tinkerer);
				}
			};
			btnUpgrade.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
			add(btnUpgrade);

			resize(WIDTH, (int) btnUpgrade.bottom());
			
		} else {
			BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE3, 6);
			message.maxWidth = WIDTH;
			message.measure();
			message.y = titlebar.bottom() + GAP;
			add(message);

			RedButton btnNew = new RedButton(TXT_NEW) {
				@Override
				protected void onClick() {
					selectNew(tinkerer);
				}
			};
			btnNew.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
			add(btnNew);
			
			resize(WIDTH, (int) btnNew.bottom());
		}

	}
	
	
	

	private void selectUpgrade(Tinkerer2 tinkerer) {

		hide();
		
		Mushroom mushroom = Dungeon.hero.belongings.getItem(Mushroom.class);
		mushroom.detach(Dungeon.hero.belongings.backpack);
		
		InactiveMrDestructo inmrd = Dungeon.hero.belongings.getItem(InactiveMrDestructo.class);
		inmrd.detach(Dungeon.hero.belongings.backpack);
		
		 ActiveMrDestructo2 mrd2 = new ActiveMrDestructo2();	
			if (mrd2.doPickUp(Dungeon.hero)) {
				GLog.i(Hero.TXT_YOU_NOW_HAVE, mrd2.name());
			} else {
				Dungeon.level.drop(mrd2, Dungeon.hero.pos).sprite.drop();
			}
		
		tinkerer.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.givenName()));
		tinkerer.destroy();

		tinkerer.sprite.die();

		//Wandmaker.Quest.complete();
	}
	
	private void selectUpgradePlus(Tinkerer2 tinkerer) {

		hide();
		
		Mushroom mushroom = Dungeon.hero.belongings.getItem(Mushroom.class);
		mushroom.detach(Dungeon.hero.belongings.backpack);		
		
		 ActiveMrDestructo2 mrd2 = new ActiveMrDestructo2();	
			if (mrd2.doPickUp(Dungeon.hero)) {
				GLog.i(Hero.TXT_YOU_NOW_HAVE, mrd2.name());
			} else {
				Dungeon.level.drop(mrd2, Dungeon.hero.pos).sprite.drop();
			}
		
		tinkerer.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.givenName()));
		tinkerer.destroy();

		tinkerer.sprite.die();

		//Wandmaker.Quest.complete();
	}
	
	private void selectRecharge(Tinkerer2 tinkerer) {

		hide();
		
		Mushroom mushroom = Dungeon.hero.belongings.getItem(Mushroom.class);
		mushroom.detach(Dungeon.hero.belongings.backpack);
		
		InactiveMrDestructo inmrd = Dungeon.hero.belongings.getItem(InactiveMrDestructo.class);
		inmrd.detach(Dungeon.hero.belongings.backpack);
		
		 ActiveMrDestructo mrd = new ActiveMrDestructo();	
			if (mrd.doPickUp(Dungeon.hero)) {
				GLog.i(Hero.TXT_YOU_NOW_HAVE, mrd.name());
			} else {
				Dungeon.level.drop(mrd, Dungeon.hero.pos).sprite.drop();
			}
			
		ActiveMrDestructo mrds = new ActiveMrDestructo();	
				if (mrds.doPickUp(Dungeon.hero)) {
					GLog.i(Hero.TXT_YOU_NOW_HAVE, mrds.name());
				} else {
					Dungeon.level.drop(mrds, Dungeon.hero.pos).sprite.drop();
				}
		
		tinkerer.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.givenName()));
		tinkerer.destroy();

		tinkerer.sprite.die();

		//Wandmaker.Quest.complete();
	}
	
	private void selectNew(Tinkerer2 tinkerer) {

		hide();
		
		Mushroom mushroom = Dungeon.hero.belongings.getItem(Mushroom.class);
		mushroom.detach(Dungeon.hero.belongings.backpack);
		
		 ActiveMrDestructo mrd = new ActiveMrDestructo();	
			if (mrd.doPickUp(Dungeon.hero)) {
				GLog.i(Hero.TXT_YOU_NOW_HAVE, mrd.name());
			} else {
				Dungeon.level.drop(mrd, Dungeon.hero.pos).sprite.drop();
			}
			
		tinkerer.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.givenName()));
		tinkerer.destroy();

		tinkerer.sprite.die();

		//Wandmaker.Quest.complete();
	}
	
}
