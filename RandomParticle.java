/* This class allows to create a random particle that will move randomly 
 * on the screen at a specified speed and rotation. */

public class RandomParticle
{
  protected double x, y, speed_x, speed_y, speed_rot, current_angle;
  public double X() { return x; }
  public double Y() { return y; }
  protected int direction;
  protected boolean isAlive;
  public boolean IsAlive() { return isAlive; }
  private Image2D image;
  
  public RandomParticle(Image2D image, Utilities.Speed speed_move_type, Utilities.Speed speed_rot_type)
  {
    this.image = image;
    
    x=(int)(2*Math.random());
    y=Math.random();
    direction = x==0?1:-1;
    x-=direction*(Utilities.GetRatioW(image.Width()/2));
    y+=direction*(Utilities.GetRatioH(image.Height()/2));
    speed_x = GenerateSpeedMove(speed_move_type);
    speed_y = GenerateSpeedMove(speed_move_type);
    speed_rot = GenerateSpeedRotation(speed_rot_type);
    current_angle = 0;
    
    isAlive = true;
  }
  
  private double GenerateSpeedMove(Utilities.Speed type)
  {
    double r;
    switch(type)
    {
      case Slow:
        r = CalculateSpeed(0.5);
        break;
        
      case MediumSlow:
        r = CalculateSpeed(1.2);
        break;
        
      case Medium:
        r = CalculateSpeed(2);
        break;
        
      case MediumFast:
        r = CalculateSpeed(2.8);
        break;
        
      case Fast:
        r = CalculateSpeed(3.2);
        break;
        
      default:
        r = CalculateSpeed(1);
        break;
    }
    return r;
  }
  
  private double CalculateSpeed(double x)
  {
    return (double)((int)(1+Math.random()*x))/1000;
  }
  
  private double GenerateSpeedRotation(Utilities.Speed type)
  {
    double r;
    switch(type)
    {
      case Slow:
        r = Math.random()*0.5;
        break;
        
      case MediumSlow:
        r = Math.random()*1.2;
        break;
        
      case Medium:
        r = Math.random()*2;
        break;
        
      case MediumFast:
        r = Math.random()*2.8;
        break;
        
      case Fast:
        r = Math.random()*3.2;
        break;
        
      default:
        r = Math.random();
        break;
    }
    return r;
  }
  
  protected void Update()
  {
    if(isAlive)
    {
      x+=direction*speed_x;
      y+=direction*speed_y;
      current_angle+=speed_rot;
      
      Destroy();
    }
  }
  
  protected void Draw()
  {
    if(isAlive)
      StdDraw.picture(x, y, image.path, current_angle);
  }
  
  protected void Destroy()
  {
    if(x<-0.2 || x > ContentManager.WindowWidth +0.2 || y <-0.2 || y > ContentManager.WindowHeight+0.2)
      isAlive = false;
  }
}