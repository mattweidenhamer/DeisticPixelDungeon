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
package com.avmoga.dpixel.actors.mobs.npcs;

import java.util.ArrayList;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.Journal;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.Blob;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Roots;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.items.DewVial;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.Mushroom;
import com.avmoga.dpixel.items.potions.PotionOfStrength;
import com.avmoga.dpixel.items.quest.CorpseDust;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.items.wands.WandOfAmok;
import com.avmoga.dpixel.items.wands.WandOfAvalanche;
import com.avmoga.dpixel.items.wands.WandOfBlink;
import com.avmoga.dpixel.items.wands.WandOfDisintegration;
import com.avmoga.dpixel.items.wands.WandOfFirebolt;
import com.avmoga.dpixel.items.wands.WandOfLightning;
import com.avmoga.dpixel.items.wands.WandOfPoison;
import com.avmoga.dpixel.items.wands.WandOfRegrowth;
import com.avmoga.dpixel.items.wands.WandOfSlowness;
import com.avmoga.dpixel.items.wands.WandOfTelekinesis;
import com.avmoga.dpixel.levels.PrisonLevel;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.plants.Plant;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.sprites.TinkererSprite;
import com.avmoga.dpixel.sprites.WandmakerSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.avmoga.dpixel.windows.WndQuest;
import com.avmoga.dpixel.windows.WndTinkerer;
import com.avmoga.dpixel.windows.WndTinkerer3;
import com.avmoga.dpixel.windows.WndWandmaker;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Tinkerer3 extends NPC {

	{
		name = "tinkerer";
		spriteClass = TinkererSprite.class;
	}

	private static final String TXT_DUNGEON = "I'm scavenging for toadstool mushrooms. "
			+ "Could you bring me any toadstool mushrooms you find? ";
	
	
	private static final String TXT_DUNGEON2 = "Oh wow, have you seen this dungeon! This is an awesome dungeon.  ";

	private static final String TXT_MUSH = "Any luck finding toadstool mushrooms, %s?";

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return "absorbed";
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public void interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		Item item = Dungeon.hero.belongings.getItem(Mushroom.class);
		Item vial = Dungeon.hero.belongings.getItem(DewVial.class);
			if (item != null && vial != null) {
				GameScene.show(new WndTinkerer3(this, item));
			} else if (item == null && vial != null) {
				tell(TXT_DUNGEON);
			} else {
				tell(TXT_DUNGEON2);
			}
		
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	@Override
	public String description() {
		return "The tinkerer is protected by a magical shield. ";
	}

}
