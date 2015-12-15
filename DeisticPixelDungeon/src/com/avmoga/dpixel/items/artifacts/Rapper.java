package com.avmoga.dpixel.items.artifacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Silence;
import com.avmoga.dpixel.actors.hero.Belongings;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.effects.particles.ElmoParticle;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.weapon.missiles.MissileWeapon;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.windows.WndBag;
import com.avmoga.dpixel.windows.WndBag.Mode;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.avmoga.dpixel.scenes.CellSelector;

public class Rapper extends Artifact {
	{
			name = Dungeon.getMonth() == 11? "W. R. A. P. P. E. R." : "R. A. P. P. E. R.";
			image = ItemSpriteSheet.ARTIFACT_RAPPER;

			level = 0;
			levelCap = 6;

			charge = 0;
			defaultAction = AC_LAUNCH;
			knownWeps.clear();
		}

		private static HashSet<MissileWeapon> knownWeps = new HashSet<MissileWeapon>();
		private static boolean needsUpgrade;
		private static final String KNOWNWEPS = "knownweps";
		
		private static final String[] R1 = {"Rapid", "Ranged", "Richter's", "Rudabaga", "Rhubarb", "Rancid", "Righteous", "Rainbow", "Realistic", "Rolled-back", "Radical", "Rolling", "Rented", "Roped", "Reminder's", "Royal"};
		private static final String[] A = {"Active", "Aeronautic", "Alleviating", "Addled", "Antique", "Assonating", "Assaulting", "Adept", "Acrid", "Antimony", "Ambivalent", "Alucardian"};
		private static final String[] P1 = {"Projectile", "Powerful", "Potentially", "Peeking", "Pliable", "Plying", "Plushy", "Planting", "Proposed", "Pimped", "Plumbob", "Pushed"};
		private static final String[] P2 = {"Projectile", "Potent", "Pumping", "Poked", "Paul's", "Powerless", "Postmodernistic", "Precognital", "Post-mortem", "Planned", "Pink"};
		private static final String[] E = {"Euphoric", "Entry-level", "Ecch", "Euclid", "Ecclesiastic", "End-game", "Enthusiastic", "Egotistical", "Evan-wrecking", "Egging", "Eggnogging", "Early-game"};
		private static final String[] R2 = {"Rifle", "Ransacker", "Rhetoric", "Repeater", "Railgun", "Robot", "Rangefinder", "Rocketlauncher", "Radio"};
		private static final String AC_LAUNCH = "LAUNCH";
		private static final String AC_ADD1 = "ADD MISSILE";
		private static final String AC_ADDALL = "ADD MISSILE STACK";
		
		@Override
		protected ArtifactBuff passiveBuff() {
			return new Launcher();
		}
		
		private static int consumedPts = 0;
		private static int detachedItems = 0;
		public static MissileWeapon launchItem;
		@Override
		public String desc() {
			String desc = "You've found something odd: A long, fiberglass tube marked ";
			if(Dungeon.getMonth() == 11){
				desc += "W. R. A. P. P. E. R., encircled by a Christmas Wreath. It looks like it stands for Wreathed ";
			} else {
				desc += "R. A. P. P. E. R. It looks like it stands for ";
			}
			desc += R1[Random.Int(R1.length - 1)] + " " + A[Random.Int(A.length - 1)] + " " + P1[Random.Int(P1.length - 1)] + " " + P2[Random.Int(P2.length - 1)] + " " + E[Random.Int(E.length - 1)] + " " + R2[Random.Int(R2.length - 1)] 
					+ "\n\n Attached to it is a charge pack that it looks like was meant to store stabilized matter. "
					+ "Perhaps you can use this to duplicate and enhance your missile weapons?";
			if(isEquipped(Dungeon.hero)){
				desc += "\n\n";
			
				if(!cursed){
					desc += "Holding out the tube in front of you, you feel like you could take on an army! "
							+ "Let's hope that isn't just vanity.";
				} else {
					desc += "The R. A. P. P. E. R. is painfully stuck to your side, it looks like it is preventing you from using any ranged weapons.";
				}
			}
			return desc;
		}
		public void execute(Hero hero, String action) {
			super.execute(hero, action);
			if (action.equals(AC_LAUNCH)) {
				if (!isEquipped(hero))
					GLog.i("You need to equip it to do that.");
				else if (cursed)
					GLog.i("This item is cursed!");
				else if (!(charge > 0))
					GLog.i("There is not enough charge to do that.");
				else {
					GameScene.selectItem(knownWeps, select, Mode.SHOWMISSILE, "Select missile weapon to launch");
				}
				
			} else if (action.equals(AC_ADDALL)) {
				GameScene.selectItem(add, Mode.MISSILE, "Select missile stack to add");
			}
			else if (action.equals(AC_ADD1)){
				GameScene.selectItem(add1, Mode.MISSILE, "Select missile weapon to add");
			}
		}
		
		public ArrayList<String> actions(Hero hero) {
			ArrayList<String> actions = super.actions(hero);
			if (isEquipped(hero) && charge > 0 && !cursed)
				actions.add(AC_LAUNCH);
			if (isEquipped(hero) && level < levelCap && !cursed){
				actions.add(AC_ADD1);
				actions.add(AC_ADDALL);
			}
			return actions;
		}

		protected static CellSelector.Listener launch = new CellSelector.Listener() {
			
			@Override
			public String prompt() {
				return "Select direction to launch";
			}
			
			@Override
			public void onSelect(Integer cell) {
				try{
					final Artifact item= (Artifact) Item.curItem;
					MissileWeapon throwme = launchItem.getClass().newInstance();
					throwme.quantity(1);
					throwme.enhance(item.level);
					item.charge -= throwme.rapperValue;
					throwme.cast(Dungeon.hero, cell);
				} catch (Exception e) {
					return;
				}
			}
				
		}; 

		protected static WndBag.Listener add = new WndBag.Listener() {
			
			@Override
			public void onSelect(Item item) {
				Hero hero = Dungeon.hero;
				detachedItems = 0;
				boolean mustBeAdded = false;
				if(item != null && item instanceof MissileWeapon){
					if(((MissileWeapon) item).rapperValue == 0){
						GLog.i("You cannot add that item to this.");
						return;
					}
					for (MissileWeapon weapon : knownWeps){
						if(item.getClass() == weapon.getClass()){
							break;
						}
						mustBeAdded = true;
					}
					if(mustBeAdded){
						mustBeAdded = false;
						int returnTo = item.quantity();
						knownWeps.add((MissileWeapon) item);
						item.quantity(returnTo);
						needsUpgrade = true;
					}
					for(Item lookfor : hero.belongings.backpack){
						if(lookfor.getClass() == item.getClass()){
							while(hero.belongings.backpack.contains(lookfor)){
								consumedPts += ((MissileWeapon) lookfor).rapperValue;
								lookfor.detach(hero.belongings.backpack);
								detachedItems++;
							}
							break;
						}
					}
					hero.sprite.operate(hero.pos);
					hero.busy();
					hero.spend(2f);
					Sample.INSTANCE.play(Assets.SND_BURNING);
					hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
					GLog.i("Added the energy of " + detachedItems + " " +  item.name() + "s.");
					
				}
			}
		};
		
		protected static WndBag.Listener add1 = new WndBag.Listener() {
			
			@Override
			public void onSelect(Item item) {
				Hero hero = Dungeon.hero;
				detachedItems = 0;
				boolean mustBeAdded = false;
				if(item != null && item instanceof MissileWeapon){
					if(((MissileWeapon) item).rapperValue == 0){
						GLog.i("You cannot add that item to this.");
						return;
					}
					for (MissileWeapon weapon : knownWeps){
						if(item.getClass() == weapon.getClass()){
							break;
						}
						mustBeAdded = true;
					}
					if(mustBeAdded){
						mustBeAdded = false;
						int returnTo = item.quantity();
						knownWeps.add((MissileWeapon) item);
						item.quantity(returnTo);
						needsUpgrade = true;
					}
					for(Item lookfor : hero.belongings.backpack){
						if(lookfor.getClass() == item.getClass()){
							consumedPts += ((MissileWeapon) lookfor).rapperValue;
							lookfor.detach(hero.belongings.backpack);
							detachedItems++;
							break;
						}
					}
					hero.sprite.operate(hero.pos);
					hero.busy();
					hero.spend(1f);
					Sample.INSTANCE.play(Assets.SND_BURNING);
					hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
					GLog.i("Added a  " + item.name() + "'s energy.");
					
				}
			}
		};
		
		protected static WndBag.Listener select = new WndBag.Listener() {
			
			@Override
			public void onSelect(Item item) {
				if(item != null){
					final Artifact item2= (Artifact) Item.curItem;
						if(item2.charge >= ((MissileWeapon) item).rapperValue){
							launchItem = (MissileWeapon) item;
							GameScene.selectCell(launch);				
					} else {
						GLog.i("There was not enough charge to create the weapon.");
					}
				}
			}
		};
		
		public class Launcher extends ArtifactBuff {
			public boolean act() {
				if(this.isCursed() && isEquipped(Dungeon.hero)){
					Buff.affect(curUser, Silence.class, 1);
				}
				if(needsUpgrade && level < levelCap){
					needsUpgrade = false;
					upgrade();
				}
				charge += consumedPts;
				consumedPts = 0;
				updateQuickslot();

				spend(TICK);

				return true;
			}

		}
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(KNOWNWEPS, knownWeps);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			knownWeps.clear();
			knownWeps.addAll((Collection<? extends MissileWeapon>) bundle.getCollection(KNOWNWEPS));
		}
	}
