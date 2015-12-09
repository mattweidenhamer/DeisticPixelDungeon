package com.avmoga.dpixel.actors.buffs;

import com.avmoga.dpixel.Dungeon;

public class Mark extends FlavourBuff {

	public static final float DURATION = 20f;

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
}
