package com.avmoga.dpixel.plants;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ResultDescriptions;
import com.avmoga.dpixel.actors.hero.Hero.Doom;
import com.avmoga.dpixel.actors.Actor;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.effects.CellEmitter;
import com.avmoga.dpixel.effects.particles.BlastParticle;
import com.avmoga.dpixel.effects.particles.SmokeParticle;
import com.avmoga.dpixel.items.Heap;
import com.avmoga.dpixel.items.potions.PotionOfLiquidFlame;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.avmoga.dpixel.utils.GLog;
import com.avmoga.dpixel.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class BombFruit extends Plant implements Doom{

	{
		image = 13;
		plantName = "blastseed";
	}
	
	private static final String TXT_DESC = "More or less living landmines, BombSeed blast their seeds out "
			+ "violently when they sense another creatue nearby. The corpses tend to make fertile "
			+ "soil.";
	private static final String TXT_BLEW_UP = "You were killed by your own blastseed...";
	
	public void activate(Char ch) {
		if(ch != null){
			ch.damage(Random.Int(ch.HT / 10, ch.HT), this);
		}
		for (int n : Level.NEIGHBOURS9) {
			int c = this.pos + n;
			if (c >= 0 && c < Level.getLength()) {
				if (Dungeon.visible[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				Char d = Actor.findChar(c);
				if (d != null) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == this.pos ? (d.HT / 10) : 1;
					int maxDamage = c == this.pos ? d.HT : d.HT / 2;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Random.Int(d.dr());
					if (dmg > 0) {
						d.damage(dmg, this);
					}

					if (d == Dungeon.hero && !d.isAlive())
						// constant is used here in the rare instance a player
						// is killed by a double bomb.
						Dungeon.fail(Utils.format(ResultDescriptions.ITEM,
								"blastseed"));
				}
			}
		}
		CellEmitter.center(this.pos).burst(BlastParticle.FACTORY, 30);
		super.activate(ch);
	}
			
	public static class Seed extends Plant.Seed {
		{
			plantName = "Blastseed";

			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SUNGRASS;
			
			Sample.INSTANCE.play(Assets.SND_BLAST, 2);

			plantClass = BombFruit.class;
			alchemyClass = PotionOfLiquidFlame.class;				
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
		
		
	}

	@Override
	public void onDeath() {
		GLog.n(TXT_BLEW_UP);
		
	}

}
