import java.awt.event.KeyEvent;
import java.util.*;

public class GameOverScreen
{
  private static double restart_timer, restart_timer_waited;
  private static boolean score_end_displayed, newBestScore;
  private static int score_displayed, score_add, newBestScore_flash;
  private static LinkedList<Rose> roses = new LinkedList<Rose>();
  
  public static void Initialize()
  {
    restart_timer = 1000;
    restart_timer_waited = 0;
    score_displayed = 0;
    score_end_displayed = false;
    newBestScore_flash = 0;
    newBestScore = false;
    ContentManager.SetFontBlack();
    if(GameLoop.score < 100)
      score_add = 10;
    else
      score_add = (int)(GameLoop.score/100);
    
    roses.clear();
    MusicManager.ChangeMusic(MusicManager.Musics.Menu);
    Achievement.CheckAllAchievements();
  }
  
  public static void Update()
  {
    if(StdDraw.isKeyPressed(KeyEvent.VK_ENTER) || 
       restart_timer_waited >= restart_timer)
    {
      if(!Achievement.IsDisplaying())
      {
        // Has done all achievements
        if(PassiveDancer.activateFeatures &&
           Achievement.nb_achievements_done != Achievement.nb_achievements_previously_done && 
           Achievement.nb_achievements_done == Achievement.NB_ACHIEVEMENTS) 
        {
          MenuAllAchievements.Initialize();
          PassiveDancer.gameState = PassiveDancer.GameState.AllAchievements;
        }
        else
        {
          GameLoop.ResetGame(false);
          PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
          MusicManager.ChangeMusic(MusicManager.Musics.InGame);
        }
      }
    }
    else
      restart_timer_waited++;
    
    // Animation of score
    if(score_displayed != GameLoop.score)
    {
      if(score_displayed < GameLoop.score)
      {
        if(score_displayed + score_add <= GameLoop.score)
          score_displayed+=score_add;
        else
          score_displayed = GameLoop.score;
      }
    }
    else
      score_end_displayed = true;
    
    if(!newBestScore && score_end_displayed && GameLoop.score > ExternalManager.ReadBestScore())
    {
      newBestScore = true;
      ExternalManager.SaveBestScore(GameLoop.score);
    }
    
    if(newBestScore)
    {
      if(Math.random()<0.03)
        roses.add(new Rose());
      for(Iterator<Rose> i = roses.iterator(); i.hasNext();)
      {
        Rose a = i.next();
        
        if(!a.IsAlive())
          i.remove();
        else
          a.Update();
      }
      newBestScore_flash++;
      newBestScore_flash%=100;
    }
  }
  
  public static void Draw()
  {
    if(newBestScore)
    {
      for(Iterator<Rose> i = roses.iterator(); i.hasNext();)
      {
        Rose a = i.next();
        a.Draw();
      }
    }
    StdDraw.picture(0.5, 0.5, ContentManager.gameover_background.path);
    ContentManager.SetFontWhite();
    
    if(PassiveDancer.activateFeatures)
    {
      StdDraw.text(0.5, 0.5, "Score : " + score_displayed);
      if(newBestScore)
      {
        if(newBestScore_flash<50)
        {
          ContentManager.SetFontRed();
          StdDraw.text(0.5, 0.45, "NEW BEST SCORE !");
          ContentManager.SetFontWhite();
        }
      }
      else
      {
        StdDraw.text(0.5, 0.45, "Best score : " + ExternalManager.ReadBestScore());
      }
      if(score_end_displayed)
        StdDraw.text(0.5,0.35, "Achievements : " + Achievement.nb_achievements_done + "/" + Achievement.NB_ACHIEVEMENTS);
      else
        StdDraw.text(0.5, 0.35, "Achievements : ");
    }
    else
    {
      StdDraw.text(0.5, 0.45, "Score : " + score_displayed);
      if(newBestScore)
      {
        if(newBestScore_flash<50)
        {
          ContentManager.SetFontRed();
          StdDraw.text(0.5, 0.4, "NEW BEST SCORE !");
          ContentManager.SetFontWhite();
        }
      }
      else
        StdDraw.text(0.5, 0.4, "Best score : " + ExternalManager.ReadBestScore());
    }
    ContentManager.SetFontWhite();
    
    /* Achievements */
    if(PassiveDancer.activateFeatures)
      Achievement.Draw();
  }
}