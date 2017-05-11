/* This is part of the bunkers */
public class Rock
{
  public static final int NB_STATES = ContentManager.rock.size();
  double x, y, angle;
  int state;
  public Rectangle2D rectangle;
  public boolean isAlive;
  
  public Rock(double x, double y)
  {
    this.x = x;
    this.y = y;
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(ContentManager.rock.get(0)), 
                                Utilities.GetRatioH(ContentManager.rock.get(0)));
    angle = GenerateAngle();
  }
  
  public Rock()
  {
    isAlive = false;
  }
  
  private double GenerateAngle()
  {
    int r = (int)(4*Math.random());
    return r*90;
  }
  
  public void Initialize()
  {
    state = 0;
    isAlive = true;
  }
  
  public void Draw()
  {
    if(isAlive)
      StdDraw.picture(x, y, ContentManager.rock.get(state).path, angle);
  }
  
  public void Destroy()
  {
    state++;
    isAlive= state < NB_STATES;
  }
}