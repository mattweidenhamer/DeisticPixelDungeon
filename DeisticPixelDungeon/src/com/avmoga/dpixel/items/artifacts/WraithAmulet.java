package com.avmoga.dpixel.items.artifacts;

import java.util.ArrayList;
import java.util.HashSet;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.hero.HeroRace;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Effects;
import com.avmoga.dpixel.effects.particles.ShadowParticle;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.ui.QuickSlotButton;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.wands.Wand;
import com.avmoga.dpixel.items.wands.WandOfBlink;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.scenes.CellSelector;
import com.avmoga.dpixel.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WraithAmulet extends Artifact {
	
	{
		name = "Wraithmetal Amulet";
		image = ItemSpriteSheet.ARTIFACT_WAMULET;
		
		charge = ((level / 2) +3 );
		partialCharge = 0;
		chargeCap = (((level + 1) / 2) + 3);
		level = 0;
		levelCap = 10;
		
		defaultAction = AC_GHOST;
	}
	
	private static final String TXT_SELFSELECT = "You cannot target yourself.";
	private static final String TXT_NOCHARGE = "This item must be charged to do that.";
	private static final String TXT_NOEQUIP = "This item must be equipped to do that.";
	private static final String AC_GHOST = "INSUBSTANTIATE";
	private static final String AC_ASSASSINATE = "ASSASSINATE";
	private static final String TXT_FAR = "It is too far away.";
	private static final String TXT_GHOST = "Your essense becomes scattered...";
	private static final String TXT_NOTHING_THERE = "There is nothing there to kill.";
	
	public Item upgrade(){
		chargeCap = (((level + 1) / 2) + 3);
		return super.upgrade();
	}
	
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if(action.equals(AC_GHOST)){
			if(this.isEquipped(Dungeon.hero)){
				if(this.charge > 0){
					Buff.affect(Dungeon.hero, Invisibility.class, Invisibility.DURATION);
					GLog.i(TXT_GHOST);
				} else {
					GLog.i(TXT_NOCHARGE);
				}
			} else {
				GLog.i(TXT_NOEQUIP);
			}
		} else if (action.equals(AC_ASSASSINATE)) {
			if(this.charge > 0){
				GameScene.selectCell(porter);
				charge--;
			} else {
				GLog.i(TXT_NOCHARGE);
			}
		}
	}
	
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge > 0 && Dungeon.hero.heroRace == HeroRace.WRAITH)
			actions.add(AC_GHOST);
		if (isEquipped(hero) && level >= 2 && charge > 3 && Dungeon.hero.heroRace == HeroRace.WRAITH)
			actions.add(AC_ASSASSINATE);
		return actions;
	}
	
	protected ArtifactBuff passiveBuff() {
		return new WraithRecharge();
	}
	
	public class WraithRecharge extends ArtifactBuff{
		public boolean act() {
			if (charge < chargeCap && !cursed) {
				partialCharge += 1 / (150f - (chargeCap - charge) * 15f);

				if (partialCharge >= 1) {
					partialCharge--;
					charge++;

					if (charge == chargeCap) {
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();
			spend(TICK);
			return true;
		}
	}

	public String desc() {
		String desc = "You have made a very interesting find: A piece of silver, transparent metal with "
				+ "a purple gem encrusted within.\n\n";
		if(Dungeon.hero.heroRace() == HeroRace.HUMAN){
			desc += "As you touch it, you notice that your hands have begun to fade and are now "
					+ "incorporeal. A wraith would probably be able to make better use of "
					+ "this, but you should be able to figure it out well enough to use some basic "
					+ "functionalities. You just hope there aren't any negative effects that come in tow.";
		} else if(Dungeon.hero.heroRace() == HeroRace.WRAITH){
			desc += "You are entirely taken aback; This is an amulet of Torivorian Steel! "
					+ "You haven't seen anything like this since you left your "
					+ "homeworld. As your rub your finger along it, a chill runs down your "
					+ "back. This will be very helpful indeed.";
		}
		else{
			desc += "As you touch it, you feel a slight chill running down your back. Whatever this is, "
					+ "there is a powerful and evil magic that runs through it. It would probably be dangerous "
					+ "to use it without knowing how, so you decide you should try and transmute it into something "
					+ "more useful.";
		}
		
		if(isEquipped(Dungeon.hero) && (Dungeon.hero.heroRace() == HeroRace.WRAITH ||  cursed)){
			desc += "\n\n";
			if(cursed){
				desc += "The amulet is reaching into your mind, it makes known to you things beyond your "
						+ "comprehension.";
			} else if(level < 5 || Dungeon.hero.heroRace() == HeroRace.HUMAN){
				desc += "It seems like you could push your will over the object to become more wraith-like. You could probably "
						+ "amplify the experience with more... test subjects.";
			} else if (level < 10){
				desc += "It seems that existant matter when you reapparate from Wraith Form is scattered and becomes "
						+ "nonexistant. With a surprise attack, maybe you could damage with it.";
			} else {
				desc += "You are exerting your mastery over the amulet to great effect. Nothing dare stand "
						+ "against you now." ;
			}
		}
		
		return desc;
	}
	public int getCharge(){
		return this.charge;
	}
	protected static CellSelector.Listener porter = new CellSelector.Listener() {//TODO Get me working
		public String prompt() {
			return "Choose direction to teleport";
		}
		@Override
		public void onSelect(Integer target) {
			HashSet<Mob> victim = new HashSet<Mob>();
			if (target != null && (Level.passable[target])) {
				final WraithAmulet amulet = (WraithAmulet) Item.curItem;
				amulet.charge--;
				if(Actor.findChar(target) != null){
					victim.add((Mob) Actor.findChar(target));
				}
				
				if (target == curUser.pos) {
					GLog.i(TXT_SELFSELECT);
					return;
				}

				QuickSlotButton.target(Actor.findChar(target));
					if(Actor.findChar(target) != null){
							if(Level.distance(Dungeon.hero.pos, target) == 2){
								if(!Level.fieldOfView[target]){
									Actor.findChar(target).damage(Actor.findChar(target).HT, WraithAmulet.class);
									Dungeon.hero.pos = target;
									Dungeon.hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
									WandOfBlink.appear(Dungeon.hero, target);
									Dungeon.level.press(target, Dungeon.hero);
									Dungeon.observe();
								} else {
									GLog.i(TXT_FAR);
								}
							} else {
								GLog.i(TXT_FAR);
							}
						} else {
							GLog.i(TXT_NOTHING_THERE);
						}
					}
			}
	};

}
