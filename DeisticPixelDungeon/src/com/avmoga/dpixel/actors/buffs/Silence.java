package com.avmoga.dpixel.actors.buffs;

import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.items.rings.RingOfElements.Resistance;
import com.avmoga.dpixel.ui.BuffIndicator;

public class Silence extends FlavourBuff {


	public static final float DURATION = 10f;

	@Override
	public int icon() {
		return BuffIndicator.VERTIGO;
	}

	@Override
	public String toString() {
		return "Silenced";
	}

	public static float duration(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

}
