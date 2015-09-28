package somehow.mhacks;

import java.awt.Insets;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author Nevin
 */
public class Main extends Application {
    
	private class TweetPane extends ScrollPane{
		private GridPane tweet_area;
		public TextArea[] tweets;
		
		public TweetPane(){
			super();
			tweet_area = new GridPane();
			tweet_area.setAlignment(Pos.TOP_LEFT);
			tweet_area.setVgap(4);
			
			tweets = new TextArea[4];
			for(int i = 0; i < 4; ++i){
				tweets[i] = new TextArea();
				tweets[i].setEditable(false);
				tweets[i].setWrapText(true);
				tweets[i].setMaxHeight(60);
				tweet_area.add(tweets[i], 0, i);
			}
			setContent(tweet_area);
		}
	} 
	
	TweetPane tweet_view;
	
	public boolean showTweets(TweetStats[] data){
		for(int i = 0; i < 4; ++i){
			tweet_view.tweets[i].setText(data[i].user + ": " + data[i].tweet);
		}
		return true;
	}
	
	public void startProcess(String s){
        System.out.println(s);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Songify");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        Text scenetitle = new Text("Welcome to Songify!");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 1, 1);

        Label userName = new Label("Enter a Hashtag:                                                #");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        /*Label pw = new Label("Enter a Hashtag: #");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);*/

        Button btn = new Button("Songify it!");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
        grid.add(new Label("Tweets:"), 0, 5);
        tweet_view = new TweetPane();
        grid.add(tweet_view, 0, 6, 2, 1);
        /*tweet_txt = FXCollections.observableArrayList();
        tweet_view = new ListView();
        tweet_view.setPrefSize(300, 200);
        tweet_view.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			public ListCell<String> call(ListView<String> lv) {
				ListCell<String> lc = new ListCell<String>();
				lc.setGraphic(new TextArea());
				return lc;
			}
        	
        });
        
        grid.add(tweet_view, 0, 6, 2, 1);*/
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
        @Override
        public void handle(ActionEvent e) {
                //actiontarget.setFill(Color.FIREBRICK);
                //actiontarget.setText("Hashtag #" + userTextField.getText() + " entered.");
                Twitter_data td = new Twitter_data();
                if(userTextField.getText().charAt(0) == '#'){
                    try {
                        TweetStats[] data = td.main(userTextField.getText()).clone();
                        if(showTweets(data))
                    		MusicProcessor.songify(data);
                    	
                        //startProcess(userTextField.getText());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                    	TweetStats[] data = td.main("#" + userTextField.getText()).clone();
                    	if(showTweets(data))
                    		MusicProcessor.songify(data);
                        //startProcess("#" + userTextField.getText());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        Scene scene = new Scene(grid, 600, 550);
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
