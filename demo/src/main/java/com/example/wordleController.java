package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class wordleController {

    @FXML
    private TextField tf_11, tf_12, tf_13, tf_14, tf_15,tf_21, tf_22, tf_23, tf_24, tf_25, tf_31, tf_32, tf_33, tf_34, tf_35,
                      tf_41, tf_42, tf_43, tf_44, tf_45, tf_51, tf_52, tf_53, tf_54, tf_55, tf_61, tf_62, tf_63, tf_64, tf_65;
   
    
    @FXML
    private Button Delete_But, Reset_But, Statistics_ID, Enter_But,
                   Q, W, E, R, T, Y, U, I, O, P,
                   A, S, D, F, G, H, J, K, L,
                   Z, X, C, V, B, N, M;
    private List<String> wordList = new ArrayList<>();
    private String selectedWord;

    @FXML
    private Label WarningLabel;

    private int row = 1;
    private int col = 1;
    private Map<Integer, Character> colorWordMap;
    private int tries = 0;
    private List<String> guesswords = new ArrayList<>();
    private int plays=1;

    public void initialize(){
        tf_11.requestFocus();
        loadWordList();
        selectedWord = selectRandomWord();
        System.out.println(selectedWord);
        colorWordMap = new HashMap<>();
        for(int i = 0; i<selectedWord.length();i++){
            colorWordMap.put(i, selectedWord.charAt(i));
        }
    }

    private void loadWordList() {
        try (BufferedReader br = new BufferedReader(new FileReader("demo/src/main/java/com/example/wordle.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each word is in quotes and separated by commas
                String[] words = line.split(",");
                for (String word : words) {
                    wordList.add(word.trim().replaceAll("\"", "")); // Remove leading/trailing spaces and quotes
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        }
    }

    private String selectRandomWord() {
        // Shuffle the wordList to ensure randomness
        Collections.shuffle(wordList);
        // Select the first word from the shuffled list
        selectedWord = wordList.get(0);
        return selectedWord;
    }



    @FXML
    private void handlekeyboardclick(ActionEvent event){
        Button button = (Button) event.getSource();
        String letter = button.getText();
        getTextTextField().setText(letter);
        moveMouse();

    }

    @FXML
    private void handleDeleteBut(ActionEvent event){
        getTextTextField().setText("");
        moveMouseBack();
    }

    @FXML
    private void handleStatsBut(ActionEvent event){
        try{
        statisticsController statisticsController = new statisticsController(tries, guesswords, plays, selectedWord);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statistics.fxml"));
        fxmlLoader.setController(statisticsController);

        Parent root = fxmlLoader.load();
        
        Stage stage = new Stage();
        stage.setTitle("Stats");
        stage.setScene(new Scene(root));
        stage.show();
        }
        catch(IOException e) {
            e.printStackTrace(); // Handle file reading errors)

        }

    }

    @FXML
    private void handleEnterBut(ActionEvent event){
        String userGuess = getUserGuess(row);
        clearStyles(row);
        Boolean vis = visibleWarningLabel(userGuess);
        WarningLabel.setVisible(!vis);
        if(col == 5 && !isRowDisabled()){
            disableCurrRow();
            row++;
            col=1;
            if(row<=5){
                getTextTextField().requestFocus();
                Enter_But.setDisable(true);
            }
        } else{
            moveMouse();
        }
        checkGuess(userGuess);
        //for storing - first have to check if the game is won
        boolean won = true;
        for(int i = 0; i< userGuess.length(); i++){
            char guessLetter = userGuess.charAt(i);
            Character rightLetter = colorWordMap.get(i);
            if(guessLetter != Character.toUpperCase(rightLetter)){
                won = false;
                tries++;
                guesswords.add(userGuess);
                break;
            }
        }
        if(won){
            saveAttempts(selectedWord, tries, guesswords);
        }
    
    }

    
    
    private void saveAttempts(String selectedWord2, int tries2, List<String> guesswords2) {
        try{
            FileWriter store = new FileWriter("storeResults_txt", true);
            store.write("Random Word: " + selectedWord2 + "\n");
            store.write("Attempts: " + tries2 + "\n");
            store.write("Guess Words: ");
            for(String guess: guesswords2){
                store.write(guess +", ");
            }
            store.write("\n\n\n");
            store.close();
        }
        catch(IOException i){
            i.printStackTrace();
        }
        
    }

    private Boolean visibleWarningLabel(String userGuess){
        boolean isInWordList = wordList.contains(userGuess.toLowerCase());
        return isInWordList;
    }

    @FXML 
    private void handleResetBut(ActionEvent event){
        clearAllTextFields();
        resetGameState();
        selectedWord = selectRandomWord();
        updateColorWordMap();
        WarningLabel.setVisible(false);
        tries = 0;
        guesswords.clear();
        plays++;
        System.out.println(selectedWord); // Print the new selected word for debugging
    }

    private void clearAllTextFields() {
        for (int r = 1; r <= 6; r++) {
            for (int c = 1; c <= 5; c++) {
                TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + r + c);
                textField.setText("");
                textField.setStyle(""); // Clear any styling
                textField.setDisable(false); // Enable all text fields
            }
        }
    }

    private void resetGameState() {
        row = 1;
        col = 1;
        Enter_But.setDisable(false); // Enable the Enter button
    }

    private void updateColorWordMap() {
        colorWordMap.clear();
        for (int i = 0; i < selectedWord.length(); i++) {
            colorWordMap.put(i, selectedWord.charAt(i));
        }
    }

    private void moveMouse(){
        if(col<5){
            col++;
            getTextTextField().requestFocus();
            if(col==5){
                Enter_But.setDisable(false);
            }
        } else {
            Enter_But.setDisable(false);
        }
    }
    
    private void moveMouseBack(){
        if(col>1){
            col--;
            getTextTextField().requestFocus();
        }
    }

    private String getUserGuess(int r){
        String currentGuess = "";
        for(int c = 1; c <=5;c++){
            TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + r + c);
            currentGuess += textField.getText();
        }
        return currentGuess;
    }

    private void checkGuess(String userGuess){
        int curRow = row - 1;
        for(int i = 0; i< userGuess.length(); i++){
            char guessLetter = userGuess.charAt(i);
            Character rightLetter = colorWordMap.get(i);
            TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + curRow + (i+1));

            if(rightLetter==null){
                continue;
            }
            setTextFieldStyle(textField, guessLetter, rightLetter);
        }
    }
    private void setTextFieldStyle(TextField textField, char guessLetter, char rightLetter) {
        if(guessLetter == Character.toUpperCase(rightLetter)){
            textField.setStyle("-fx-background-color: green");  
        } else if(selectedWord.contains(Character.toString(Character.toLowerCase(guessLetter)))){
            textField.setStyle("-fx-background-color: yellow");  
        } else {
            textField.setStyle("-fx-background-color: gray");  
        }
    }
    private void clearStyles(int r){
        for(int i = 1; i<=5; i++){
            TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + r + i);
            textField.setStyle("");

        }
    }

    private boolean isRowDisabled(){
        TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + row + "1");
        return textField.isDisabled();
        
    }

    private TextField getTextTextField(){
            String tfId = "tf_" + row + col;
            return (TextField) Enter_But.getScene().lookup('#' + tfId);
        }


    private void disableCurrRow(){
        for(int i=1; i<=5;i++){
            TextField textField = (TextField) Enter_But.getScene().lookup("#tf_" + row + i);
            textField.setDisable(true);
        }
    }

}