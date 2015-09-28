package somehow.mhacks;

import java.util.Arrays;
import java.util.List;

public class BassSeq {
	//Starting/ ending bass notes for phrases
	static final Integer[] MAJ_START1 = {1, 3, 4, 5, 6};
	static final Integer[] MAJ_END1 = {1, 3, 4, 5, 6};
	static final Integer[] MAJ_START2 = {3, 4, 5, 6};
	static final Integer[] MAJ_END2 = {1, 4, 5};
	static final Integer[] MIN_START1 = {1, 3, 4, 5, 6};
	static final Integer[] MIN_END1 = {1, 2, 3, 4, 5, 6, 7}; //-5
	static final Integer[] MIN_START2 = {4, 5, 6, 7};
	static final Integer[] MIN_END2 = {1}; //-1, -5
	
	//Chord changes that sound reasonable (personal preference)
	static final Integer[][] MAJ_CHORD_TRANS = { 
		{1, 2, 3, 4, 5, 6, 7}, //Placeholder to make accessing indices easier 
		{3, 4, 5, 6},
		{3, 4, 5, 6},
		{4, 5, 6},
		{1, 2, 3, 5, 6},
		{1, 3, 4, 5, 6},
		{1, 3, 4, 5},
		{1, 2, 3, 4, 5, 6, 7} //Placeholder, should never reach 7
		};
	static final Integer[][] MIN_CHORD_TRANS = {
		{1, 2, 3, 4, 5, 6 , 7}, //Placeholder to make accessing indices easier
		{3, 4, 5, 6, 7},
		{1, 3, 4, 5, 7},
		{1, 4, 5, 6, 7},
		{1, 2, 3, 5, 6, 7}, //-5
		{1, 3, 4, 6, 7}, //-5
		{1, 3, 4, 5, 7}, //-5
		{1, 4, 5, 6, 7} //-5
		};
	
	//curr_bass: current bass note, given as int from 1-7
	//meas: current measure, given as int > 0
	//isMaj: if the key is a Major key
	//seed: "randomization"
	public static int getNextBass(int curr_bass, int meas, boolean isMaj, int seed){
		//TODO: Improve algorithm for better sounding bass
		List<Integer> bass_arr;
		if(meas % 8 == 0)
			bass_arr = Arrays.asList((isMaj) ? MAJ_START1 : MIN_START1);
		else if(meas % 8 == 3)
			bass_arr = Arrays.asList((isMaj) ? MAJ_END1 : MIN_END1);
		else if(meas % 8 == 4)
			bass_arr = Arrays.asList((isMaj) ? MAJ_START2 : MIN_START2);
		else if(meas % 8 == 7)
			bass_arr = Arrays.asList((isMaj) ? MAJ_END2 : MIN_END2);
		else{
			bass_arr = Arrays.asList((isMaj) ?
					MAJ_CHORD_TRANS[curr_bass] : MIN_CHORD_TRANS[curr_bass]);
		}
			
		return bass_arr.get(seed % bass_arr.size());
	}
}
