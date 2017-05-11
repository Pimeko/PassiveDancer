public class Alien
{
  public double speed, timer_alien_move, gap_down, random_to_get;
  public static int direction = 1;
  public static void ChangeDirection(){ direction*=-1; }
  public static int left, right, top, bot;
  private static final int TIMER_ALIEN_MOVE_MIN = 25;
  private static final double RANDOM_TO_GET_MAX = 0.004;
  
  private int i, j; // Array position, ith column, jth line
  private double x, y;
  public double X(){ return x; }
  public double Y(){ return y; }
  private boolean isAlive, canShoot, carriesBonus, animation_death_over;
  public boolean IsAlive() { return isAlive; }
  public boolean CarriesBonus() { return carriesBonus; }
  public boolean IsAnimationDeathOver() { return animation_death_over; }
  public void SetShooter() { canShoot = true; }
  public void SetPassive() { canShoot = false; }
  public Rectangle2D rectangle;
  private Animation animation_move, animation_death;
  
  public Alien(int i, int j)
  {
    this.i = i;
    this.j = j;
    isAlive = true;
    rectangle = new Rectangle2D(0,0,
                                Utilities.GetRatioW(ContentManager.alien_move.get(j).get(0)), 
                                Utilities.GetRatioH(ContentManager.alien_move.get(j).get(0)));
    canShoot = (j==bot);
    animation_move = new Animation(ContentManager.alien_move.get(j), 3, true);
    carriesBonus = false;
    animation_death_over = false;
  }
  
  public void Reset(boolean next_level)
  {
    speed = 0.065;
    gap_down = 0.05;
    if(next_level)
      NextLevel();
    else
    {
      timer_alien_move = 95;
      random_to_get = 0.001;
    }
    isAlive = true;
    rectangle.UpdatePos(0,0);
    animation_move = new Animation(ContentManager.alien_move.get(j), 3, true);
    canShoot = (j==bot);
    carriesBonus = false;
    animation_death_over = false;
  }
  
  public void NextLevel()
  {
    if(timer_alien_move > TIMER_ALIEN_MOVE_MIN)
      timer_alien_move-=10;
    else if(random_to_get<RANDOM_TO_GET_MAX)
      random_to_get+=0.001;
  }
  
  // Returns the nb of levels
  public static int GetMaxLevel()
  {
    int t = 95, level = 1;
    double r = 0.001;
    while(t > TIMER_ALIEN_MOVE_MIN)
    {
      t-=10;
      level++;
    }
    while(r < RANDOM_TO_GET_MAX)
    {
      r+=0.001;
      level++;
    }
    return level;
  }
  
  public void AttachBonus()
  {
    carriesBonus = true;
  }
  
  public void Update()
  {
    UpdatePos();
    
    if(isAlive)
    {
      if(canShoot && Math.random() < random_to_get)
        Shoot();
    }
    else
    {
      if(!animation_death_over && animation_death.IsFinished())
        animation_death_over = true;
    }
  }
  
  public void UpdatePos(double x, double y)
  {
    this.x = x;
    this.y = y;
    rectangle.UpdatePos(x, y);
  }
  
  // Calibrate the pos according to the pos of the top left alien
  public void UpdatePos()
  {
    x = GameLoop.aliens[0][0].x + 
      i*(Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0)) + GameLoop.SPACE_ALIENS_L);
    y = GameLoop.aliens[0][0].y - 
      j*(Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0)) + GameLoop.SPACE_ALIENS_C);
    rectangle.UpdatePos(x, y);
  }
  
  public void Draw(boolean animation)
  {
    if(isAlive)
      animation_move.Draw(x, y, animation);
    else
      animation_death.Draw(animation);
  }
  
  private int GetScore()
  {
    return (GameLoop.NB_ALIENS_C-j)*10;
  }
  
  public void Kill()
  {
    if(isAlive)
    {
      isAlive = false;
      GameLoop.nb_aliens_alive--;
      int score = GetScore();
      GameLoop.score+=score;
      GameLoop.score_displayers.add(new ScoreDisplayer(x, y, "+ " + String.valueOf(score)));
      animation_death = new Animation(ContentManager.alien_death, 1.5, false, x, y);
      ContentManager.sound_alien_death.Play(false);
      
      /* Replaces boundaries of the alien rectangle */
      int n, actual_i, actual_j, prev_left, prev_right, prev_top, prev_bot;
      boolean keepOnSearching;
      // Left
      keepOnSearching = true;
      prev_left = left;
      actual_i = left;
      while(keepOnSearching)
      {
        n = 0;
        for(int a = top; n==0 && a < bot+1; a++)
        {
          if(GameLoop.aliens[actual_i][a].isAlive)
            n++;
        }
        keepOnSearching = (n==0) && (actual_i < right);
        if(keepOnSearching)
          actual_i++;
      }
      left = actual_i;
      GameLoop.alien_rectangle.width-=
        (left-prev_left)*(Utilities.GetRatioW(ContentManager.alien_move.get(j).get(0))+GameLoop.SPACE_ALIENS_L);
      
      // Right
      keepOnSearching = true;
      prev_right = right;
      actual_i = right;
      while(keepOnSearching)
      {
        n = 0;
        for(int a = top; n==0 && a < bot+1; a++)
        {
          if(GameLoop.aliens[actual_i][a].isAlive)
            n++;
        }
        keepOnSearching = (n==0) && (actual_i > left);
        if(keepOnSearching)
          actual_i--;
      }
      right = actual_i;
      GameLoop.alien_rectangle.width-=
        (prev_right-right)*(Utilities.GetRatioW(ContentManager.alien_move.get(j).get(0))+GameLoop.SPACE_ALIENS_L);
      
      
      // Top
      keepOnSearching = true;
      prev_top = top;
      actual_j = top;
      while(keepOnSearching)
      {
        n = 0;
        for(int a = left; n==0 && a < right+1; a++)
        {
          if(GameLoop.aliens[a][actual_j].isAlive)
            n++;
        }
        keepOnSearching = (n==0) && (actual_j < bot);
        if(keepOnSearching)
          actual_j++;
      }
      top = actual_j;
      GameLoop.alien_rectangle.height-=
        (top-prev_top)*(Utilities.GetRatioH(ContentManager.alien_move.get(j).get(0))+GameLoop.SPACE_ALIENS_C);
      
      
      // Bot
      keepOnSearching = true;
      prev_bot = bot;
      actual_j = bot;
      while(keepOnSearching)
      {
        n = 0;
        for(int a = left; n==0 && a < right+1; a++)
        {
          if(GameLoop.aliens[a][actual_j].isAlive)
            n++;
        }
        keepOnSearching = (n==0) && (actual_j > top);
        if(keepOnSearching)
          actual_j--;
      }
      bot = actual_j;
      GameLoop.alien_rectangle.height-=
        (prev_bot-bot)*(Utilities.GetRatioH(ContentManager.alien_move.get(j).get(0))+GameLoop.SPACE_ALIENS_C);
      
      
      // Refresh Shooter
      int newShooter = 0;
      for(int a = 0; a<GameLoop.NB_ALIENS_C; a++)
      {
        if(GameLoop.aliens[i][a].IsAlive())
        {
          newShooter = a;
          GameLoop.aliens[i][a].SetPassive();
        }
      }
      GameLoop.aliens[i][newShooter].SetShooter();
      
      // Bonus
      if(PassiveDancer.activateFeatures && carriesBonus)
        GameLoop.bonus.add(Bonus.GenerateBonus(x, y));
    }
  }
  
  public void Shoot()
  {
    GameLoop.balls.add(new Ball(x, y - Utilities.GetRatioH(ContentManager.alien_move.get(j).get(0))/2
                                  - Utilities.GetRatioH(ContentManager.bullet)/2 - 0.005, 
                                -1, 0.01, false, false, false));
    ContentManager.sound_shoot.Play(false);
  }
}