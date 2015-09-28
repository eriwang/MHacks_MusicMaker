package somehow.mhacks;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Twitter_data {
	public TweetStats[] main(String args) throws FileNotFoundException, UnsupportedEncodingException{
		List<Status> tweets = null;
		PrintWriter printer = new PrintWriter("tweets.txt", "UTF-8");
		ArrayList<String> tweetText = new ArrayList<String>();
		ArrayList<String> tweetUser = new ArrayList<String>();
		ArrayList<String> musicText = new ArrayList<String>();
		ArrayList<String> splitText = new ArrayList<String>();
		ArrayList<Integer> arrayLength = new ArrayList<Integer>();
		ArrayList<Double> arrayDouble = new ArrayList<Double>();
		ArrayList<Character> puncuation = new ArrayList<Character>();
		String soft = "aehilmnorsuwv";
		String punc = ".?,!@#$%^&*()_+=/_~";
		//char[] soft = {'a', 'e', 'h', 'i', 'l', 'm', 'n', 'o', 'r', 's', 'u', 'w', 'y'};
		//char[] hard = {'b', 'c', 'd', 'f', 'g', 'j', 'k', 'p', 'q', 't', 'v', 'x', 'z'};
		//char[] punc = {'.', '?', ',', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '/', '_', '~'};
		String[] positive = {"advantageous","bright","favorable","favorite","felicitous","fortunate","golden","happy","hopeful","lucky",
								"opportune","promising","propitious","prosperous","rosy","timely","love","Know","Too","good","lol","like",
								"recipe","healthy","cookies","wedding","ideas","thanks","tonight","tomorrow","best","hope","nice","yes","fun",
								"happy","free","sure","sleep","awesome","cool","playing","gd","wow","won","great","proud","welcome","welcoming",
								"greatest","amazing","congrats","congratz","gratz","bae","magical","haha","fav","fam","friends","baby","birthday",
								"family","blessed","energy","inspiring","inspired","thriving","rad","cool","luck","lucky","sun","eating","share",
								"festival","laugh","party", "liked"}; 
		String[] negative = {"Work","sick","wrong","rain","no","must","why","morning","working","trying","feel","media","life","never","bad",
							"die","school","snow","miss","part","kill","death","murder","crying","forget", "the"};
		int[] randInt = new int[4];
		TweetStats[] twoots = new TweetStats[4];

		//String[] musicText = new String[4];
		int temp = 0, hardCount = 0, softCount = 0, happy = 0, sad = 0, capsCount = 0, charCount = 0, happySadMeter = 0;
		double tempo = 0;
		
		tweets = tweetRetrieve(15, args);
		if(tweets.size() < 15){
			System.out.println(tweets.size());
			printer.println("0");
			printer.close();
			return new TweetStats[0];
		}
		else printer.println("1");
		
		Collections.shuffle(tweets);
		
		for(Status tweet : tweets){
			tweetText.add(tweet.getText());
			tweetUser.add(tweet.getUser().getScreenName());
		}
		for(int i=0; i<4; ++i){
			temp = randInt[i];
			printer.println(tweetUser.get(i));
			printer.println(tweetText.get(i));
			musicText.add(i,tweetText.get(i));
		}
		printer.close();
		
		for(int k=0; k<4; ++k){
			splitText.addAll(Arrays.asList(((musicText.get(k)).split(" "))));
			arrayLength = ccounter(splitText);
			arrayDouble = normalize(arrayLength);
			System.out.println(arrayDouble);
			for(String str : splitText){
				//System.out.print(str + " ");
				for(String emo : positive){
					if(str.compareToIgnoreCase(emo) == 0) ++happy;
				}
				for(String emo : negative){
					if(str.compareToIgnoreCase(emo) == 0) ++sad;
				}
				for(int i=0; i<str.length(); ++i){
					++charCount;
					if(Character.isUpperCase(str.charAt(i))) ++capsCount;
					if(punc.indexOf(str.charAt(i)) != -1) puncuation.add(str.charAt(i));
					else{
							if(soft.indexOf(str.charAt(i)) != -1) ++softCount;
							else ++hardCount;
					}
				}
			}
			happySadMeter = 50 + (happy - sad) * 5;
			tempo = 120 + 80 * Math.log((double)hardCount/(double)softCount);
			if(happySadMeter > 100) happySadMeter = 100;
			else if(happySadMeter < 0)happySadMeter = 0;
			if (tempo > 220) tempo = 220;
			else if (tempo < 40) tempo = 40;
			System.out.println(hardCount);
			System.out.println(softCount);
			System.out.println(tempo);
			twoots[k] = new TweetStats(hardCount, softCount, capsCount, 
					charCount, happySadMeter, arrayDouble,
					tweetUser.get(k), tweetText.get(k));
			hardCount = 0;
			softCount = 0;
			happy = 0;
			sad = 0;
			capsCount = 0;
			charCount = 0;
			happySadMeter = 0;
			arrayDouble = new ArrayList<Double>();
			splitText = new ArrayList<String>();
		}
		
		return twoots;
		//System.out.println(hardCount);
		//System.out.println(softCount);
		//System.out.println(happy);
		//System.out.println(sad);
	}

	public static int[] rand4(int numTweets){
		int[] output = new int[4];
		Random rand = new Random();
		//int next = 0;
		IntStream randInts = rand.ints(4, 0, numTweets);
		output = randInts.toArray();
		/*for(int i=0; i<4; ++i){
			next = 
			while()
			output[i] = 
		}*/
		return output;
	}
	public static List<Status> tweetRetrieve(int numTweets, String hash){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("JBIUrsLbKPatKhdvPwwWlBSDz")
		  .setOAuthConsumerSecret("DZxOl0sQY60WmJ96ZieDPVasfguuJf5wmTDDjRVVtpB3iLLBQs")
		  .setOAuthAccessToken("3641149828-YpaVpqOj0aRWu16qElbgVZuGSp1EwE39SaJYjRv")
		  .setOAuthAccessTokenSecret("qnPzxGXTvNEHlD03LbchDNCc5BVLV8ifsFM83XkW7LXCQ");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		List<Status> tweets = new LinkedList<Status>();
		int count = 0;
		
		try{
			Query query = new Query(hash);
			//query.setResultType(Query.RECENT);
			query.setCount(100);
			QueryResult result;
			result = twitter.search(query);
			List<Status> tweetsAll = result.getTweets();
				do{
					for (Status tweet : tweetsAll){
						if(tweet.isRetweet()) tweets.remove(tweet);
						else{
						System.out.println(tweet.getUser().getScreenName() + "-" + tweet.getText());
						tweets.add(tweet);
						if(count >= numTweets) return tweets;
						++count;
						}
					}
				}
			while ((query = result.nextQuery()) != null && count < 15);
			return tweets;
		}
		catch(TwitterException te){
			tweets = null;
			te.printStackTrace();
			System.out.println("Failed to find Tweets");
			return tweets;
		}
	}
	
	public static ArrayList<Integer> ccounter(ArrayList<String> tweet){
        
        ArrayList<Integer> lengtharray = new ArrayList<Integer>();
        //Function to Count Number of Characters
        String word;
        int count = 0;
        int wordlength = 0;
            while (count < tweet.size()){
                word = tweet.get(count);
                //System.out.println(word);
                wordlength = word.length();
                //System.out.println(wordlength);
                lengtharray.add(wordlength);
                //System.out.println(lengtharray);
                count = count + 1; 
    
            }
            
              return lengtharray;
    }
    
    
    public static ArrayList<Double> normalize(ArrayList<Integer> length) {
        
        //ArrayList<String> tweet = new ArrayList<String>();
       //ArrayList<Integer> length = new ArrayList<Integer>();
       ArrayList<Double> normlength = new ArrayList<Double>();
      
       //length.add(1);
       //length.add(2);
       //length.add(4);
       //length.add(7);
       //length.add(10);
       //length.add(5);
       
       int count = 0;
       double max,min;
       max = length.get(0);
       //min = length.get(0);
       while(count < length.size()){
           //initial values for max and min (dont need max)
           if (length.get(count) > max){
               max = length.get(count);
            
           //if (length.get(count) < min){
             //  min = length.get(count);
               
           }
           
           count = count + 1;
        }  
       int total = 0;
       count = 0;    
       while (count < length.size()){
           total =  total + length.get(count);
           
           normlength.add(length.get(count)/max);
           count = count + 1;
       
           
           
        }
       double average = total/normlength.size();
       
        //System.out.println(average);
        //System.out.println(normlength);   
        
        //round to nearest 1.25 function
        
        count = 0;
        
        while (count < normlength.size()){
            normlength.set(count,normlength.get(count)*average);
            count++;
            
          
        }
        double token = normlength.get(0);
        
      
        while (count <normlength.size()){
            token = normlength.get(count);
            token = Math.round(token * 2);
            token = token / 2;
            normlength.set(count,token);
            
            count = count + 1;    
        }
    
        //System.out.println(normlength);
        
        count = 0;
        while (count < normlength.size()){
            
            if (normlength.get(count) < 0.25){
               normlength.set(count,0.0);
            }
            else if (normlength.get(count) < 0.75){
                normlength.set(count,0.5);
            }
            else if (normlength.get(count) < 4){
                    
                normlength.set(count,(double)Math.round(normlength.get(count)));    
                    }
            else{
                normlength.set(count,4.0);        
                
                 
                       
                           }
            count++;
        }
            //System.out.println(normlength);
        return normlength;
    }
}


