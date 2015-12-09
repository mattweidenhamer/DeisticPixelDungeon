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
package com.avmoga.dpixel.scenes;

import java.util.HashMap;//TODO make the race select scene, which will essentially copy this class

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.ShatteredPixelDungeon;
import com.avmoga.dpixel.actors.hero.HeroRace;
import com.avmoga.dpixel.effects.BannerSprites;
import com.avmoga.dpixel.effects.Speck;
import com.avmoga.dpixel.effects.BannerSprites.Type;
import com.avmoga.dpixel.ui.Archs;
import com.avmoga.dpixel.ui.ExitButton;
import com.avmoga.dpixel.ui.Icons;
import com.avmoga.dpixel.ui.RedButton;
import com.avmoga.dpixel.windows.WndChallenges;
import com.avmoga.dpixel.windows.WndMessage;
import com.avmoga.dpixel.windows.WndRace;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Callback;

public class RaceScene extends PixelScene {

	private static final float BUTTON_HEIGHT = 24;

	private static final String TXT_NEW = "Select Race";

	private static final String TXT_WIN_THE_GAME = "To unlock \"Challenges\", win the game with any character race.";

	private static final float WIDTH_P = 116;
	private static final float HEIGHT_P = 220;

	private static final float WIDTH_L = 224;
	private static final float HEIGHT_L = 124;
	
	private static HashMap<HeroRace, RaceShield> shields = new HashMap<HeroRace, RaceShield>();

	private float buttonX;
	private float buttonY;

	private GameButton btnLoad;
	private GameButton btnNewGame;
	
	private Group unlock;

	public static HeroRace curRace;

	@Override
	public void create() {
		//I couldn't recreate a null pointer exception the second time, after changing nothing. 
		//I think that makes me a genius.

		super.create();

		Badges.loadGlobal();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		float width, height;
		if (ShatteredPixelDungeon.landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2;
		float bottom = h - top;

		Archs archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		Image title = BannerSprites.get(Type.SELECT_YOUR_HERO);
		title.x = align((w - title.width()) / 2);
		title.y = align(top);
		add(title);

		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;

		btnNewGame = new GameButton(TXT_NEW) {
			@Override
			protected void onClick() {
					startNewGame();
			}
		};
		add(btnNewGame);

		float centralHeight = buttonY - title.y - title.height();

		HeroRace[] races = { HeroRace.HUMAN, HeroRace.DWARF,
				HeroRace.WRAITH, HeroRace.GNOLL };
		for (HeroRace cl : races) {
			RaceShield shield = new RaceShield(cl);
			shields.put(cl, shield);
			add(shield);
		}
		if (ShatteredPixelDungeon.landscape()) {
			float shieldW = width / 4;
			float shieldH = Math.min(centralHeight, shieldW);
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int i = 0; i < races.length; i++) {
				RaceShield shield = shields.get(races[i]);
				shield.setRect(left + i * shieldW, top, shieldW, shieldH);
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(w / 2 - challenge.width() / 2, top + shieldH / 2
					- challenge.height() / 2);
			add(challenge);

		} else {
			float shieldW = width / 2;
			float shieldH = Math.min(centralHeight / 2, shieldW * 1.2f);
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int i = 0; i < races.length; i++) {
				RaceShield shield = shields.get(races[i]);
				shield.setRect(left + (i % 2) * shieldW, top + (i / 2)
						* shieldH, shieldW, shieldH);
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(w / 2 - challenge.width() / 2, top + shieldH
					- challenge.height() / 2);
			add(challenge);

		}

		unlock = new Group();
		add(unlock);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		curRace = null;
		updateRace(HeroRace.values()[ShatteredPixelDungeon.lastRace()]);

		fadeIn();

		Badges.loadingListener = new Callback() {
			@Override
			public void call() {
				if (Game.scene() == RaceScene.this) {
					ShatteredPixelDungeon.switchNoFade(RaceScene.class);
				}
			}
		};
	}

	@Override
	public void destroy() {

		Badges.saveGlobal();
		Badges.loadingListener = null;

		super.destroy();

	}

	private void updateRace(HeroRace cl) {

		if (curRace == cl) {
			add(new WndRace(cl));
			return;
		}

		if (curRace != null) {
			shields.get(curRace).highlight(false);
		}
		shields.get(curRace = cl).highlight(true);
		curRace = cl;

		btnNewGame.visible = true;
		btnNewGame.secondary(null, false);
		btnNewGame.setRect(buttonX, buttonY, Camera.main.width
			- buttonX * 2, BUTTON_HEIGHT);
	}

	private void startNewGame() {
		

		Dungeon.hero = null;
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

		if (ShatteredPixelDungeon.intro()) {
			ShatteredPixelDungeon.intro(false);
			Game.switchScene(IntroScene.class);
		} else {
			Game.switchScene(InterlevelScene.class);
		}
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(StartScene.class);
	}

	private static class GameButton extends RedButton {

		private static final int SECONDARY_COLOR_N = 0xCACFC2;
		private static final int SECONDARY_COLOR_H = 0xFFFF88;

		private BitmapText secondary;

		public GameButton(String primary) {
			super(primary);

			this.secondary.text(null);
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			secondary = createText(6);
			add(secondary);
		}

		@Override
		protected void layout() {
			super.layout();

			if (secondary.text().length() > 0) {
				text.y = align(y
						+ (height - text.height() - secondary.baseLine()) / 2);

				secondary.x = align(x + (width - secondary.width()) / 2);
				secondary.y = align(text.y + text.height());
			} else {
				text.y = align(y + (height - text.baseLine()) / 2);
			}
		}

		public void secondary(String text, boolean highlighted) {
			secondary.text(text);
			secondary.measure();

			secondary.hardlight(highlighted ? SECONDARY_COLOR_H
					: SECONDARY_COLOR_N);
		}
	}

	private class RaceShield extends Button {

		private static final float MIN_BRIGHTNESS = 0.6f;

		private static final int BASIC_NORMAL = 0x444444;
		private static final int BASIC_HIGHLIGHTED = 0xCACFC2;

		private static final int MASTERY_NORMAL = 0x666644;
		private static final int MASTERY_HIGHLIGHTED = 0xFFFF88;

		private static final int WIDTH = 24;
		private static final int HEIGHT = 32;
		private static final float SCALE = 1.75f;

		private HeroRace cl;

		private Image avatar;
		private BitmapText name;
		private Emitter emitter;

		private float brightness;

		private int normal;
		private int highlighted;

		public RaceShield(HeroRace cl) {
			super();

			this.cl = cl;

			avatar.frame(cl.ordinal() * WIDTH, 0, WIDTH, HEIGHT);
			avatar.scale.set(SCALE);

			if (Badges.isUnlocked(cl.masteryBadge())) {
				normal = MASTERY_NORMAL;
				highlighted = MASTERY_HIGHLIGHTED;
			} else {
				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;
			}

			name.text(cl.name());
			name.measure();
			name.hardlight(normal);

			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			avatar = new Image(Assets.RACEAVATARS);
			add(avatar);

			name = PixelScene.createText(9);
			add(name);

			emitter = new Emitter();
			add(emitter);
		}

		@Override
		protected void layout() {

			super.layout();

			avatar.x = align(x + (width - avatar.width()) / 2);
			avatar.y = align(y + (height - avatar.height() - name.height()) / 2);

			name.x = align(x + (width - name.width()) / 2);
			name.y = avatar.y + avatar.height() + SCALE;

			emitter.pos(avatar.x, avatar.y, avatar.width(), avatar.height());
		}

		@Override
		protected void onTouchDown() {

			emitter.revive();
			emitter.start(Speck.factory(Speck.LIGHT), 0.05f, 7);

			Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 1.2f);
			updateRace(cl);
		}

		@Override
		public void update() {
			super.update();

			if (brightness < 1.0f && brightness > MIN_BRIGHTNESS) {
				if ((brightness -= Game.elapsed) <= MIN_BRIGHTNESS) {
					brightness = MIN_BRIGHTNESS;
				}
				updateBrightness();
			}
		}

		public void highlight(boolean value) {
			if (value) {
				brightness = 1.0f;
				name.hardlight(highlighted);
			} else {
				brightness = 0.999f;
				name.hardlight(normal);
			}

			updateBrightness();
		}

		private void updateBrightness() {
			avatar.gm = avatar.bm = avatar.rm = avatar.am = brightness;
		}
	}

	private class ChallengeButton extends Button {

		private Image image;

		public ChallengeButton() {
			super();

			width = image.width;
			height = image.height;

			image.am = Badges.isUnlocked(Badges.Badge.VICTORY) ? 1.0f : 0.5f;
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			image = Icons
					.get(ShatteredPixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON
							: Icons.CHALLENGE_OFF);
			add(image);
		}

		@Override
		protected void layout() {

			super.layout();

			image.x = align(x);
			image.y = align(y);
		}

		@Override
		protected void onClick() {
			if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
				RaceScene.this.add(new WndChallenges(ShatteredPixelDungeon
						.challenges(), true) {
					@Override
					public void onBackPressed() {
						super.onBackPressed();
						image.copy(Icons
								.get(ShatteredPixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON
										: Icons.CHALLENGE_OFF));
					};
				});
			} else {
				RaceScene.this.add(new WndMessage(TXT_WIN_THE_GAME));
			}
		}

		@Override
		protected void onTouchDown() {
			Sample.INSTANCE.play(Assets.SND_CLICK);
		}
	}
}