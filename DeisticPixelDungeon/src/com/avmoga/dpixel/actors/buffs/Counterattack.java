package com.avmoga.dpixel.actors.buffs;

import com.avmoga.dpixel.ui.BuffIndicator;

public class Counterattack extends FlavourBuff {
	public class Haste extends FlavourBuff {

		public static final float DURATION = 1f;

		@Override
		public int icon() {
			return BuffIndicator.HASTE;
		}

		@Override
		public String toString() {
			return "Counterattacking";
		}
	}
}
