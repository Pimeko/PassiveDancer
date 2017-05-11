import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Image2D
{
  String path;
  int width, height;
  
  public Image2D(String path)
  {
    this.path = path;
    BufferedImage img = null;
    try
    {
      img = ImageIO.read(new File(path));
      width = img.getWidth();
      height = img.getHeight();
    }
    catch(IOException ex)
    {
      System.out.println("Error loading the image " + path + ".");
    }
  }
  
  public int Width()
  {
    return width;
  }
  
  public int Height()
  {
    return height;
  }
  
  public String Path()
  {
    return path;
  }
}