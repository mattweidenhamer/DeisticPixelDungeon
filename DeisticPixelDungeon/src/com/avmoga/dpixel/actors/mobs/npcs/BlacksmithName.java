package com.avmoga.dpixel.actors.mobs.npcs;
//I am so not sorry.
//If I cause a stack overflow with this, I'm going to laugh so hard.

public class BlacksmithName {
	private static String lyrics = "Bip, Bop,"
			+ "Bippie to the Bippie"
			+ "The bip, bip a bop, and you don't stop, a rock it"
			+ "To the bang bang boogie, say, up jump the boogie,"
			+ "To the rhythm of the boogie, the beat."
			+ "Now, what you hear is not a test - I'm rappin' to the beat,"
			+ "And me, the groove, and my friends are gonna try to move your feet."
			+ "See, I am Wonder Mike, and I'd like to say hello,"
			+ "To the black, to the white, the red and the brown,"
			+ "The purple and yellow. But first, I gotta"
			+ "Bang bang, the boogie to the boogie,"
			+ "Say up jump the boogie to the bang bang boogie,"
			+ "Let's rock, you don't stop,Rock the rhythm that'll make your body rock. Well so far you've heard my voice but I brought two friends along, And the next on the mic is my man Hank, C'mon, Hank, sing that song!"
			+ "Check it out, I'm the C-A-S-A, the N-O-V-A, And the rest is F-L-Y, You see I go by the code of the doctor of the mix,And these reasons I'll tell you why. You see, I'm six foot one, and I'm tons of fun When I dress to a T,You see, I got more clothes than Muhammad Ali and I dress so viciously. I got bodyguards, I got two big cars That definitely ain't the wack, I got a Lincoln Continental and a sunfoofed Cadillac. So after school I take a dip in the pool, Which is really on the wall, I got a colour TV, so I can see The Knicks play basketball. Hear me talk about Checkbooks, credit cards, mo' money Than a sucker could ever spend, But I wouldn't give a sucker or a bum form the Rucker Not a dime 'til I made it again. Everybody go Ho-tel, Mo-tel, Whatcha gonna do today? (Say what?)'Cos I'm a get a fly girl, Gonna get some spank n' drive off in a def OJ. Everybody go Ho-tel, Mo-tel, Holiday Inn,Say if your girl starts actin' up, then you take her friend. Master Gee! My mellow!It's on to you, so whatcha gonna do?"
			+ "Well, it's on'n'n'on'n'on on'n'on,The beat don't stop until the break of dawn.I said M-A-S, T-E-R, a G with a double E,I said I go by the unforgettable nameOf the man they call the Master Gee.Well, my name is known all over the worldBy all the foxy ladies and the pretty girls.I'm goin' down in historyAs the baddest rapper there ever could be.Now I'm feelin' the highs and you're feelin' the lows,The beat starts gettin' into your toesYou start poppin' your fingers and stompin' your feetAnd movin' your body while while you're sitting in your seatAnd then damn! Ya start doin' the freak, I saidDamn! Right outta your seatThen you throw your hands high in the air,Ya rockin' to the rhythm, shake your derriereYa rockin' to the beat without a care,With the sureshot MCs for the affair.Now, I'm not as tall as the rest of the gangBut I rap to the beat just the same.I got a little face, and a pair of brown eyesAll I'm here to do, ladies, is hypnotizeSingin' on'n'n'on'n'on on'n'on,The beat don't stop until the break of dawningin' on'n'n'on'n'on on'n'on,Like a hot buttered pop da pop da pop dibbie dibbiePop da pop pop, don't you dare stopCome alive y'all, gimme whatcha gotI guess by now you can take a hunchAnd find that I am the baby of the bunchBut that's okay, I still keep in stride,'Cos all I'm here to do is just wiggle your behindSingin' on'n'n'on'n'on on'n'on,The beat don't stop until the break of dawn.Singin'n'n'on'n'on on'n'on,Rock rock, y'all, throw it on the floorI'm gonna freak you here, I'm gona freak you there,I'm gonna move you outta this atmosphere.'Cos I'm one of a kind and I'll shock your mindI'll put TNT in your behind. I saidOne, two, three, four, come on, girls, get on the floorA-come alive, y'all, a-gimme whatcha got'Cos I'm guaranteed to make you rockI said one, two, three, four, tell me, Wonder MikeWhat are you waiting for?";
	private static String[] blacksmithNames = lyrics.split(" ");
	public static String getName(int n) {
		if(blacksmithNames[n] != null){
			return "Troll Blacksmith named " + blacksmithNames[n];
		} else {
			return "Troll Blacksmith named bop";
		}

	}
}
