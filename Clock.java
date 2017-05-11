public class Clock
{
  double x, y;
  int angle, angle_small, angle_big, plus_small, plus_big;
  Utilities.Speed speed;
  public Clock(double x, double y, Utilities.Speed speed)
  {
    this.x = x;
    this.y = y;
    this.speed = speed;
    Reset();
  }
  
  public void Reset()
  {
    angle_small=0;
    angle_big=0;
    SpeedToPlus();
  }
  
  // Sets the speed of the needles
  private void SpeedToPlus()
  {
    switch(speed)
    {
      case Slow:
        plus_big = 1;
        break;
        
      case MediumSlow:
        plus_big = 2;
        break;
        
      case Medium:
        plus_big = 3;
        break;
        
      case MediumFast:
        plus_big = 4;
        break;
        
      case Fast:
        plus_big = 6;
        break;
    }
    plus_small = 2*plus_big;
  }
  
  public void Update(double x, double y, int angle)
  {
    this.x = x;
    this.y = y;
    angle_small+=plus_small;
    angle_big+=plus_big;
  }
  
  public void Draw()
  {
    StdDraw.picture(x, y, ContentManager.clock_background.path);
    StdDraw.picture(x, y, ContentManager.clock_small_aiguille.path, angle_small);
    StdDraw.picture(x, y, ContentManager.clock_big_aiguille.path, angle_big);
  }
}