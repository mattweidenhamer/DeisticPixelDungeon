package com.avmoga.dpixel.items.bags;

import com.avmoga.dpixel.items.CavesKey;
import com.avmoga.dpixel.items.CityKey;
import com.avmoga.dpixel.items.HallsKey;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.PrisonKey;
import com.avmoga.dpixel.items.SewersKey;
import com.avmoga.dpixel.items.TenguKey;
import com.avmoga.dpixel.items.artifacts.*;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;

public class ArtifactBox extends Bag{

	{
		name = "artifact collection";
		image = ItemSpriteSheet.KEYRING;

		size = 16;
		
	}
	public boolean grab(Item item) {
		if (item instanceof AlchemistsToolkit
			||  item instanceof AresChains
			||  item instanceof CapeOfThorns
			||  item instanceof ChaliceOfBlood
			||  item instanceof CloakOfShadows
			||  item instanceof CommRelay
			||  item instanceof DriedRose
			||  item instanceof HornOfPlenty
			||  item instanceof MasterThievesArmband
			||  item instanceof MysticBranch
			||  item instanceof SandalsOfNature
			||  item instanceof TalismanOfForesight
			||  item instanceof TimekeepersHourglass
			||  item instanceof UnstableSpellbook
			||  item instanceof WraithAmulet
				){
			return true;
			} else {
			return false;
			}
	}
	public String info() {
		return "This box is lined with a pleasant-feeling fabric, and there is a glass windows for "
				+ "you to look inside with. You could probably store a decent number of valuable "
				+"items in this box.";
	}
}
