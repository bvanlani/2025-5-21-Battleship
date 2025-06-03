package dev.bvanlani.battleship;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.rmi.*;

public class Player extends UnicastRemoteObject implements PlayerInterface{

   static Scanner scanner = new Scanner(System.in);

   public static void main(String[] args) {
      try{
         System.out.print("Enter Server IP: ");
         String IP = scanner.nextLine();


         Registry registry = LocateRegistry.getRegistry(IP, 1234);
         GameInterface gm = (GameInterface) registry.lookup("GameManager");


         System.out.println("Connected to the server.\n");
         System.out.print("Enter your name: ");
         String name = scanner.nextLine();

         Player player = new Player(name);

      }catch(Exception e){
            e.printStackTrace();
      }
   }


   // player variable
   private ArrayList<Battleship> fleet = new ArrayList<>();
   private final String name;
   
   private final Grid playerGrid;
   private final Grid targetGrid;
   private final java.util.List<Battleship> shipsToPlace = new ArrayList<>();
   private Direction currentDirection = Direction.RIGHT;
   private boolean isReady = false;
   private final Display dis;
   private GameManager gm;
   
   public Player(String name) throws RemoteException {
      super();
      this.name = name;
     
      playerGrid = new Grid(10, 10);
      targetGrid = new Grid(10, 10);

      this.dis = new Display(name, this);

      setupShipStorage();
   }
   
   private void setupShipStorage(){
      shipsToPlace.add(new Battleship("Carrier", 5));
      shipsToPlace.add(new Battleship("Battleship", 4));
      shipsToPlace.add(new Battleship("Cruiser", 3));
      shipsToPlace.add(new Battleship("Submarine", 3));
      shipsToPlace.add(new Battleship("Destroyer", 2));
     
      dis.setShipLengthLabel("" + shipsToPlace.get(0).getLength());
      enablePlacementMode();
   }
   
   private void enablePlacementMode(){
      JButton[][] board = playerGrid.getBoard();
      for(int r = 0; r< board.length;r++){
         for(int c = 0;c < board[r].length; c++){
            int row = r;
            int col = c;
            board[r][c].addActionListener(e -> handleShipPlacement(row, col));
         }
      }
   }
   
   private void handleShipPlacement(int startRow, int startCol){
      if(shipsToPlace.isEmpty()) return;
     
      int length = shipsToPlace.get(0).getLength();
     
      if(!playerGrid.canPlaceShip(startRow, startCol, length, currentDirection)){
      System.out.println("Trying to place ship at: (" + startRow + "," + startCol + ")");

         System.out.println("Invalid ship placement.");
         return;
      }
     
      playerGrid.placeShipTiles(startRow, startCol, length, currentDirection, shipsToPlace.get(0));
      shipsToPlace.get(0).setDirection(currentDirection);
      shipsToPlace.remove(0);
     
      if(shipsToPlace.isEmpty()){

         dis.readyState();

      }else{
         dis.setShipLengthLabel("" + shipsToPlace.get(0).getLength());
      }
   }

   public void setGM(GameManager gm) {
      this.gm = gm;
   }

   public void setTargetSquareType(int row, int col, String type) {
      targetGrid.setSquare(row, col, type);
   }

   public String getPlayerSquareType(int row, int col) {
      return playerGrid.getSquare(row, col).getType();
   }

   public String getName(){
        return name;
   }

   public void gameRun(){
      gm.gameRun(this);
   }
   
   public boolean getIsReady(){
      return isReady;
   }  
   
   public Grid getPlayerGrid(){
      return playerGrid;
   }
   
   public Grid getTargetGrid(){
      return targetGrid;
   }
   
   public void setIsReady(boolean isReady){
      this.isReady = isReady;
   }

   public Direction getDirection(){
      return currentDirection;
   }

   public void setDirection(Direction dir){
        this.currentDirection = dir;
   }

   public Display getDisplay(){
      return dis;
   }
   
   // Player Methods
   
   // Returns the whole fleet and the ships data
   /*
   @return Battleship[] -> the current whole fleet
   */
   public ArrayList<Battleship> getFleet () {
      return fleet;
   }
   
   // Sets the whole fleet with an inputted fleet
   /*
   @param Battleship[] in_fleet -> input fleet to set the current fleet
   */
   public void setFleet (ArrayList<Battleship> in_fleet) {
      fleet = in_fleet;
   }
   
   public void removeShip(Battleship ship){
      fleet.remove(ship);
   }

   public void initializeFleet(){
      setFleet(playerGrid.getShips());
   }

   public int fleetSize(){
      return fleet.size();
   }

   public void addBoardEventListeners(){
      Square[][] opBoard = targetGrid.getBoard();

      for (int row = 0; row < opBoard.length; row++) {
         for (int col = 0; col < opBoard[0].length; col++) {
            Square opSquare = opBoard[row][col];

            opSquare.addActionListener(e -> {
               if(gm.getCurrentPlayer() == this) {
                  gm.callHit(opSquare.getPositionX(), opSquare.getPositionY());
               }
            });
         }
      }
   }


   public void receivePlayerHit(int row, int col){
      playerGrid.getSquare(row, col).hitSquare();
      checkIfShipSunk();
   }

   public void receiveTargetHit(int row, int col){
      targetGrid.getSquare(row, col).hitSquare();
   }

   public void checkIfShipSunk(){
       for (int i=fleet.size()-1;i>=0;i--) {

           if (fleet.get(i).checkIsSunk()) {
               ArrayList<Square> shipSquares = fleet.get(i).getShipParts();

               for (Square shipSquare : shipSquares) {
                   shipSquare.setType("sunk");
               }

               gm.sinkBattleship(this, fleet.get(i).getLength(), fleet.get(i).getDirection(), fleet.get(i).getStartX(), fleet.get(i).getStartY());

               removeShip(fleet.get(i));

               gm.checkWin();
           }
       }
   }

   public void sinkBattleship(int length, Direction dir, int startX, int startY) {
      int x = startX;
      int y = startY;

      for(int i = 0; i < length; i++) {

         targetGrid.getSquare(x, y).setType("sunk");

         x += dir.dRow;
         y += dir.dCol;
      }
   }
}