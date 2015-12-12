package com.avmoga.dpixel.ui;

import com.avmoga.dpixel.Assets;
import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.actors.Char;
import com.avmoga.dpixel.actors.buffs.Buff;
import com.avmoga.dpixel.gods.God;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.SparseArray;

public class GodIndicator extends Component {
	public static final int UNKNOWN = 0;

	public static final int RATKING = 1;
	public static final int NEMESIS = 2;
	public static final int PLUTO = 3;
			
	public static final int SIZE = 7;

	private SmartTexture texture;
	private TextureFilm film;

	private SparseArray<Image> icons = new SparseArray<Image>();

	@Override
	protected void createChildren() {
		texture = TextureCache.get(Assets.BUFFS_SMALL);
		film = new TextureFilm(texture, SIZE, SIZE);
	}

	@Override
	protected void layout() {
		clear();

		SparseArray<Image> newIcons = new SparseArray<Image>();

		for (God god: Dungeon.discoveredGods) {
			int icon = god.icon();
			if(god.discovered){
				Image img = new Image(texture);
				img.frame(film.get(icon));
				img.x = x + members.size() * (SIZE + 2);
				img.y = y;
				add(img);

				newIcons.put(icon, img);
			}
			else {
				
			}
		}

		for (Integer key : icons.keyArray()) {
			if (newIcons.get(key) == null) {
				Image icon = icons.get(key);
				icon.origin.set(SIZE / 2);
				add(icon);
				add(new AlphaTweener(icon, 0, 0.6f) {
					@Override
					protected void updateValues(float progress) {
						super.updateValues(progress);
						image.scale.set(1 + 5 * progress);
					};
				});
			}
		}

		icons = newIcons;
	}
}
