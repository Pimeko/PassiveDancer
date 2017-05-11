/* This is used to display a score and its animation (Ex : +10 when killing and ennemy) */

public class ScoreDisplayer
{
  double x, y;
  String text;
  
  double timer_display, timer_display_waited;
  boolean isAlive;
  public boolean IsAlive() { return isAlive; }
  
  public ScoreDisplayer(double x, double y, String text)
  {
    this.x = x;
    this.y = y;
    this.text = text;
    
    timer_display = 50;
    timer_display_waited = 0;
    isAlive = true;
  }
  
  public void Update()
  {
    if(timer_display_waited >= timer_display)
    {
      isAlive = false;
    }
    else
    {
      x+=0.0003;
      y-=0.0006;
      timer_display_waited++;
    }
  }
  
  public void Draw()
  {
    if(isAlive)
      StdDraw.text(x, y, text);
  }
}