// Author: Tyerone Chen
// Create Date: 5/20/2025
// Update Date: 5/20/2025

// Imports
import java.util.*;

// Player class which handles and stores information such as battle ships, as well as actions

public class Player {
   // Constants
   
   // Variables
   private static final int fleet_size = 5;
   private Battleship[] fleet;
   private boolean is_turn;
   
   // Constructors
   public Player() {
      this.fleet = new Battleship[5];
      this.is_turn = false;
   }
   
   // Functions
   // Getters

   // Returns the whole fleet and the ships data
   /*
   @return Battleship[] -> the current whole fleet
   */
   public Battleship[] getFleet () {
      return fleet;
   }

   // Returns a certain ship inside of the fleet, checks index is within range
   /*
   @param int index -> the index in fleet that will be recieved
   @return Battleship -> the Battlship in fleet
   */
   public Battleship getIndexFleet (int index) {
      if (index < 0 || index > fleet_size - 1) return null;
      
      return fleet[index];
   }

   /*
   @return boolean -> the current turn state of the player
   */
   // Gets if its the players turn
   public boolean getIsTurn () {
      return is_turn;
   }
   
   // Setters
   // Sets the whole fleet with an inputed fleet
   /*
   @param Battleship[] in_fleet -> input fleet to set the current fleet
   */
   public void setFleet (Battleship[] in_fleet) {
      fleet = in_fleet;
   }

   // Set a certain battleship in fleet, checks index is within range
   /*
   @param Battleship in_ship -> the input ship to be stored in the fleet
   @param int index -> the index location in fleet where the ship will be stored
   */
   public void setIndexFleet (Battleship in_ship, int index){
      if (index < 0 || index > fleet_size - 1) return;
      
      fleet[index] = in_ship;
   }

   // Sets turn state of player
   /*
   @param boolean state -> the input bool state that will be set for the player
   */
   public void setTurn (boolean state) {
      is_turn = state;
   }
   
   // Standard Functions
   // Initialize and setups fleet, might be deprecated
   public void initializeFleet () {
      ;
   }

}
