package com.avmoga.dpixel.items.artifacts;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Rapper extends Artifact {
	{
			name = Dungeon.getMonth() == 11? "W. R. A. P. P. E. R." : "R. A. P. P. E. R.";
			image = ItemSpriteSheet.ARTIFACT_RAPPER;

			level = 0;
			levelCap = 10;

			charge = 0;
		}

		@Override
		protected ArtifactBuff passiveBuff() {
			return new Launcher();
		}

		@Override
		public String desc() {
			String desc = "This purple velvet armband bears the mark of a master thief. This doesn't belong to you, but "
					+ "you doubt it belonged to the person you took it from either.";

			if (isEquipped(Dungeon.hero))
				desc += "\n\nWith the armband around your wrist you feel more dexterous and cunning. Every piece of gold "
						+ "you find makes you desire others property more. "
						+ "You wonder if Pixel Mart accepts the five finger discount...";

			return desc;
		}

		public class Launcher extends ArtifactBuff {
		}
	}
