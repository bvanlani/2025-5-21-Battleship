import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Display extends JFrame{
   private final Grid playerGrid;
   private final Grid targetGrid;
   private final java.util.List<Battleship> shipsToPlace = new ArrayList<>();
   private Direction currentDirection = Direction.RIGHT;
   private boolean placementPhase = true;
   private JLabel shipLengthLabel;
   private boolean isReady = false;
   
   private final Color background = new Color(17, 17, 17);
   
   public Display(String player){
      setTitle(player);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
     
      JButton rotateButton = new JButton("Rotate (" +currentDirection + ")");
      rotateButton.addActionListener(e -> {
         currentDirection = currentDirection.next();
         rotateButton.setText("Rotate (" + currentDirection + ")");
      });
     
      shipLengthLabel = new JLabel("-");
      shipLengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
     
      JPanel controlPanel = new JPanel(new BorderLayout());
      controlPanel.add(shipLengthLabel, BorderLayout.CENTER);
      controlPanel.add(rotateButton, BorderLayout.EAST);
     
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
         System.out.println("Invalid ship placement.");
         return;
      }
     
      playerGrid.placeShipTiles(startRow, startCol, length, currentDirection, shipsToPlace.get(0));
      shipsToPlace.remove(0);
     
      if(shipsToPlace.isEmpty()){
         placementPhase = false;
         shipLengthLabel.setText("-");
         isReady = true;
      }else{
         shipLengthLabel.setText("" + shipsToPlace.get(0).getLength());
      }
   }
   
   public boolean getISReady(){
      return isReady;
   }  
   
   public Grid getTargetGrid(){
      return targetGrid;
   }
}
