public class Battleship{
   private final String name;
   private final int length;
   private boolean isSunk;
   private Square[] shipParts;
   private Direction dir;
   
   public Battleship(String name, int length){
      this.name = name;
      this.length = length;
   }
   
   public void setDirection(Direction dir){
      this.dir = dir;
   }
   
   public boolean checkIsSunk(){
      if(shipParts.size() == 0){
         return true;
      }
   }
   
   public void hitShip(Square shipPart){
      shipParts.remove(shipParts.indexOf(shipPart));
   }
}