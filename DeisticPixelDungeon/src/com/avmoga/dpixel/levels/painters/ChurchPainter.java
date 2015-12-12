package com.avmoga.dpixel.levels.painters;

import com.avmoga.dpixel.Dungeon;
import com.avmoga.dpixel.items.Generator;
import com.avmoga.dpixel.items.Item;
import com.avmoga.dpixel.items.keys.IronKey;
import com.avmoga.dpixel.items.scrolls.Scroll;
import com.avmoga.dpixel.levels.Level;
import com.avmoga.dpixel.levels.Room;
import com.avmoga.dpixel.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ChurchPainter extends Painter {//Michaelangelo
	public static void paint(Level level, Room room) {//TODO Church painter, make appear on a guarenteed floor and contain a chest item. No mobs can attack in the Church.

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);

		Room.Door entrance = room.entrance();
		Point a = null;
		Point b = null;

		if (entrance.x == room.left) {
			a = new Point(room.left + 1, entrance.y - 1);
			b = new Point(room.left + 1, entrance.y + 1);
			fill(level, room.right - 1, room.top + 1, 1, room.height() - 1,
					Terrain.STATUE);
		} else if (entrance.x == room.right) {
			a = new Point(room.right - 1, entrance.y - 1);
			b = new Point(room.right - 1, entrance.y + 1);
			fill(level, room.left + 1, room.top + 1, 1, room.height() - 1,
					Terrain.STATUE);
		} else if (entrance.y == room.top) {
			a = new Point(entrance.x + 1, room.top + 1);
			b = new Point(entrance.x - 1, room.top + 1);
			fill(level, room.left + 1, room.bottom - 1, room.width() - 1, 1,
					Terrain.STATUE);
		} else if (entrance.y == room.bottom) {
			a = new Point(entrance.x + 1, room.bottom - 1);
			b = new Point(entrance.x - 1, room.bottom - 1);
			fill(level, room.left + 1, room.top + 1, room.width() - 1, 1,
					Terrain.STATUE);
		}
		if (a != null && level.map[a.x + a.y * Level.getWidth()] == Terrain.EMPTY) {
			set(level, a, Terrain.STATUE);
		}
		if (b != null && level.map[b.x + b.y * Level.getWidth()] == Terrain.EMPTY) {
			set(level, b, Terrain.STATUE);
		}

		int n = Random.IntRange(2, 3);
		for (int i = 0; i < n; i++) {
			int pos;
			do {
				pos = room.random();
			} while (level.map[pos] != Terrain.EMPTY
					|| level.heaps.get(pos) != null);
			level.drop(prize(level), pos);
		}

		entrance.set(Room.Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));
	}

	private static Item prize(Level level) {

		Item prize = level.findPrizeItem(Scroll.class);
		if (prize == null)
			prize = Generator.random(Generator.Category.SCROLL);

		return prize;
	}
}
