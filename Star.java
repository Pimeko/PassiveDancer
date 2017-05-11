import java.awt.Color;

public class Star
{
  public double x, y, speed;
  
  public Star(double x, double y)
  {
    this.x = x;
    this.y = y;
    speed = 0.005 + Math.random()/1200;
  }
  
  public Star(double x, double y, int actual_level)
  {
    this.x = x;
    this.y = y;
    speed = actual_level*0.0008 + 0.004 + Math.random()/1200;
  }
  
  public void Update()
  {
    if(y<0)
      y=1;
    else
      y-=speed;
  }
  
  public void Draw()
  {
    StdDraw.picture(x, y, ContentManager.star.path);
  }
}