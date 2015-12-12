package com.avmoga.dpixel.actors.mobs;

import java.util.HashSet;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.blobs.ParalyticGas;
import com.avmoga.dpixel.actors.blobs.ToxicGas;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Burning;
import com.avmoga.dpixel.actors.buffs.Terror;
import com.avmoga.dpixel.scenes.GameScene;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.weapon.enchantments.Death;
import com.avmoga.dpixel.items.weapon.enchantments.Leech;
import com.avmoga.dpixel.levels.Terrain;
import com.avmoga.dpixel.levels.traps.LightningTrap;
import com.avmoga.dpixel.mechanics.Ballistica;
import com.avmoga.dpixel.effects.particles.EnergyParticle;
import com.avmoga.dpixel.effects.particles.PurpleParticle;
import com.avmoga.dpixel.effects.particles.SparkParticle;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.Lightning;
import com.avmoga.dpixel.sprites.CharSprite;
import com.avmoga.dpixel.sprites.OrbOfZotSprite;
import com.avmoga.dpixel.sprites.SingularitySprite;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Callback;
import com.avmoga.dpixel.actors.buffs.Silence;
import com.avmoga.dpixel.actors.buffs.Slow;

public class Singularity extends Mob implements Callback {

	{
		name = "arcane singularity";
		spriteClass = SingularitySprite.class;
		
		hostile = true;
		
		HP = HT = 20;
		defenseSkill = 5;
	}
	
	public static final float SPAWN_DELAY = 0.1f;
	private final String TXT_SINGULARITY = "%s's lightning bolt killed you...";
	private final String TXT_UNDONE = "You were killed by %s's stabilizing.";
	private int killCount;
	private int hitCell;
	
	public void add (Buff buff){
		if (buff instanceof Silence){
			damage(Random.NormalIntRange(HT / 5, HT / 3), buff);
		} else {
			super.add(buff);
		}
	}
	
	public String description(){
		return "Wild magic has resulted in the creation of a fluxuation "
			+ "in spacetime! It will keep drawing in energy until it stabilizes!";
	}
	
	public void move(int step){
	}
	
	public void die(Object cause){
		for(int space : Level.NEIGHBOURS8){
			int c = this.pos + space;
			GLog.i("The singularity destroys itself!");
			Sample.INSTANCE.play(Assets.SND_BLAST, 2);
			if (Dungeon.visible[c]){
				CellEmitter.get(c).burst(EnergyParticle.FACTORY, 2);
			}
		
			if(Level.flamable[c]){
				Level.set(c, Terrain.EMBERS);
				GameScene.updateMap();
			}
		
			Heap heap = Dungeon.level.heaps.get(c);
			if(heap != null){
				heap.explode();
			}
		
			Char ch = Actor.findChar(c);
			if (ch != null) {
			// those not at the center of the blast take damage less
			// consistently.
				int minDamage = c == this.pos ? Dungeon.depth + 5 : 1;
				int maxDamage = 10 + Dungeon.depth * 2;

			int dmg = Random.NormalIntRange(minDamage, maxDamage)
					- Random.Int(ch.dr());
			if (dmg > 0) {
				ch.damage(dmg, this);
			}

			if (ch == Dungeon.hero && !ch.isAlive())
				Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name)));
				GLog.n(TXT_UNDONE);
		}
		}
		super.die(cause);
		Dungeon.observe();
	}
	
	protected boolean act() {
		
		for (int n : Level.NEIGHBOURS8DIST2) {
			int c = pos + n;
			Char ch = Actor.findChar(c);
		}
		//Level.fieldOfView[Dungeon.hero.pos] &&
		
		boolean result = super.act();
		return result;
	}
	
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Char> enemies = new HashSet<Char>();
			for(Mob mob : Dungeon.level.mobs){
				if(Level.fieldOfView[mob.pos]){
					enemies.add(mob);
				}
			}
			if(Level.fieldOfView[Dungeon.hero.pos]){
				enemies.add(Dungeon.hero);
			}
			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}
	public void call(){
		next();
	}
	public void becokon(int cell){
	}
	
	protected boolean doAttack(Char enemy) {
		boolean returnme;
		spend(attackDelay());

		boolean rayVisible = false;

		for (int i = 0; i < Ballistica.distance; i++) {
			if (Dungeon.visible[Ballistica.trace[i]]) {
				rayVisible = true;
			}
		}

		if (rayVisible) {
			sprite.attack(hitCell);
			returnme = false;
		} else {
			attack(enemy);
			returnme = true;
		}
		if(!enemy.isAlive()){
			killCount++;
			checkKills();
		}
		return returnme;
	}
	public boolean attack(Char enemy) {

		for (int i = 1; i < Ballistica.distance; i++) {

			int pos = Ballistica.trace[i];

			Char ch = Actor.findChar(pos);
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange((Dungeon.depth * 2)+5, (Dungeon.depth*2)+10), this);

				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(SparkParticle.FACTORY, 3);
				}

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(Utils.format(ResultDescriptions.MOB,
							Utils.indefinite(name)));
					GLog.n(TXT_SINGULARITY, name);
				}
			} else {
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		return true;
	}
	private void checkKills(){
		if(this.killCount >= 20){
			die(this);
		}
	}
	protected boolean canAttack(Char enemy) {

		hitCell = Ballistica.cast(pos, enemy.pos, true, false);

		for (int i = 1; i < Ballistica.distance; i++) {
			if (Ballistica.trace[i] == enemy.pos) {
				return true;
			}
		}
		return false;
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	public int attackSkill(Char target) {
		return 20 + Dungeon.depth;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(ParalyticGas.class);
		IMMUNITIES.add(Burning.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
		
	public static Singularity spawnAt(int pos){
		Singularity me = new Singularity();
		
		me.pos = pos;
		GameScene.add(me, SPAWN_DELAY);
			
		return me;
	}
}

