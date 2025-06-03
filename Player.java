import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.*;

public class Player extends JFrame{

   // player variable
   private ArrayList<Battleship> fleet = new ArrayList<Battleship>();
   private String name;
   
   private final Grid playerGrid;
   private final Grid targetGrid;
   private final java.util.List<Battleship> shipsToPlace = new ArrayList<>();
   private Direction currentDirection = Direction.RIGHT;
   private boolean placementPhase = true;
   private JButton start;
   private boolean isReady = false;
   private JPanel controlPanel;
   private JButton rotateButton;
   private JLabel shipLengthLabel;
   private Consumer<JButton> onStartButtonCreated;
   
   public Player(String name){
      this.name = name;
      setTitle(name);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setResizable(false);
      setLayout(new BorderLayout());
     
      playerGrid = new Grid(10, 10);
      targetGrid = new Grid(10, 10);
     
      JPanel gridsPanel = new JPanel();
      gridsPanel.setLayout(new BoxLayout(gridsPanel, BoxLayout.Y_AXIS));
      gridsPanel.setBackground(ColorPalette.getBackgroundColor());
     
      gridsPanel.add(targetGrid);
      gridsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      gridsPanel.add(playerGrid);
      this.add(gridsPanel, BorderLayout.CENTER);
           
      rotateButton = new JButton("Rotate (" +currentDirection + ")");
      rotateButton.addActionListener(e -> {
         currentDirection = currentDirection.next();
         rotateButton.setText("Rotate (" + currentDirection + ")");
      });
     
      shipLengthLabel = new JLabel("-");
      shipLengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
     
      controlPanel = new JPanel(new BorderLayout());
      controlPanel.add(shipLengthLabel, BorderLayout.CENTER);
      controlPanel.add(rotateButton, BorderLayout.WEST);
     
      this.add(controlPanel, BorderLayout.SOUTH);
     
      this.pack();
      this.setVisible(true);
     
      setupShipStorage();
   }
   
   private void setupShipStorage(){
      shipsToPlace.add(new Battleship("Carrier", 5));
      shipsToPlace.add(new Battleship("Battleship", 4));
      shipsToPlace.add(new Battleship("Cruiser", 3));
      shipsToPlace.add(new Battleship("Submarine", 3));
      shipsToPlace.add(new Battleship("Destroyer", 2));
     
      shipLengthLabel.setText("" + shipsToPlace.get(0).getLength());
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
      shipsToPlace.remove(0);
     
      if(shipsToPlace.isEmpty()){
         placementPhase = false;
         shipLengthLabel.setText("-");
         
         controlPanel.remove(shipLengthLabel);
         controlPanel.remove(rotateButton);
         
         start = new JButton("Ready");
         controlPanel.add(start, BorderLayout.CENTER);
         
         controlPanel.revalidate();
         controlPanel.repaint();

         if (onStartButtonCreated != null) onStartButtonCreated.accept(start);
   
      }else{
         shipLengthLabel.setText("" + shipsToPlace.get(0).getLength());
      }
   }
  
   public java.util.List<Battleship> getShipsToPlace(){
      return shipsToPlace;
   }
   
   public JButton getStart(){
      return start;
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
   
   public int getGridRow(){
      return playerGrid.getRow();
   }
   
   // Player Methods
   
   // Returns the whole fleet and the ships data
   /*
   @return Battleship[] -> the current whole fleet
   */
   public ArrayList<Battleship> getFleet () {
      return fleet;
   }
   
   // Sets the whole fleet with an inputed fleet
   /*
   @param Battleship[] in_fleet -> input fleet to set the current fleet
   */
   public void setFleet (ArrayList<Battleship> in_fleet) {
      fleet = in_fleet;
   }
   
   public void removeShip(Battleship ship){
      if(fleet != null){
         
         for(int i = 0; i < fleet.size(); i++){
         
            if(fleet.get(i).equals(ship)){
               fleet.remove(i);
            }
         
         }
         
      }
   }
   
   public void setOnStartButtonCreated(Consumer<JButton> listener) {
      this.onStartButtonCreated = listener;
   }
}