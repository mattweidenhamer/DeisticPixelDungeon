package com.avmoga.dpixel.gods;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
//TODO finish me yo
public abstract class God implements Bundlable{
	
	public int ranking = 0;
	public int progress = 0;
	public String name;
	public boolean communed = false;
	public int involvement = ranking / 15; //How involved the God will be during the quest. Depends on ranking.
	
	public static final String NAME = "name";
	public static final String RANKING = "ranking";
	public static final String PROGRESS = "progress";
	public static final String COMMUNED = "communed";
	
	
	public void storeInBundle(Bundle bundle){
		bundle.put(NAME, name);
		bundle.put(RANKING, ranking);
		bundle.put(PROGRESS, progress);
		bundle.put(COMMUNED, communed);
	}
	
	public void restoreFromBundle(Bundle bundle){
		ranking = bundle.getInt(RANKING);
		progress = bundle.getInt(PROGRESS);
		name = bundle.getString(NAME);
		communed = bundle.getBoolean(COMMUNED);
	}
}
