import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel{
   private final Square[][] board;
   private final int rows;
   private final int cols;
   
   private final Color water = new Color(73, 93, 121);
   private final Color ship = new Color(87, 87, 87);
   private final Color background = new Color(17, 17, 17);
   
   private final int size = 20;
   
   public Grid(int rows, int cols){
      this.rows = rows;
      this.cols = cols;
      setLayout(new GridLayout(rows, cols, 2, 2));
      setBackground(ColorPalette.BACKGROUND);
      board = new Square[rows][cols];
      
      for(int r=0;r<rows;r++){
         for(int c=0;c<cols;c++){
            board[r][c] = new Square(r, c, false, "water");
            board[r][c].setPreferredSize(new Dimension(size, size));
            board[r][c].setOpaque(true);
            board[r][c].setBackground(ColorPalette.WATER);
            board[r][c].setBorderPainted(false);
            this.add(board[r][c]);
         }
      }
   }  
   
   public Square getSquare(int row, int col){
      return board[row][col];
   }
   
   public Square[][] getBoard(){
      return board;
   }
   
   public void resetGrid(){
      for(JButton[] row : board){
         for(JButton square : row){
            square.setBackground(null);
            square.setEnabled(true);
         }
      }
   }
   
   public void placeShipTiles(int startRow, int startCol, int length, Direction dir, Battleship bs){
      for(int i=0;i<length;i++){
         int r = startRow + dir.dRow * i;
         int c = startCol + dir.dCol * i;
         board[r][c].setType("ship");
         bs.addShipPart(board[r][c]);
      }
   }
   
   public boolean canPlaceShip(int startRow, int startCol, int length, Direction dir){
      for(int i=0;i<length;i++){
         int r = startRow + dir.dRow * i;
         int c = startCol + dir.dCol * i;
         
         if(r < 0 || r >= rows || c <0 || c >= cols) return false;
         if ("ship".equals(board[r][c].getType())) return false;
      }
      return true;
   }
}