package com.avmoga.dpixel.items.weapon.enchantments;

import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.actors.buffs.Silence;
import com.avmoga.dpixel.items.weapon.Weapon;
import com.avmoga.dpixel.items.weapon.Weapon.Enchantment;
import com.avmoga.dpixel.sprites.ItemSprite;
import com.avmoga.dpixel.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Nullify extends Enchantment {
	
	private static final String TXT_NULL = "Null %s";
	
	private static ItemSprite.Glowing WHITE= new ItemSprite.Glowing(0xffffff);

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		int level = Math.max(0, weapon.level);
		if(Random.Int(level + 100) >= 85){
			Buff.affect(defender, Silence.class, Silence.duration(defender));
			return true;
		}
		else{
			return false;
		}
	}
	public Glowing glowing() {
		return WHITE;
	}
	public String name(String weaponName){
		return String.format(TXT_NULL, weaponName);
	}

}
