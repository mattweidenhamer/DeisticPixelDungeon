package com.avmoga.dpixel.actors;

public enum Flag {
	EVIL("evil"), UNDEAD("undead"), ANTHRO("anthropomorphic"), BEAST("beast"), DWARF("dwarf"), GNOLL("gnoll"), 
	MAGIC("magical"), MACHINE("machine"), PET("pet"), INSECT("insect"), RARE("rare"), DEWMOB("dewmob"), AQUATIC("aquatic"),
	RANGED("ranged"), HUMAN("human"), ANIMATED("animated"), BOSS("boss"), FLYING("flying");

	private String title;
	
	public String getTitle(){
		return title;
	}
	
	private Flag(String title) {
		this.title = title;
	}
}