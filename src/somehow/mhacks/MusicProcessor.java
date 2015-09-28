package somehow.mhacks;

import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Write;

import java.util.ArrayList;

public class MusicProcessor implements JMC{
	//Bass/ Melody Notes of C Major, where BASS_NOTES[1] represents C, 2, rep D, etc.
	private static final int[] BASS_MAJ_NOTES =
		{47, 48, 50, 52, 53, 55, 57, 59, 60};
	private static final int[] BASS_MIN_NOTES =
		{43, 45, 47, 48, 50, 52, 53, 55, 57};

	
	//Determines a tempo based on hard and soft counters
	public static double createTempo(int totHardCtr, int totSoftCtr){
		double tempo = 
				120 + 80 * Math.log((double)totHardCtr / (double)totSoftCtr);

		if (tempo > 220) return 220;
		else if (tempo < 40) return 40;
		else return tempo;
	}
	
	//Creates a score to be played from the data
	//Typically, 4 8 measure melodies w/ bass accompaniment
	public static Score genSong(TweetStats[] data){
		Score song = new Score();
		Part melo_part = new Part();
		Part harm_part = new Part();
		Part bass_part = new Part();
		
		int totHardCtr = 0, totSoftCtr = 0, curr_bass = -1, measure = 0;
		for(TweetStats ts : data){
			double hardPct = ts.hardCtr / (ts.hardCtr + ts.softCtr + 0.0);
			double capsPct = ts.capsCtr / (ts.capsCtr + ts.charCtr + 0.0);
			totHardCtr += ts.hardCtr;
			totSoftCtr += ts.softCtr;
			
			Phrase melo_phrase = new Phrase();
			Phrase harm_phrase = new Phrase();
			Phrase bass_phrase = new Phrase();
			
			boolean isMaj = (ts.emotMtr > 40);
			/*
			//TODO: put correct input into getNextBase
			for(int i = 0; i < 8; ++i){
				curr_bass = BassSeq.getNextBass(curr_bass, i, isMaj, 70);
				bass_phrase.add(
						new Note(((isMaj) ? BASS_MAJ_NOTES[curr_bass] : BASS_MIN_NOTES[curr_bass]), 4));
				harm_phrase.add(
						new Note(((isMaj) ? 
								BASS_MAJ_NOTES[(curr_bass + 2) % BASS_MAJ_NOTES.length] + 24 : 
									BASS_MIN_NOTES[(curr_bass + 2) % BASS_MIN_NOTES.length] + 24), 4));
				melo_phrase.add(
						new Note(((isMaj) ? BASS_MAJ_NOTES[curr_bass] + 31 : BASS_MIN_NOTES[curr_bass] + 31), 4));
			}*/
			
			int i = 0, bass_note = -1, harm_note = 0, melo_note = 0;
			double curr_beat = 0, bound = 0;
			for(double beats = 0; beats < 32; beats += curr_beat){
				curr_beat = ts.rhythms.get(i % ts.rhythms.size());
				System.out.println(bass_note);
				bass_note = BassSeq.getNextBass(bass_note, (int)beats / 4, isMaj, ts.charCtr);
				harm_note = ((isMaj) ? 
						BASS_MAJ_NOTES[(bass_note + 2) % BASS_MAJ_NOTES.length] + 24 : 
							BASS_MIN_NOTES[(bass_note + 2) % BASS_MIN_NOTES.length] + 24);
				melo_note = ((isMaj) ? BASS_MAJ_NOTES[bass_note] + 31 : BASS_MIN_NOTES[bass_note] + 31);
				
				if(beats + curr_beat >= bound){
					if(beats + curr_beat > bound + 2){
						harm_note = REST;
						melo_note = REST;
					}
					curr_beat = bound - beats;
					bound += 4;
				}
				
				bass_phrase.add(
						new Note(((isMaj) ? BASS_MAJ_NOTES[bass_note] 
								: BASS_MIN_NOTES[bass_note]), curr_beat));
				harm_phrase.add(new Note(harm_note, curr_beat));
				melo_phrase.add(new Note(melo_note, curr_beat));
				
				++i;
			}
			
			melo_phrase.setStartTime(measure);
			harm_phrase.setStartTime(measure);
			bass_phrase.setStartTime(measure);
			
			measure += 32;
			melo_part.add(melo_phrase);
			harm_part.add(harm_phrase);
			bass_part.add(bass_phrase);
			
		}
		song.add(melo_part);
		song.add(harm_part);
		song.add(bass_part);
		song.setTempo(createTempo(totHardCtr, totSoftCtr));
		
		return song;
	}
	
	public static void songify(TweetStats[] data){
		System.out.println("Songifying");
		Write.midi(genSong(data), "song.mid");
		Play.mid("song.mid");
	}
	
	/*public static void main(String[] args){	
		//TODO: take data correctly from Twitter_data
		TweetStats[] data = {new TweetStats(40,50,3,100,4, new ArrayList<Double>()),
				new TweetStats(40,50,3,100,70, new ArrayList<Double>()),
				new TweetStats(40,50,3,100,60, new ArrayList<Double>()),
				new TweetStats(40,50,3,100,50, new ArrayList<Double>())};
		Write.midi(genSong(data), "song.mid");
		Play.mid("song.mid");
				
	}*/
}
