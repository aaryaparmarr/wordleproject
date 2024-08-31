package com.example;
import java.util.ArrayList;

import java.util.List;


import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;

public class statisticsController {

    @FXML
    private TextField Played_tf, Win_tf, Current_tf, Max_tf;
    
    @FXML
    private ProgressBar one_br, three_br, four_br, five_br, two_br, six_br;
    

    private int tries;
    private List<String> guesswords;
    private int plays;
    private String selectedWord;


    public statisticsController(int tries, List<String> guesswords, int plays, String selectedWord) {
        this.tries = tries;
        this.guesswords = new ArrayList<>(guesswords); // Create a new instance of guesswords to avoid modifying the original list
        this.plays = plays;
        this.selectedWord = selectedWord;
    }
    public void initialize(){
        setStats();
        one_br.setProgress(0); // Set initial progress to 0
        two_br.setProgress(0);
        three_br.setProgress(0);
        four_br.setProgress(0);
        five_br.setProgress(0);
        six_br.setProgress(0);
        if(tries==1){
            one_br.setProgress(plays / 20.0);
            one_br.progressProperty().unbind(); // Unbind the progress property
        } else if(tries==2){
            two_br.setProgress(plays / 20.0);
            two_br.progressProperty().unbind(); // Unbind the progress property
        }
        else if(tries==3){
            three_br.setProgress(plays / 20.0);
            three_br.progressProperty().unbind();
        }  else if(tries==4){
            four_br.setProgress(plays / 20.0);
            four_br.progressProperty().unbind();
        } else if(tries==5){
            five_br.setProgress(plays / 20.0);
            five_br.progressProperty().unbind();
        } else{
            six_br.setProgress(plays / 20.0);
            six_br.progressProperty().unbind();
        }
      
 
    }

    // Getter methods for accessing the variables
    public int getTries() {
        return tries;
    }

    public List<String> getGuesswords() {
        return new ArrayList<>(guesswords); // Return a copy of guesswords to avoid modifying the original list
    }

    public int getPlays() {
        return plays;
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    @FXML
    private void setStats() {
        Played_tf.setText(String.valueOf(plays));
        Current_tf.setText("1");
        Win_tf.setText("100");
        Max_tf.setText("20");
        
        
    }
}


