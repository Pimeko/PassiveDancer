public class Utilities
{
  public static double GetRatioW(Image2D img)
  {
    return img.Width()/(double)ContentManager.WindowWidth;
  }
  
  public static double GetRatioW(double width)
  {
    return width/(double)ContentManager.WindowWidth;
  }
  
  public static double GetRatioH(Image2D img)
  {
    return img.Height()/(double)ContentManager.WindowHeight;
  }
  
  public static double GetRatioH(double height)
  {
    return height/(double)ContentManager.WindowHeight;
  }
  
  public static double RadToDeg(double a)
  {
    return a*(180/Math.PI);
  }
  
  public static double DegToRad(double a)
  {
    return a*(Math.PI/180);
  }
  
  public static boolean IntToBool(int x)
  {
    return (x==0)?false:true;
  }
  
  public static int BoolToInt(boolean x)
  {
    return (x)?1:0;
  }
  
  public enum Speed
  {
    Slow, MediumSlow, Medium, MediumFast, Fast;
  }
}