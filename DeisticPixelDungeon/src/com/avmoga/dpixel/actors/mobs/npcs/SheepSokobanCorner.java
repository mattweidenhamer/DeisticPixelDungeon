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
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.Item;
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
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.PrisonLevel;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.plants.Plant;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.sprites.SheepSprite;
import com.avmoga.dpixel.sprites.SokobanCornerSheepSprite;
import com.avmoga.dpixel.sprites.WandmakerSprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.avmoga.dpixel.windows.WndQuest;
import com.avmoga.dpixel.windows.WndWandmaker;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SheepSokobanCorner extends NPC {

	private static final String[] QUOTES = { "Baa!", "Baa?", "Baa.",
	"Baa..." };

{
name = "sheep";
spriteClass = SokobanCornerSheepSprite.class;
}


@Override
protected boolean act() {
	throwItem();
	return super.act();
}

@Override
public void damage(int dmg, Object src) {
}

@Override
public String description() {
return "This is a magic sheep. What's so magical about it? You can't kill it. "
		+ "You could probably push it out of the way though.";
}

/*  -W-1 -W  -W+1
 *  -1    P  +1
 *  W-1   W  W+1
 * 
 */

@Override
public void interact() {
	int curPos = pos;
	int movPos = pos;
	int width = Level.getWidth();
    boolean moved = false;
	int posDif = Dungeon.hero.pos-curPos;
	
	if (posDif==1) {
		movPos = curPos-1;
	} else if(posDif==-1) {
		movPos = curPos+1;
	} else if(posDif==width) {
		movPos = curPos-width;
	} else if(posDif==-width) {
		movPos = curPos+width;
	} 
	
	else if(posDif==-width+1) {
		movPos = curPos+width-1;
		
	} else if(posDif==-width-1) {
		movPos = curPos+width+1;
		
	} else if(posDif==width+1) {
		movPos = curPos-(width+1);
		
	} else if(posDif==width-1) {
		movPos = curPos-(width-1);
	}    
	
	if (movPos != pos && Level.passable[movPos] && Actor.findChar(movPos) == null){
		
		moveSprite(curPos,movPos);
		move(movPos);
		moved=true;
			
	} 
	
	if(moved){
      Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
	  Dungeon.hero.move(curPos);
	}
	
    yell(Random.element(QUOTES));
    
}

}
