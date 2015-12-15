package com.avmoga.dpixel.items;

import java.util.ArrayList;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.Statistics;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.items.artifacts.DriedRose;
import com.avmoga.dpixel.items.artifacts.TimekeepersHourglass;
import com.avmoga.dpixel.items.weapon.missiles.ForestDart;
import com.avmoga.dpixel.scenes.InterlevelScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.sprites.ItemSprite.Glowing;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class MegaKey extends Item {
	
	private static final String TXT_PREVENTING = "Strong magic aura of this place prevents you from using the ancient key!";
		
	
	public static final float TIME_TO_USE = 1;

	public static final String AC_PORT = "DESCEND";
	public static final String AC_PORTUP = "ASCEND";

	private int specialLevel = 22;
	private int returnDepth = -1;
	private int returnPos;

	{
		name = "key of debugging";
		image = ItemSpriteSheet.ANCIENTKEY;

		stackable = false;
		unique = true;
	}
	
	private static final String DEPTH = "depth";
	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_PORT);
		actions.add(AC_PORTUP);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_PORT) {			
			if (Dungeon.depth>25 && Dungeon.depth!=specialLevel) {
				hero.spend(TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}


		}

		if (action == AC_PORT) {

				Buff buff = Dungeon.hero
						.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null)
					buff.detach();

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero)
						mob.destroy();
            	returnDepth = Dungeon.depth;
       			returnPos = hero.pos;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

               
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
					
		} else if(action == AC_PORTUP) {

					InterlevelScene.mode = InterlevelScene.Mode.ASCEND;	
	               
					InterlevelScene.returnDepth = returnDepth;
					InterlevelScene.returnPos = returnPos;
					Game.switchScene(InterlevelScene.class);
		} else {

			super.execute(hero, action);

		}
	}
	
	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}


	private static final Glowing GREEN = new Glowing(0x00FF00);
	
	@Override
	public Glowing glowing() {
		return GREEN;
	}

	@Override
	public String info() {
		return "Forged by the Gods themselves.";
	}
}
