import java.awt.Color;

public final class ColorPalette{
   private static final Color SHIP = new Color(102, 102, 102);
   private static final Color HITSHIP = new Color(154, 51, 52);
   private static final Color WATER = new Color(145, 195, 220);
   private static final Color FLAG = new Color(255, 71, 76);
   private static final Color BACKGROUND = new Color(52, 68, 64);
   private static final Color WATERHIT = new Color(0, 0, 139);
   private static final Color SUNKSHIP = new Color(139, 0, 0);

   // Get Ship color
   public static Color getShipColor(){
      return SHIP;
   }

   // Get HitShip color
   public static Color getHitShipColor(){
      return HITSHIP;
   }

   // Get Water color
   public static Color getWaterColor(){
      return WATER;
   }

   // Get Flag color
   public static Color getFlagColor(){
      return FLAG;
   }

   // Get Background color
   public static Color getBackgroundColor(){
      return BACKGROUND;
   }
   
   public static Color getWaterHit(){
      return WATERHIT;
   }
   
   public static Color getSunkShip(){
      return SUNKSHIP;
   }

}