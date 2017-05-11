public class Ball
{
  private double speed, x, y, angle;
  private boolean isAlive, isDestructor, fromShip, isBeingDestroyed, isSpecial;
  public boolean IsAlive() { return isAlive; }
  public boolean IsDestructor() { return isDestructor; }
  public boolean FromShip() { return fromShip; }
  public boolean IsBeingDestroyed() { return isBeingDestroyed; }
  public boolean IsSpecial() { return isSpecial; }
  private Rectangle2D rectangle;
  public Rectangle2D Rectangle()
  {
    return rectangle;
  }
  public int direction;
  private Animation animation_death;
  private Image2D bullet_texture;
  
  public Ball(double x, double y, int direction,
              boolean isDestructor, boolean isSpecial, boolean fromShip)
  {
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.isDestructor = isDestructor;
    this.isSpecial = isSpecial;
    this.fromShip = fromShip;
    speed=Sign(direction)*10;
    isAlive = true;
    isBeingDestroyed = false;
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(ContentManager.bullet),
                                Utilities.GetRatioH(ContentManager.bullet));
    angle = 0;
    if(fromShip)
    {
      if(isSpecial)
        bullet_texture = ContentManager.bullet;
      else
        bullet_texture = ContentManager.bullet_special;
    }
    else
      bullet_texture = ContentManager.bullet_alien;
  }
  
  public Ball(double x, double y, int direction, double angle,
              boolean isDestructor, boolean isSpecial, boolean fromShip)
  {
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.isDestructor = isDestructor;
    this.fromShip = fromShip;
    this.isSpecial = isSpecial;
    speed=Sign(direction)*10;
    isAlive = true;
    isBeingDestroyed = false;
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(ContentManager.bullet),
                                Utilities.GetRatioH(ContentManager.bullet));
    this.angle = angle;
    if(fromShip)
    {
      if(isSpecial)
        bullet_texture = ContentManager.bullet_special;
      else
        bullet_texture = ContentManager.bullet;
    }
    else
      bullet_texture = ContentManager.bullet_alien;
  }
  
  private int Sign(int x)
  {
    return x>0?1:-1;
  }
  
  public void Update()
  {
    if(isAlive)
    {
      if(y<0 || y > 1 || x < 0 || x > 1)
        Destroy(false);
      else
      {
        double radAngle = Utilities.DegToRad(angle+90);
        x+=speed*Utilities.GetRatioW(Math.cos(radAngle));
        y+=speed*Utilities.GetRatioH(Math.sin(radAngle));
      }
      
      rectangle.UpdatePos(x, y);
    }
  }
  
  public void Draw(boolean animation)
  {
    if(isAlive)
    {
      if(direction>0)
        StdDraw.picture(rectangle.x, rectangle.y, bullet_texture.path, angle);
      else
        StdDraw.picture(x, y, bullet_texture.path, 180);
      
    }
    if(isBeingDestroyed) // Animation destruction
    {
      if(!animation_death.IsFinished())
        animation_death.Draw(animation);
      else
        isBeingDestroyed = false;
    }
  }
  
  public void Destroy(boolean killed)
  {
    isAlive = false;
    if(killed)
    {
      isBeingDestroyed = true;
      animation_death = new Animation(ContentManager.bullet_death, 0.5, false, x, y);
    }
    else if(fromShip)
      GameLoop.ship.SetLostBullet();
  }
}