package com.avmoga.dpixel.gods;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
//TODO finish me yo
public abstract class God implements Bundlable{
	
	public int ranking = 0;
	public int progress = 0;
	public String name;
	public Status status = Status.NEUTRAL;
	public boolean communed = false;
	public int involvement = ranking / 15; //How involved the God will be during the quest. Depends on ranking.
	
	public static final String NAME = "name";
	public static final String RANKING = "ranking";
	public static final String PROGRESS = "progress";
	public static final String COMMUNED = "communed";
	
	public boolean discovered = false;
	
	public enum Status {
		NEUTRAL("neutral"), ANGRY("angry"), CALM("calm"), GENEROUS("generous");
		
		public String title;

		private Status(String status){
			this.title = status;
		}
	}
	
	public void storeInBundle(Bundle bundle){
		bundle.put(RANKING, ranking);
		bundle.put(PROGRESS, progress);
		bundle.put(COMMUNED, communed);
	}
	
	public void restoreFromBundle(Bundle bundle){
		ranking = bundle.getInt(RANKING);
		progress = bundle.getInt(PROGRESS);
		communed = bundle.getBoolean(COMMUNED);
	}
	
	public abstract int icon();
	public String name(){
		return this.name;
	}
}
