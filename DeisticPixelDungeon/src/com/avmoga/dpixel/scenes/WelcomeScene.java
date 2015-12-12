package com.avmoga.dpixel.scenes;

import com.avmoga.dpixel.Badges;
import com.avmoga.dpixel.Chrome;
import com.avmoga.dpixel.Rankings;
import com.avmoga.dpixel.ShatteredPixelDungeon;
import com.avmoga.dpixel.ui.Archs;
import com.avmoga.dpixel.ui.RedButton;
import com.avmoga.dpixel.ui.ScrollPane;
import com.avmoga.dpixel.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

	private static final String TTL_Welcome = "Welcome!";

	private static final String TTL_Update = "v0.1.0 DPD, v0.3.0: SPD, 0.2.4c: PD 1.7.5 and Some Extras!";

	private static final String TTL_Future = "Wait What?";

	private static final String TXT_Welcome = "Deistic Pixel Dungeon\n\nVersion 0.1.3: Exodus"
			+"This is a rework/expansion of Watabou's Pixel Dungeon.\n\n"
			+ "Included are all additions from Shattered Pixel Dungeon (0.2.4c) by 00-Evan.\n\n"
			+ "Additionally, all additions from Sprouted Pixel Dungeon (0.3.5 beta) by dachhack are here.\n\n"
			+ "This mod will eventually add in Gods, and has currently added in Races and Artifacts based on races.\n\n"
			+ "Happy Dungeoneering!\n\n"
			+"\n\n"


			+"Many other tweaks and additions have been included!\n\n";

	//TODO: Fix this whenever an update is pushed.
	private static final String TXT_Update = 
			"Version 1.0.3 is hopefully the last bugfixing patch. Everything should be stable for the most part now."
			;

	private static final String TXT_Future = "It seems that your current saves are from a future version of Sprouted Pixel Dungeon!\n\n"
			+ "Either you're messing around with older versions of the app, or something has gone buggy.\n\n"
			+ "Regardless, tread with caution! Your saves may contain things which don't exist in this version, "
			+ "this could cause some very weird errors to occur."
			+ "consider reporting this bug to the developer!";

	private static final String LNK = "https://play.google.com/store/apps/details?id=com.avmoga.dpixel";

	@Override
	public void create() {
		super.create();

		final int gameversion = ShatteredPixelDungeon.version();

		BitmapTextMultiline title;
		BitmapTextMultiline text;

		if (gameversion == 0) {

			text = createMultiline(TXT_Welcome, 8);
			title = createMultiline(TTL_Welcome, 16);

		} else if (gameversion <= Game.versionCode) {

			text = createMultiline(TXT_Update, 6);
			title = createMultiline(TTL_Update, 9);

		} else {

			text = createMultiline(TXT_Future, 8);
			title = createMultiline(TTL_Future, 16);

		}

		int w = Camera.main.width;
		int h = Camera.main.height;

		int pw = w - 10;
		int ph = h - 50;

		title.maxWidth = pw;
		title.measure();
		title.hardlight(Window.SHPX_COLOR);

		title.x = align((w - title.width()) / 2);
		title.y = align(8);
		add(title);

		NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
		panel.size(pw, ph);
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add(panel);

		ScrollPane list = new ScrollPane(new Component());
		add(list);
		list.setRect(panel.x + panel.marginLeft(), panel.y + panel.marginTop(),
				panel.innerWidth(), panel.innerHeight());
		list.scrollTo(0, 0);

		Component content = list.content();
		content.clear();

		text.maxWidth = (int) panel.innerWidth();
		text.measure();

		content.add(text);

		content.setSize(panel.innerWidth(), text.height());

		RedButton okay = new RedButton("Okay!") {
			@Override
			protected void onClick() {

				if (gameversion <= 32) {
					// removes all bags bought badge from pre-0.2.4 saves.
					Badges.disown(Badges.Badge.ALL_BAGS_BOUGHT);
					Badges.saveGlobal();

					// imports new ranking data for pre-0.2.3 saves.
					if (gameversion <= 29) {
						Rankings.INSTANCE.load();
						Rankings.INSTANCE.save();
					}
				}

				ShatteredPixelDungeon.version(Game.versionCode);
				Game.switchScene(TitleScene.class);
			}
		};

		/*
		 * okay.setRect(text.x, text.y + text.height() + 5, 55, 18); add(okay);
		 * 
		 * RedButton changes = new RedButton("Changes") {
		 * 
		 * @Override protected void onClick() { parent.add(new WndChanges()); }
		 * };
		 * 
		 * changes.setRect(text.x + 65, text.y + text.height() + 5, 55, 18);
		 * add(changes);
		 */

		okay.setRect((w - pw) / 2, h - 22, pw, 18);
		add(okay);

		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		fadeIn();
	}
}
