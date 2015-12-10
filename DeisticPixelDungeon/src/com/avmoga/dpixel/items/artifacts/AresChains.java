package com.avmoga.dpixel.items.artifacts;

import java.util.ArrayList;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Bleeding;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Cripple;
import com.avmoga.dpixel.actors.buffs.Paralysis;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroRace;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.Chain;
import com.avmoga.dpixel.effects.Pushing;
import com.avmoga.dpixel.effects.particles.FlameParticle;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.scenes.CellSelector;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Callback;

public class AresChains extends Artifact {
	{
		name = "Chains of Ares";
		image = ItemSpriteSheet.ARTIFACT_SHIELD;
		
		level = 0;
		levelCap = 4;
		defaultAction = AC_CHAIN;
		
		charge = ((level * 2) +3 );
		partialCharge = 0;
		chargeCap = (((level + 1) / 2) + 3);
		//Level Progression: 0, 2, 5, 7, 10
	}
	public static int mobsSinceUpgrade = 0;
	private static final float TIMETOUSE = 1f;
	private static final String AC_CHAIN = "CHAIN";
	private static final String AC_PULL = "PULL";
	private static final String YELL = "I HAVE YOU NOW!";
	private static final String BUFF = "buff";
	private static final String TXT_FAIL = "but there was nobody to pull...";
	private static final String TXT_NO_CHARGE = "But there was no charge...";
	private static final String TXT_SELF_TARGET = "you can't target yourself.";
	
	public void castChain(int cell){

		Char ch = Actor.findChar(cell);
		if (ch != null) {
			curUser.sprite.parent.add(new Chain(Dungeon.hero.pos, ch.pos, null));
			Dungeon.hero.busy();
			Buff.prolong(ch, Cripple.class, Random.Int(3 + level));
			Buff.affect(ch, Bleeding.class).set(3);
			ch.damage(Random.Int(1, 10), this);
			charge--;
			ch.sprite.emitter().burst(FlameParticle.FACTORY, 5);
			Dungeon.hero.spendAndNext(TIMETOUSE);
		}
	}
	
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {
			if (activeBuff != null) {
				activeBuff.detach();
				activeBuff = null;
			}
			return true;
		} else
			return false;
	}
	
	protected static CellSelector.Listener chain = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {
			final AresChains chains = (AresChains) Item.curItem;
			if (target != null) {

				final int cell = Ballistica.cast(curUser.pos, target, true,	true);
				
				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i(TXT_SELF_TARGET);
					return;
				}
				chains.castChain(cell);

			}
		}

		@Override
		public String prompt() {
			return "Choose direction to chain";
		}
	};
	
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if(isEquipped(hero) && !cursed){
			if ((hero.heroRace() == HeroRace.DWARF || hero.heroRace() == HeroRace.HUMAN))
				actions.add(AC_CHAIN);
			
			if (hero.heroRace() == HeroRace.DWARF && level >= 2)
					actions.add(AC_PULL);
		}
		return actions;
	}
	
	protected boolean useableBasic(){
		if(Dungeon.hero.heroRace() == HeroRace.HUMAN || Dungeon.hero.heroRace() == HeroRace.DWARF){
			return true;
		}
		return false;
	}
	
	protected boolean useable(){
		if(Dungeon.hero.heroRace() == HeroRace.DWARF){
			return true;
		}
		return false;
	}
	
	protected ArtifactBuff passiveBuff(){
		return new chainsRecharge();
	}
	
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_CHAIN)) {
			if (!isEquipped(hero))
				GLog.i("You need to equip this to do that.");
			else if(cursed)
				GLog.i("The chains will not obey your will!");
			else if(!useableBasic()){
				GLog.i("What are you trying to do?");
			}
			else {
				GameScene.selectCell(chain);
				}
		} else if (action.equals(AC_PULL) && useable()) {
			Dungeon.hero.checkVisibleMobs();
			
			if(charge > 0 && (Dungeon.hero.visibleEnemies()) > 0){
				Dungeon.hero.yell(YELL);
				for (final Mob mob : Dungeon.level.mobs) {
					if(Level.fieldOfView[mob.pos]){
						for(int space : Level.NEIGHBOURS8){
							final int newSpace = Dungeon.hero.pos + space;
							if((Actor.findChar(newSpace) == null) && (charge > 0) && (Level.passable[newSpace] ||Level.avoid[newSpace])){
								curUser.busy();
								curUser.sprite.parent.add(new Chain(curUser.pos, mob.pos, new Callback() {
									public void call() {
										Actor.add(new Pushing(mob, mob.pos, newSpace));
										mob.pos = newSpace;
										Dungeon.observe();
										curUser.spendAndNext(1f);
										Dungeon.level.mobPress(mob);
										GLog.i("Pulled a " + mob.name + "!");
										mob.damage(Random.Int(mob.HT / 10, mob.HT / 4), this);
										if (!mob.immunities().contains(Paralysis.class)){
											Buff.prolong(mob, Paralysis.class, Paralysis.duration(mob));
										}
									}
								}));
								charge--;
								break;
							} 
						}
					}
					if(!(charge > 0)){
						break;
					}
				}
				Dungeon.observe();
				Dungeon.hero.spendAndNext(TIME_TO_THROW);
			} else if(!(charge > 0)){
				GLog.i(TXT_NO_CHARGE);
			} else if(!(Dungeon.hero.visibleEnemies() > 0)){
				GLog.i(TXT_FAIL);
			}
		}
	}
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		if (activeBuff != null)
			bundle.put(BUFF, activeBuff);
	}
	
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		
		if (bundle.contains(BUFF)) {
			Bundle buffBundle = bundle.getBundle(BUFF);

			activeBuff.restoreFromBundle(buffBundle);
		}
	}
	public String desc(){
		String desc = "You've made quite an interesting find: A shield with the emblem of Ares, the God " + 
	"of War. \n\n";
		if(Dungeon.hero.heroRace() == HeroRace.DWARF){
			desc += "The shield immediately seems to take a liking to you; it grabs onto your arm and "
					+ "almost refuses to let go. It looks like you could use it to hinder enemies, and, "
					+ "with a bit more training, pull numerous enemies to you.";
		} else if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
			desc += "You notice that the chains attached to the shield seem to jump to the legs of nearby "
					+ "enemies; perhaps you could throw them at an enemy to hinder them. You're sure the "
					+ "warlike Drawves could make a much better use of it.";
		} else {
			desc += "The chains are too heavy to be used effectively, however. Since the chain and shield "
					+ "are intertwined, you decide that it is too heavy to be of any use. You should probably "
					+ "transmute it for a better item.";
		}
		if(cursed && isEquipped(Dungeon.hero)){
			desc += "\n\nThe Shield of Ares is bound to your arm, the chains are floating dangerously "
					+ "close to your legs. You feel a cruel spirit in the shield.";
		}
		return desc;
	}
	public class chainsRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {
				partialCharge += 1 / (45f - (chargeCap - charge) * 2f);

				if (partialCharge >= 1) {
					partialCharge--;
					charge++;

					if (charge == chargeCap) {
						partialCharge = 0;
					}
				}
				
			} else if (cursed && Random.Int(10) == 0)
				((Hero) target).spend(TICK);

			updateQuickslot();

			spend(TICK);

			return true;
		}
		public void gainEXP(float partialEXP){
			if (cursed) return;
			
			exp += Math.round(partialEXP);
			
			if(Random.Int(exp, 10000) > 7000){
				exp = 0;
				upgrade();
			}
		}
	}
}
