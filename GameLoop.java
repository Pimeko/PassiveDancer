import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class GameLoop
{
  public static final int NB_LIVES_CHAR= 3, NB_ALIENS_L = 6, NB_ALIENS_C = 4, // L = Line, C = Column
    NB_BONUS_MAX = 4, NB_BUNKERS = 4, NB_LEVEL_MAX = Alien.GetMaxLevel(), NB_STARS = 20;
  public static final double SPACE_ALIENS_L = 0.04, SPACE_ALIENS_C = 0.02;
  
  
  public static int nb_aliens_alive, score, nb_bonus, actual_level;
  private static double timer_alien_move_waited;
  private static int timer_pop_clouds, timer_pop_clouds_waited;
  private static Rectangle2D fog_rectangle;
  public static Rectangle2D alien_rectangle;
  public static Ship ship;
  public static Alien[][] aliens;
  public static SpecialAlien special_alien;
  private static boolean hasPressedEscape;
  
  private static Star[] stars;
  public static LinkedList<Ball> balls;
  public static LinkedList<Bonus> bonus;
  public static LinkedList<ScoreDisplayer> score_displayers;
  public static LinkedList<Cloud> clouds;
  public static ArrayList<Rock> rocks;
  public static ArrayList<Bunker> bunkers;
  
  public static void Initialize()
  {
    actual_level = 1;
    hasPressedEscape = false;
    
    /* Background */
    stars = new Star[NB_STARS];
    for(int i = 0; i<NB_STARS; i++)
    {
      double i_rnd = Math.random();
      double j_rnd = Math.random();
      stars[i] = new Star(i_rnd, j_rnd);
    }
    /* Background */
    
    /* Char */
    ship = new Ship();
    ship.Initialize();
    score = 0;
    /* Char */
    
    /* Aliens */
    aliens = new Alien[NB_ALIENS_L][NB_ALIENS_C]; // [Column][Line]
    Alien.left = 0;
    Alien.right = NB_ALIENS_L-1;
    Alien.top = 0;
    Alien.bot = NB_ALIENS_C-1;
    alien_rectangle = new Rectangle2D(0.5, 1, 
                                      NB_ALIENS_L*(Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0))
                                                     + SPACE_ALIENS_L) - SPACE_ALIENS_L,
                                      NB_ALIENS_C*(Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0))
                                                     +SPACE_ALIENS_C) - SPACE_ALIENS_C);    
    for(int i = 0; i<NB_ALIENS_L;i++)
    {
      for(int j = 0; j<NB_ALIENS_C; j++)
      {
        aliens[i][j] = new Alien(i, j);
        aliens[i][j].Reset(false);
      }
    }
    aliens[0][0].UpdatePos(alien_rectangle.x - alien_rectangle.width/2, 
                           alien_rectangle.y - alien_rectangle.height/2);
    
    // UpdatePos of AliensRectangle according to the TopLeft Alien's position
    alien_rectangle.UpdatePos(aliens[Alien.left][Alien.top].X() + alien_rectangle.width/2 
                                - Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0))/2, 
                              aliens[Alien.left][Alien.top].Y() - alien_rectangle.height/2 
                                + Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0))/2);
    timer_alien_move_waited = 0;
    nb_aliens_alive = NB_ALIENS_C*NB_ALIENS_L;
    SpecialAlien.oneAlive = false;
    /* Aliens */
    
    /* Bullets */
    balls = new LinkedList<Ball>();
    /* Bullets */
    
    /* Bunkers */
    bunkers = new ArrayList<Bunker>();
    if(PassiveDancer.activateFeatures)
    {
      for(int i = 0; i < NB_BUNKERS; i++)
        bunkers.add(new Bunker(0.08 + i*0.25, 0.2, Bunker.BunkerType.Stairs));
    }
    /* Bunkers */
    
    /* Bonus */
    bonus = new LinkedList<Bonus>();
    nb_bonus = 0;
    while(nb_bonus < NB_BONUS_MAX)
    {
      int i_rnd = (int)(Math.random()*(NB_ALIENS_L));
      int j_rnd = (int)(Math.random()*(NB_ALIENS_C));
      Alien a = aliens[i_rnd][j_rnd];
      
      // Distribute bonus to random aliens
      if(!a.CarriesBonus())
      {
        a.AttachBonus();
        nb_bonus++;
      }
    }
    /* Bonus */
    
    /* Clouds */
    clouds = new LinkedList<Cloud>();
    timer_pop_clouds = 100;
    timer_pop_clouds_waited = 0;
    /* Clouds */
    
    /* HUD */
    score_displayers = new LinkedList<ScoreDisplayer>();
    ContentManager.SetFontWhite();
    /* HUD */
    
    /* Achievements */    
    Achievement.nb_achievements_previously_done = Achievement.nb_achievements_done;
    /* Achievements */
  }
  
  public static void ResetGame(boolean nextLevel)
  {
    if(!nextLevel) // Reset game
    {
      MusicManager.ChangeMusic(MusicManager.Musics.InGame);
      score = 0;
      actual_level=1;
      
      score_displayers.clear();
    
      /* Bullets */
      balls.clear();
      /* Bullets */
      
      /* Bunkers */
      bunkers.clear();
      if(PassiveDancer.activateFeatures)
      {
        for(int i = 0; i < NB_BUNKERS; i++)
          bunkers.add(new Bunker(0.08 + i*0.25, 0.2, Bunker.BunkerType.Stairs));
      }
      /* Bunkers */
      
      /* Aliens */
      if(SpecialAlien.oneAlive)
        special_alien.Kill(false);
      /* Aliens */
      
      /* Bonus */
      bonus.clear();
      /* Bonus */
      
      /* Clouds */
      clouds.clear();
      timer_pop_clouds_waited = 0;
      /* Clouds */
      
      /* Achievements */    
      Achievement.nb_achievements_previously_done = Achievement.nb_achievements_done;
      /* Achievements */
    }
    else
    {
      if(actual_level <= NB_LEVEL_MAX)
      {
        actual_level++;
        GameLoop.score_displayers.add(new ScoreDisplayer(0.95, 0.9, "+ 1"));
      }
      if(actual_level == NB_LEVEL_MAX + 1)
      {
        Achievement.SetAchieved(Achievement.AchievementType.HighestGround);
        if(ship.NeverDied())
          Achievement.SetAchieved(Achievement.AchievementType.PGM);
      }
      
      /* Achievements */
      Achievement.nb_achievements_previously_done = Achievement.nb_achievements_done;
      if(PassiveDancer.activateFeatures)
      {
        if(ship.nbBonus == 0)
          Achievement.SetAchieved(Achievement.AchievementType.WhoNeedsBonus);
        else if(ship.nbBonus == NB_BONUS_MAX)
          Achievement.SetAchieved(Achievement.AchievementType.BonusNeeded);
        if(!ship.gotShotOnce)
          Achievement.SetAchieved(Achievement.AchievementType.Perfect);
        if(!ship.HasLostBullet())
          Achievement.SetAchieved(Achievement.AchievementType.Sniper);
      }
      /* Achievements */
      
    }
    
    /* Background */
    for(int i = 0; i<NB_STARS; i++)
    {
      double i_rnd = Math.random();
      double j_rnd = Math.random();
      stars[i] = new Star(i_rnd, j_rnd, actual_level);
    }
    /* Background */
    
    /* Char */
    ship.Reset(!nextLevel);
    /* Char */
    
    /* Aliens */
    alien_rectangle = new Rectangle2D(0.5, 1 - actual_level*(0.012), 
                                      NB_ALIENS_L*(Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0))
                                                     + SPACE_ALIENS_L) - SPACE_ALIENS_L,
                                      NB_ALIENS_C*(Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0))
                                                     +SPACE_ALIENS_C) - SPACE_ALIENS_C);
    Alien.left = 0;
    Alien.right = NB_ALIENS_L-1;
    Alien.top = 0;
    Alien.bot = NB_ALIENS_C-1;
    
    for(int i = 0; i<NB_ALIENS_L;i++)
      for(int j = 0; j<NB_ALIENS_C; j++)
        aliens[i][j].Reset(nextLevel);
    
    aliens[0][0].UpdatePos(alien_rectangle.x - alien_rectangle.width/2, 
                           alien_rectangle.y - alien_rectangle.height/2);
    
    // UpdatePos of AliensRectangle according to the TopLeft Alien's position
    alien_rectangle.UpdatePos(aliens[Alien.left][Alien.top].X() + alien_rectangle.width/2 
                                - Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0))/2, 
                              aliens[Alien.left][Alien.top].Y() - alien_rectangle.height/2 
                                + Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0))/2);
    timer_alien_move_waited = 0;
    nb_aliens_alive = NB_ALIENS_C*NB_ALIENS_L;
    Alien.direction = 1;
    /* Aliens */
    
    hasPressedEscape = false;
    
    /* Bonus */
    nb_bonus = 0;
    
    // Distribute bonus to random aliens
    while(nb_bonus != NB_BONUS_MAX)
    {
      int i_rnd = (int)(Math.random()*(NB_ALIENS_L));
      int j_rnd = (int)(Math.random()*(NB_ALIENS_C));
      Alien a = aliens[i_rnd][j_rnd];
      
      if(!a.CarriesBonus())
      {
        a.AttachBonus();
        nb_bonus++;
      }
    }
    /* Bonus */
  }
  
  public static void EndGame()
  {    
    GameOverScreen.Initialize();
    PassiveDancer.gameState = PassiveDancer.GameState.GameOver;
  }
  
  public static void Update()
  {    
    if(nb_aliens_alive == 0 && AllAliensAnimationsFinished())
      GameLoop.ResetGame(true);
    
    /* Stars */
    for(int i = 0; i < NB_STARS; i++)
      stars[i].Update();
    /* Stars */
    
    /* Char */
    ship.Update();
    /* Char */
    
    /* Aliens */
    for(int i = 0; i < NB_ALIENS_L;i++)
    {
      for(int j = 0; j < NB_ALIENS_C; j++)
      {
        aliens[i][j].Update();
      }
    }
    
    if(timer_alien_move_waited < aliens[0][0].timer_alien_move)
      timer_alien_move_waited++;
    else
    {
      MoveAliens();
      timer_alien_move_waited = 0;
    }
    // UpdatePos of AliensRectangle according to the TopLeft Alien's position
    alien_rectangle.UpdatePos(aliens[Alien.left][Alien.top].X() + alien_rectangle.width/2 
                                - Utilities.GetRatioW(ContentManager.alien_move.get(0).get(0))/2, 
                              aliens[Alien.left][Alien.top].Y() - alien_rectangle.height/2 
                                + Utilities.GetRatioH(ContentManager.alien_move.get(0).get(0))/2);
    if(SpecialAlien.oneAlive)
      special_alien.Update();
    else
      SpecialAlien.Generate();
    
    // Score Displayer
    for(Iterator<ScoreDisplayer> i = score_displayers.iterator(); i.hasNext();)
    {
      ScoreDisplayer s = i.next();
      
      if(!s.IsAlive())
        i.remove();
      else
        s.Update();
    }
    /* Aliens */
    
    /* Bullets */
    for(Iterator<Ball> i = balls.iterator(); i.hasNext();)
    {
      Ball b = i.next();
      
      if(!b.IsAlive())
      {
        // Wait the end of the destroying animation
        if(!b.IsBeingDestroyed())
          i.remove();
      }
      else
        b.Update();
    }
    /* Bullets */
    
    /* Bonus */
    if(PassiveDancer.activateFeatures)
    {
      for(Iterator<Bonus> i = bonus.iterator(); i.hasNext();)
      {
        Bonus b = i.next();
        
        if(!b.IsAlive())
          i.remove();
        else
          b.Update();
      }
    }
    /* Bonus */
    
    CollisionManager.Collisions(ship, balls, aliens, bunkers, bonus);
    
    /* Clouds */
    if(timer_pop_clouds_waited >= timer_pop_clouds)
    {
      if(Math.random()<0.5)
        clouds.add(new Cloud());
      timer_pop_clouds_waited = 0;
    }
    else
      timer_pop_clouds_waited++;
    
    for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();)
    {
      Cloud a = i.next();
      
      if(!a.IsAlive())
        i.remove();
      else
        a.Update();
    }
    /* Clouds */
    
    /* Pause */
    if(StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE))
      hasPressedEscape = true;
    if(!StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE) && hasPressedEscape)
    {
      PauseMenu.Reset();
      PassiveDancer.gameState = PassiveDancer.GameState.Pause;
      
      hasPressedEscape = false;
    }
    /* Pause */
    
    /* Achievements */
    if(PassiveDancer.activateFeatures && ship.nbRocksDestroyed == GameLoop.NB_BUNKERS)
      Achievement.SetAchieved(Achievement.AchievementType.SelfSabotage);
    /* Achievements */
    
    /* Demo All Achievements */
    if(StdDraw.isKeyPressed(KeyEvent.VK_K))
    {
      ExternalManager.SetAllAchievementsDone();
      EndGame();
    }
    /* Demo All Achievements */
  }
  
  public static void KillAll()
  {
    for(int i = 0; i < NB_ALIENS_L;i++)
      for(int j = 0; j < NB_ALIENS_C; j++)
      aliens[i][j].Kill();
  }
  
  public static void Draw(boolean animation)
  {
    ContentManager.SetFontWhite();
    
    /* Stars */
    for(int i = 0; i < NB_STARS; i++)
      stars[i].Draw();
    /* Stars */
    
    /* Char */
    ship.Draw(animation);
    /* Char */
    
    /* Bunkers */
    for(int i = 0; i < bunkers.size(); i++)
      bunkers.get(i).Draw();
    /* Bunkers */
    
    /* Aliens */
    for(int i = 0; i < NB_ALIENS_L;i++)
    {
      for(int j = 0; j < NB_ALIENS_C; j++)
      {
        aliens[i][j].Draw(animation);
      }
    }
    if(SpecialAlien.oneAlive)
      special_alien.Draw(animation);
    /* Aliens */
    
    /* Bullets */
    for(Iterator<Ball> i = balls.iterator(); i.hasNext();)
      i.next().Draw(animation);
    /* Bullets */
    
    /* Bonus */
    if(PassiveDancer.activateFeatures)
    {
      for(Iterator<Bonus> i = bonus.iterator(); i.hasNext();)
      {
        Bonus b = i.next();
        
        if(b.IsAlive())
          b.Draw();
      }
    }
    /* Bonus */
    
    /* Clouds */
    for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();)
      i.next().Draw();
    /* Clouds */
    
    /* HUD */
    // Score Displayer
    for(Iterator<ScoreDisplayer> i = score_displayers.iterator(); i.hasNext();)
    {
      ScoreDisplayer s = i.next();
      
      if(s.IsAlive())
        s.Draw();
    }
    
    StdDraw.text(0.1, 0.9, "Score : " + score);
    
    if(!Achievement.keepsOnDisplaying)
      StdDraw.text(0.5, 0.9, "Level " + actual_level);
    
    StdDraw.text(0.9, 0.9, "Lives : " + ship.char_lives);
    /* HUD */
    
    /* Achievements */
    if(PassiveDancer.activateFeatures)
      Achievement.Draw();
  }
  
  private static void MoveAliens()
  {
    Alien first_alien = aliens[0][0];
    
    // Test Hypothetic position after move
    double next_pos = alien_rectangle.x + Alien.direction*alien_rectangle.width/2 + Alien.direction*first_alien.speed;
    if((Alien.direction == 1 & next_pos > 1) || (Alien.direction == -1 & next_pos < 0))
      MoveAliensDown();
    else
      first_alien.UpdatePos(first_alien.rectangle.x + first_alien.speed*Alien.direction, first_alien.rectangle.y);
    
    if(alien_rectangle.Intersects(ship.char_rectangle))
      EndGame();
  }
  
  private static void MoveAliensDown()
  {
    Alien first_alien = aliens[0][0];
    
    Alien.ChangeDirection();
    if(first_alien.rectangle.y - first_alien.gap_down < 0)
      EndGame();
    first_alien.UpdatePos(first_alien.rectangle.x, first_alien.rectangle.y - first_alien.gap_down);
  }
  
  private static boolean AllAliensAnimationsFinished()
  {
    boolean r = true;
    for(int i = 0; i < NB_ALIENS_L && r;i++)
      for(int j = 0; j < NB_ALIENS_C && r; j++)
        r = aliens[i][j].IsAnimationDeathOver();
      
    return r;
  }
}