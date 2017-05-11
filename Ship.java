import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Ship
{
  public double speed, char_initial_y, char_angle, shield_angle;
  private double radiusW, radiusH;
  private final double SPEED_INI = 0.004;
  private final int SPEED_ROT = 3, NB_KILLS_SPECIAL = 5;
  public Rectangle2D char_rectangle;
  private Animation ship_move, ship_destructor_move, animation_death;
  public int char_lives, speed_rot, nb_kills_special_done, timer_shoot, timer_shoot_waited;
  private LinkedList<Bonus> bonus;
  public boolean hasShield, hasDestructor, isSlow, canShoot, justGotShot;
  private boolean lostBullet, neverDied, isAlive, animation_death_over;
  public boolean NeverDied() { return neverDied; }
  private double timer_shield, timer_destructor, timer_slow;
  private int timer_shield_flash_waited;
  public double X() { return char_rectangle.x; }
  public double Y() { return char_rectangle.y; }
  public double X_CENTER() { return char_rectangle.x; }
  public double Y_CENTER() 
  { 
    return char_rectangle.y 
      + Utilities.GetRatioH(ContentManager.ship_move.get(0).Height()-ContentManager.ship.Height())/2; 
  }
  private Clock clock_slow;
  
  /* Achievements */
  public int nbRocksDestroyed, nbShoots, nbBonus;
  public boolean gotShotOnce;
  
  public Ship() { }
  
  public void Initialize()
  {
    isAlive = true;
    canShoot = true;
    speed = SPEED_INI;
    char_initial_y = 0.07;
    char_rectangle = new Rectangle2D(0.5, char_initial_y, 
                                     Utilities.GetRatioW(ContentManager.ship_move.get(0)),
                                     Utilities.GetRatioH(ContentManager.ship_move.get(0)));
    canShoot = true;
    GameLoop.score = 0;
    ApplySkin();
    justGotShot = false;
    char_angle = 0;
    char_lives = GameLoop.NB_LIVES_CHAR;
    bonus = new LinkedList<Bonus>();
    hasShield = false;
    hasDestructor = false;
    isSlow = false;
    speed_rot = SPEED_ROT;
    shield_angle = 0;
    double radius = Math.sqrt(
                              ContentManager.ship_move.get(0).Width() * 
                              ContentManager.ship_move.get(0).Width() +
                              ContentManager.ship_move.get(0).Height() * 
                              ContentManager.ship_move.get(0).Height());
    radiusW = Utilities.GetRatioW(radius)/2 + 0.005;
    radiusH = Utilities.GetRatioH(radius)/2 + 0.005;
    
    nb_kills_special_done = 0;
    
    timer_shoot = 50;
    timer_shoot_waited = 0;
    animation_death_over = false;
    
    /* Bonus */
    timer_shield = 0;
    timer_destructor = 0;
    timer_slow = 0;
    timer_shield_flash_waited = 0;
    
    /* Achievements */
    nbRocksDestroyed = 0;
    nbShoots = 0;
    nbBonus = 0;
    gotShotOnce = false;
    lostBullet = false;
    neverDied = true;
  }
  
  public void Reset(boolean resetLives)
  {
    isAlive = true;
    if(resetLives)
    {
      canShoot = true;
      speed = SPEED_INI;
      char_rectangle = new Rectangle2D(0.5, char_initial_y, 
                                       Utilities.GetRatioW(ContentManager.ship_move.get(0)),
                                       Utilities.GetRatioH(ContentManager.ship_move.get(0)));
      canShoot = true;
      char_angle = 0;
      justGotShot = false;
      hasShield = false;
      shield_angle = 0;
      hasDestructor = false;
      isSlow = false;
      char_lives = GameLoop.NB_LIVES_CHAR;
      bonus.clear();
      speed_rot = SPEED_ROT;
      timer_shoot_waited = 0;
      animation_death_over = false;
      
      nbRocksDestroyed = 0;
      
      nb_kills_special_done = 0;
      
      /* Bonus */
      timer_shield = 0;
      timer_destructor = 0;
      timer_slow = 0;
      timer_shield_flash_waited = 0;
      
      ApplySkin(); // GoldenSkin only for a new game
      
      /* Achievements */
      neverDied = true;
    }
    else 
    {
      GiveLife();
    }
    
    /* Achievements */
    nbShoots = 0;
    nbBonus = 0;
    gotShotOnce = false;
    lostBullet = false;
  }
  
  public void ApplySkin()
  {
    Achievement.CheckAllAchievements();
    if((PassiveDancer.activateFeatures) && Achievement.isGolden)
    {
      ship_move = new Animation(ContentManager.goldenship_move, 2.5, true);
      ship_destructor_move = new Animation(ContentManager.goldenship_destructor_move, 5, true);
    }
    else
    {
      ship_move = new Animation(ContentManager.ship_move, 2.5, true);
      ship_destructor_move = new Animation(ContentManager.ship_destructor_move, 5, true);
    }
  }
  
  public void Update()
  {
    char_rectangle.UpdatePos(char_rectangle.x, char_rectangle.y);
    if(!isAlive)
    {
      if(!animation_death_over && animation_death.IsFinished())
      {
        animation_death_over = true;
        GameLoop.EndGame();
      }
    }
    else
    {
      Move();
      Rotate();
      
      /* Shoot */
      if(!canShoot)
      {
        if(timer_shoot_waited >= timer_shoot)
          canShoot = true;
        else
          timer_shoot_waited++;
      }
      else
      {
        if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
        {
          Shoot();
          canShoot = false;
          timer_shoot_waited = 0;
        }
      }
      /* Shoot */
      
      /* Bonus */
      hasShield = timer_shield>0;
      hasDestructor = timer_destructor>0;
      isSlow = timer_slow>0;
      if(hasShield)
      {
        timer_shield--;
        shield_angle++;
        if(timer_shield<90)
        {
          timer_shield_flash_waited++;
          timer_shield_flash_waited%=20;
        }
      }
      if(hasDestructor)
        timer_destructor--;
      if(isSlow)
      {
        clock_slow.Update(X_CENTER(), Y_CENTER(), (int)char_angle);
        timer_slow--;
      }
      for(Iterator<Bonus> i = bonus.iterator(); i.hasNext();)
      {
        Bonus b = i.next();
        
        if(b.IsActive())
          b.Update();
        else
        {
          StopBonus(b);
          i.remove();
        }
      }
      /* Bonus */
    }
  }
  
  public void Draw(boolean animation)
  {
    if(!isAlive)
    {
      animation_death.Draw(animation);
    }
    else
    {
      if(justGotShot)
      {
        if(hasDestructor)
          justGotShot = ship_destructor_move.Flash(char_rectangle.x, char_rectangle.y, 10, 4, char_angle);
        else
          justGotShot = ship_move.Flash(char_rectangle.x, char_rectangle.y, 10, 4, char_angle);
      }
      else
      {
        if(hasDestructor)
          ship_destructor_move.Draw(char_rectangle.x, char_rectangle.y, char_angle, animation);
        else
          ship_move.Draw(char_rectangle.x, char_rectangle.y, char_angle, animation);
      }
      if(Achievement.isGolden
           && ((hasDestructor && ship_destructor_move.IsVisible()) || (!hasDestructor && ship_move.IsVisible())))
      {
        StdDraw.picture(char_rectangle.x,
                        char_rectangle.y,
                        ContentManager.progressing_bar.get(nb_kills_special_done).path, char_angle);
      }
      if(hasShield)
      {
        if(timer_shield_flash_waited < 11)
          StdDraw.picture(X_CENTER(), Y_CENTER(), ContentManager.shield.path, shield_angle);
      }
      if(isSlow)
        clock_slow.Draw();
    }
  }
  
  public void Move()
  {
    int width = ContentManager.WindowWidth;
    int height = ContentManager.WindowHeight;
    
    if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && char_rectangle.x 
         - speed - Utilities.GetRatioW(ContentManager.ship_move.get(0))/2 > 0)
      char_rectangle.x-=speed;
    if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && char_rectangle.x 
         + speed + Utilities.GetRatioW(ContentManager.ship_move.get(0))/2 < 1)
      char_rectangle.x+=speed;
  }
  
  private void Rotate()
  {
    int limit_angle = 40;
    if(PassiveDancer.englishMode)
    {
      if(StdDraw.isKeyPressed(KeyEvent.VK_A) && char_angle < limit_angle)
        char_angle+=speed_rot;
      if(StdDraw.isKeyPressed(KeyEvent.VK_D) && char_angle > -limit_angle)
        char_angle-=speed_rot;
    }
    else
    {
      if(StdDraw.isKeyPressed(KeyEvent.VK_A) && char_angle < limit_angle)
        char_angle+=speed_rot;
      if(StdDraw.isKeyPressed(KeyEvent.VK_E) && char_angle > -limit_angle)
        char_angle-=speed_rot;
    }
  }
  
  public void Shoot()
  {
    boolean hasSpecial = 
      (PassiveDancer.activateFeatures && nb_kills_special_done >= NB_KILLS_SPECIAL);
    if(hasSpecial)
      nb_kills_special_done = 0;
    GameLoop.balls.add(new Ball(
                                char_rectangle.x + radiusW * Math.cos(Utilities.DegToRad(char_angle+90)),
                                char_rectangle.y + radiusH * Math.sin(Utilities.DegToRad(char_angle+90)), 
                                1, char_angle, hasDestructor, hasSpecial, true));
    ContentManager.sound_shoot.Play(false);
    
    nbShoots++;
  }
  
  public void Touch()
  {
    ContentManager.sound_char_touched.Play(false);
    if(char_lives == 0)
    {
      if(PassiveDancer.activateFeatures && Achievement.isGolden)
        animation_death = new Animation(ContentManager.goldenship_death, 1.5, false, 
                                        char_rectangle.x, char_rectangle.y);
      else
        animation_death = new Animation(ContentManager.ship_death, 1.5, false, 
                                        char_rectangle.x, char_rectangle.y);
      isAlive = false;
    }
    else
    {
      char_lives--;
      justGotShot = true;
      ship_move.StartFlash();
    }
    gotShotOnce = true;
    neverDied = false;
  }
  
  // Activates the bonus received
  public void GiveBonus(Bonus newBonus)
  {
    bonus.add(newBonus);
    ContentManager.bonus.Play(false);
    nbBonus++;
    switch(newBonus.bonusType)
    {
      case Destructor:
        timer_destructor+=newBonus.TimerActive();
        break;
      case Shield:
        timer_shield+=newBonus.TimerActive();
        break;
      case Slow:
      {
        if(timer_slow==0)
          clock_slow = new Clock(char_rectangle.x, char_rectangle.y, Utilities.Speed.Slow);
        BonusSlow slow = (BonusSlow)newBonus;
        speed = slow.newSpeed;
        speed_rot = 1;
        timer_slow+=newBonus.TimerActive();
      }
        break;
      case Life:
        GiveLife();
        break;
      default:
        break;
    }
  }
  
  // Disables the bonus
  public void StopBonus(Bonus newBonus)
  {
    switch(newBonus.bonusType)
    {
      case Slow:
        speed = SPEED_INI;
        speed_rot = SPEED_ROT;
        timer_slow = 0;
        break;
      case Shield:
        timer_shield_flash_waited = 0;
        break;
      default:
        break;
    }
  }
  
  public void SetLostBullet()
  {
    lostBullet = true;
  }
  
  public boolean HasLostBullet()
  {
    return lostBullet;
  }
  
  private void GiveLife()
  {
    GameLoop.score_displayers.add(new ScoreDisplayer(char_rectangle.x + char_rectangle.width/2, 
                                                     char_rectangle.y + char_rectangle.height/2, 
                                                     "+ 1 LIFE"));
    if(char_lives<GameLoop.NB_LIVES_CHAR)
      char_lives++;
  }
}