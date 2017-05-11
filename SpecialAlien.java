/* This class is for the special alien passing by on top of the screen */

public class SpecialAlien
{
  public static boolean oneAlive;
  
  double speed;
  Rectangle2D rectangle;
  public Rectangle2D Rectangle() { return rectangle; }
  boolean isAlive, isOut;
  int score;
  private Animation animation_move, animation_death;
  
  public SpecialAlien()
  {
    speed = 0.003;
    rectangle = new Rectangle2D(0,1-Utilities.GetRatioH(ContentManager.special_alien_move.get(0))/2-0.03, 
                                Utilities.GetRatioW(ContentManager.special_alien_move.get(0)),
                                Utilities.GetRatioH(ContentManager.special_alien_move.get(0)));
    isAlive = true;
    isOut = false;
    score = 10*GameLoop.NB_ALIENS_C + 40;
    animation_move = new Animation(ContentManager.special_alien_move, 5, true);
  }
  
  public void Update()
  {
    if(isAlive)
    {
      if(!isOut)
      {
        rectangle.x += speed;
        isOut = rectangle.x - rectangle.width/2 > 1;
      }
      else
        Kill(false);
    }
    else if((!isOut && animation_death.IsFinished()) || (isOut))
      oneAlive = false;
  }
  
  public void Draw(boolean animation)
  {
    if(isAlive)
      animation_move.Draw(rectangle.x, rectangle.y, animation);
    else
      animation_death.Draw(animation);
  }
  
  // The boolean "killed" precises if it is out of the screen or if the ship killed it
  public void Kill(boolean killed)
  {
    if(killed)
    {
      ContentManager.sound_alien_death.Play(false);
      GameLoop.score+=score;
      GameLoop.score_displayers.add(new ScoreDisplayer(rectangle.x, rectangle.y, "+ " + String.valueOf(score)));
      Achievement.SetAchieved(Achievement.AchievementType.UFO);
      animation_death = new Animation(ContentManager.special_alien_death, 1.5, false, rectangle.x, rectangle.y);
    }
    else
      oneAlive = false;
    isAlive = false;
    ContentManager.sound_special_alien.Stop();
  }
  
  // Only creates a special alien if a random is under a certain value
  public static void Generate()
  {
    if(PassiveDancer.activateFeatures && Math.random()<0.0005)
    {
      GameLoop.special_alien = new SpecialAlien();
      ContentManager.sound_special_alien.Play(false);
      oneAlive = true;
    }
  }
}