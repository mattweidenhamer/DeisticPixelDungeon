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
package com.avmoga.dpixel.items.scrolls;

import java.util.ArrayList;
import java.util.HashSet;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Invisibility;
import com.avmoga.dpixel.actors.buffs.Strength;
import com.avmoga.dpixel.actors.hero.Hero;
import com.avmoga.dpixel.actors.mobs.Mob;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.traps.LightningTrap;
import com.avmoga.dpixel.effects.SpellSprite;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Lightning;
import com.avmoga.dpixel.effects.particles.EnergyParticle;
import com.avmoga.dpixel.effects.particles.SparkParticle;
import com.avmoga.dpixel.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ScrollOfRecharging extends Scroll{
	private static final String LIGHTNING = "Scroll of Recharging Detonation";
	
	private ArrayList<Char> targets = new ArrayList<Char>();
	
	private int[] points = new int[20];
	private int nPoints;
	
	{
		name = "Scroll of Recharging";
		consumedValue = 10;
	}

	public void detonate(Heap heap){
		targets.clear();
		for(Mob mob : Dungeon.level.mobs){
			if(Level.distance(heap.pos, mob.pos) <=3 ){//hee hee
				fx(mob.pos);
			}
		}
		if(Level.distance(heap.pos, Dungeon.hero.pos) <= 3){
			fx(Dungeon.hero.pos);
		}
	}
	
	protected void fx(int cell) {

		nPoints = 0;
		points[nPoints++] = Dungeon.hero.pos;

		Char ch = Actor.findChar(cell);
		if (ch != null) {

			targets.clear();
			hit(ch, Random.Int(7, 13));
			if(!(ch.HP >= 0)){
				targets.remove(ch);
			}

		} else {

			points[nPoints++] = cell;
			CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);

		}
		Dungeon.hero.sprite.parent.add(new Lightning(points, nPoints, null));
	}
	
	private void hit(Char ch, int damage) {

		if (damage < 1) {
			return;
		}

		if (ch == Dungeon.hero) {
			Camera.main.shake(2, 0.3f);
		}

		targets.add(ch);
		ch.damage(Level.water[ch.pos] || ch.flying ? (int) (damage * 2)
				: damage, LightningTrap.LIGHTNING);

		ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();

		points[nPoints++] = ch.pos;

		HashSet<Char> ns = new HashSet<Char>();
		for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar(ch.pos + Level.NEIGHBOURS8[i]);
			if (n != null && !targets.contains(n)) {
				ns.add(n);
			}
		}

		if (ns.size() > 0) {
			hit(Random.element(ns), Random.Int(damage / 2, damage));
		}
		if(!(ch.HP >= 0)){
			
		}
	}
	
	public void detonateOn(Hero hero){
		hero.damage(Random.Int(hero.HP / 3), LIGHTNING);
		
		int[] points = new int[2];
		
		points[0] = hero.pos - Level.getWidth();
		points[1] = hero.pos + Level.getWidth();
		hero.sprite.parent.add(new Lightning(points, 2, null));

		points[0] = hero.pos - 1;
		points[1] = hero.pos + 1;
		hero.sprite.parent.add(new Lightning(points, 2, null));
		
		CellEmitter.center(hero.pos).burst(SparkParticle.FACTORY,
				Random.IntRange(3, 4));
	}
	@Override
	protected void doRead() {

		int count = curUser.belongings.charge(true);
		charge(curUser);

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		if (count > 0) {
			GLog.i("a surge of energy courses through your pack, recharging your wand"
					+ (count > 1 ? "s" : ""));
			SpellSprite.show(curUser, SpellSprite.CHARGE);
		} else {
			GLog.i("a surge of energy courses through your pack, but nothing happens");
		}
		setKnown();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return "The raw magical power bound up in this parchment will, when released, "
				+ "recharge all of the reader's wands to full power.";
	}

	public static void charge(Hero hero) {
		hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 15);
	}

	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
