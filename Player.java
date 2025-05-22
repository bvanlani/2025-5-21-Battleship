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
   public Battleship[] getFleet () {
      return fleet;
   }
   
   public Battleship getIndexFleet (int index) {
      if (index < 0 || index > fleet_size - 1) return null;
      
      return fleet[index];
   }
   
   public boolean getIsTurn () {
      return is_turn;
   }
   
   // Setters
   public void setFleet (Battleship[] in_fleet) {
      fleet = in_fleet;
   }
  
   public void setIndexFleet (Battleship in_ship, int index){
      if (index < 0 || index > fleet_size - 1) return;
      
      fleet[index] = in_ship;
   }
   
   public void setTurn (boolean state) {
      is_turn = state;
   }
   
   // Standard Functions
   public void initializeFleet () {
      ;
   }

}