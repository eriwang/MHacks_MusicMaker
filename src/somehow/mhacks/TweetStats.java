package somehow.mhacks;

import java.util.ArrayList;

public class TweetStats {
	//counts of various char types in the tweet
	public int hardCtr, softCtr, capsCtr;
	//total char count (non-space) and "emotion", 0-100 where 100 is super happy
	public int charCtr, emotMtr;
	//actual tweet/ username info
	public String user, tweet;
	//base rhythms, 0.5 eighth, 1 quarter... 
	public ArrayList<Double> rhythms;
	
	public TweetStats(int hctr, int sctr, int cpctr, 
			int crctr, int hapmtr,  ArrayList<Double> rhy,
			String usr, String twt){
		hardCtr = hctr;
		softCtr = sctr;
		capsCtr = cpctr;
		charCtr = crctr;
		emotMtr = hapmtr;
		rhythms = rhy;
		user = usr;
		tweet = twt;
	}
}
