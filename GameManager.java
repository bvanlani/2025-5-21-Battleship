/*
 * This class represents a game of Battleship. It contains the 
 *
 * Code written by Toryn Bieri & Ethan Benzaquen.
 */

import java.util.*;
//Insert other imports here when we figure out what we will need.

public class GameManager{

   //Contains a boolean variable that represents whether or not the game is running.
   private boolean gameRunning;
   
   //Should only contain a '1' or a '2', with the numbers representing which player's turn it is.
   private int currentTurn;
   
   private Player player1; 
   
   private Player player2;
   
   private Display player1Display;
   
   private Display player2Display;
   
   
   //Contains a boolean variable, which represents whether either of the players have had all their ships sunk or not.
   private boolean isGameOver;
   
   /*
    * Creates a new instance of a GameManager object, updating the instance variables by using the objects in the parameters. - Toryn
    *
    * @param player1 The first player that will be participating in the game.
    * @param player2 The second player that will be participating in the game.
    * @param display The window where Battleship will be played.
    */
   public GameManager(Player player1, Player player2, Display display1, Display display2){
      gameRunning = false;
      
      this.player1 = player1;
      this.player2 = player2;
      
      player1Display = display1;
      player2Display = display2;
      
      currentTurn = 1;
      
      isGameOver = false;
      
   }
   
   
   /*
    * Updates the currentTurn object, so that it changes to either 1 or 2, depending on which player's turn it is about to be. - Toryn
    */
   private void switchTurns(){
      if(player1.getIsTurn()){
         player1.setTurn(false);
         player2.setTurn(true);
      }
      else{
       player2.setTurn(false);
       player1.setTurn(true);
      }
   }
   /*
    *
    * Depending on which player's turn it is, the code will run through the fleets each player has and check if any of their ships have not been sunk yet.
    * If there is at least one ship that has not been sunk yet, the code will return false. Otherwise, the code will return true. - Toryn
    *
    */
   private boolean checkForGameOver(){
      if(currentTurn == 1){
         for(Battleship s : player1.getFleet()){ 
            if(s.checkIsSunk() == false){
               return false;
            }
         }
      }
      else{
         for(Battleship s: player2.getFleet()){
            if(s.checkIsSunk() == false){
               return false;
            }
         }
      }
      return true;
   }
   
   private void player1Turn(){
      player1Display.setVisible(true);
   }
   
   private void player2Turn(){
   
   }
   
   /*
    * Starts the game of Battleship.
    */
   public void startGame(){
      while(!(isGameOver)){
       
       }
   }
   
}
