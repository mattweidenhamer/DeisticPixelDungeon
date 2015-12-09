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
package com.avmoga.dpixel.items.weapon.melee;

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.quest.DarkGold;
import com.avmoga.dpixel.items.scrolls.ScrollOfUpgrade;
import com.avmoga.dpixel.items.weapon.missiles.Boomerang;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ShortSword extends MeleeWeapon {

	public static final String AC_REFORGE = "REFORGE";

	private static final String TXT_SELECT_WEAPON = "Select a weapon to upgrade";

	private static final String TXT_REFORGED = "you reforged the short sword to upgrade your %s";
	private static final String TXT_NOT_BOOMERANG = "you can't upgrade a boomerang this way";

	private static final float TIME_TO_REFORGE = 2f;

	private boolean equipped;
	
	private float upgradeChance = 0.5f;

	{
		name = "short sword";
		image = ItemSpriteSheet.SHORT_SWORD;

		bones = false;
	}

	public ShortSword() {
		super(1, 1f, 1f);

		STR = 11;
		MAX = 12;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (level > 0) {
			actions.add(AC_REFORGE);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_REFORGE) {

			if (hero.belongings.weapon == this) {
				equipped = true;
				hero.belongings.weapon = null;
			} else {
				equipped = false;
				detach(hero.belongings.backpack);
			}

			curUser = hero;

			GameScene.selectItem(itemSelector, WndBag.Mode.WEAPON,
					TXT_SELECT_WEAPON);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public String desc() {
		return "It is indeed quite short, just a few inches longer, than a dagger.";
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
			if (gold!=null){
			upgradeChance = (upgradeChance + (gold.quantity()*0.01f));
			}
			if (item != null && !(item instanceof Boomerang)) {
                int i=0;
				while(i<level) {
					if (i<2){
					  Sample.INSTANCE.play(Assets.SND_EVOKE);
					  ScrollOfUpgrade.upgrade(curUser);
					  evoke(curUser);
					  item.upgrade();
					} else if (Random.Float()<upgradeChance){
						if (item.level<15 || item.reinforced){
				            Sample.INSTANCE.play(Assets.SND_EVOKE);
				            ScrollOfUpgrade.upgrade(curUser);
				            evoke(curUser);
				            item.upgrade();
				            upgradeChance = Math.max(0.5f, upgradeChance-0.1f);
						 } else {
							 GLog.w("%s is not strong enough to recieve anymore upgrades!", item.name());
							 i=level;
						 }
				  }
				i++;
				}
							
				curUser.spendAndNext(TIME_TO_REFORGE);

				GLog.w(TXT_REFORGED, item.name());
				Badges.validateItemLevelAquired(item);

			} else {

				if (item instanceof Boomerang) {
					GLog.w(TXT_NOT_BOOMERANG);
				}

				if (equipped) {
					curUser.belongings.weapon = ShortSword.this;
				} else {
					collect(curUser.belongings.backpack);
				}
			}
		}
	};
}
