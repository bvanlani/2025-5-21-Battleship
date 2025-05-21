import java.util.*;

public class Battleship{
   private final String name;
   private final int length;
   private boolean isSunk;
   private ArrayList<Square> shipParts;
   private Direction dir;
   
   public Battleship(String name, int length){
      this.name = name;
      this.length = length;
      this.shipParts = new ArrayList<>();
   }
   
   public void setDirection(Direction dir){
      this.dir = dir;
   }
      
   public Direction getDirection(){
      return dir;
   }
   
   public boolean checkIsSunk(){
      if(isSunk) return isSunk;
      
      if(shipParts.size() == 0){
         isSunk = true;
         return isSunk;
      }
      
      return false;
   }
   
   public void removeShipPart(Square shipPart){
      shipParts.remove(shipParts.indexOf(shipPart));
   }
   
   public void addShipPart(Square shipPart){
      shipParts.add(shipPart);
   }
   
   public int getLength(){
      return length;
   }
}