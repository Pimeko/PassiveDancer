public class Bonus
{
  public enum BonusType
  {
    Destructor, Shield, Slow, Life;
  }
  public BonusType bonusType;
  protected double speed, timer_active, timer_active_waited;
  public double TimerActive() { return timer_active; }
  public Rectangle2D rectangle;
  protected boolean isAlive, isActive;
  public boolean IsAlive() { return isAlive; }
  public boolean IsActive() { return isActive; }
  protected Image2D image;
  
  public Bonus(double x, double y)
  {
    timer_active_waited = 0;
    speed = -0.005;
    isAlive =true;
  }
  
  public void Update()
  {
    if(isAlive)
    {
      if(isActive)
      {
        if(timer_active_waited >= timer_active)
          Destroy();
        else
          timer_active_waited++;
      }
      else
      {
        if(rectangle.y+speed<0 || rectangle.y+speed > 1)
          Destroy();
        else
          rectangle.y+=speed;
      }
    }
  }
  
  public void Draw()
  {
    if(isAlive && !isActive)
      StdDraw.picture(rectangle.x, rectangle.y, image.path);
  }
  
  public void Activate()
  {
    isActive = true;
  }
  
  public void Destroy()
  {
    isActive = false;
    isAlive = false;
  }
  
  // Returns a new Bonus according to their chance of apparition
  public static Bonus GenerateBonus(double x, double y)
  {
    double rnd = Math.random(), nbBonus = 4;
    Bonus bonus = null;
    
    if(rnd<0.1)
      bonus = new BonusDestructor(x,y);
    else if(rnd<0.4)
      bonus = new BonusLife(x,y);
    else if(rnd<0.7)
      bonus = new BonusShield(x,y);
    else if(rnd<=1)
      bonus = new BonusSlow(x,y);
    else
      bonus = new BonusDestructor(x,y);
    
    return bonus;
  }
}