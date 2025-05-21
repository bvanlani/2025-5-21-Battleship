import javax.swing.JButton;
import java.awt.Color;
public class Square extends JButton{


   //Instance Variables
   private int positionX;
   private int positionY;
   
   //Checks if square has been hit
   private boolean isHit;
   
   //The type of square
   private String squareType;
   
   //Constructor
   public Square(int positionX, int positionY, boolean isHit, String squareType){
         this.positionX = positionX;
         this.positionY = positionY;
         this.isHit = isHit;
         this.squareType = squareType;
   }
   
   //Getting positions
   public int getPositionX(){
      return positionX;
   }
   
   public int getPositionY(){
      return positionY;
   }
   
   //Getting if hit
   public boolean getIsHit(){
      return isHit;
   }
   
   //Sets square to hit
   public boolean hitSquare(){
      setEnabled(false);
      isHit = true;
      return true;
   }
   
   //Sets square to hit and changes color
   public boolean hitSquare(Color color){
      updateColor();
      setEnabled(false);
      isHit = true;
      return true;
   }
   
   //Get the type of the Square
   public String getType(){
      return squareType;
   }
   
   public void setType(String type){
      this.squareType = type;
      updateColor();
   }
   
   //Update color
   public void updateColor(){
      if(squareType == "ship"){
         setBackground(ColorPalette.SHIP);
      }else if(squareType == "water"){
         setBackground(ColorPalette.WATER);
      }
   }
   
   //get the current color
   public Color getCurrentColor(){
      return getBackground();
   }
}