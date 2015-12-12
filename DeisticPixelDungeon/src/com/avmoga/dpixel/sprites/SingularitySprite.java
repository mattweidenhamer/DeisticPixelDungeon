package com.avmoga.dpixel.sprites;

import com.avmoga.dpixel.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class SingularitySprite extends MobSprite {

	public SingularitySprite() {
		super();

		texture(Assets.SINGULARITY);

		TextureFilm frames = new TextureFilm(texture, 16, 18);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 1, 2, 3, 4, 5, 6, 7);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 4);

		attack = new Animation(8, false);
		attack.frames(frames, 8, 9, 10, 11);

		die = new Animation(8, false);
		die.frames(frames, 12, 13, 14, 15);

		play(idle);
	}
}
