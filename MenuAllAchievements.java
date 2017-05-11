import java.awt.event.KeyEvent;
import java.util.*;

public class MenuAllAchievements
{
  private static int timer_text, timer_text_waited, current_text_index, 
    nb_texts, speed_rot, angle, press_flash, star_angle, timer_stanley, timer_stanley_waited;
  private static LinkedList<String> texts;
  private static Animation ship_evolution;
  private static boolean evolving, has_evolved, has_pressed_next, stanley_enabled;
  private static LinkedList<Rose> roses = new LinkedList<Rose>();
  
  public static void Initialize()
  {
    timer_text = 200;
    timer_text_waited = 0;
    current_text_index = 0;
    has_pressed_next = false;
    
    texts = new LinkedList<String>();
    texts.add("YOU HAVE SUCCEDED ALL THE ACHIEVEMENTS !");
    texts.add("IT IS NOW TIME FOR YOUR REWARD...");
    nb_texts = texts.size();
    
    speed_rot = 1;
    angle = 0;
    evolving = false;
    
    has_evolved = false;
    press_flash = 0;
    roses.clear();
    star_angle = 0;
    
    timer_stanley = 1000;
    timer_stanley_waited = 0;
    stanley_enabled = false;
    
    MusicManager.StopMusic();
    ContentManager.sound_crash.Play(false);
  }
  
  public static void Update()
  {
    if(current_text_index == nb_texts)
    {
      if(speed_rot > 150)
      {
        if(evolving)
        {
          if(has_evolved)// End of the whole animation
          {
            if(timer_stanley_waited>=timer_stanley) // Stanley - Easter Egg
            {
              stanley_enabled = true;
              if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
              {
                GameLoop.ResetGame(false);
                PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
                MusicManager.ChangeMusic(MusicManager.Musics.InGame);
              }
              else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
              {
                ExternalManager.ResetAchievements();
                System.exit(0);
              }
            }
            else 
            {
              if(timer_stanley_waited+1>=timer_stanley)
              {
                MusicManager.StopMusic();
                ContentManager.sound_stanley.Play(false);
              }
              timer_stanley_waited++;
              if(StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) 
              {
                GameLoop.ResetGame(false);
                PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
                MusicManager.ChangeMusic(MusicManager.Musics.InGame);
              }
              else
              {
                press_flash++;
                press_flash%=100;
                
                /* Roses */
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
                /* Roses */
                
                star_angle++;
              }
            }
          }
          else if(ship_evolution.IsFinished())
          {
            has_evolved = true;
            ContentManager.sound_ship_evolving.Stop();
            MusicManager.ChangeMusic(MusicManager.Musics.AllAchievements);
          }
        }
        else
        {
          evolving = true;
          ship_evolution = new Animation(ContentManager.ship_evolution, 0.5, false, 0.5, 0.5);
        }
      }
      else
      {
        angle+=speed_rot;
        speed_rot++;
      }
    }
    else
    {
      
      if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) 
        has_pressed_next = true;
      if(timer_text_waited>=timer_text || (has_pressed_next && !StdDraw.isKeyPressed(KeyEvent.VK_SPACE)))
      {
        current_text_index++;
        timer_text_waited = 0;
        has_pressed_next = false;
        if(current_text_index == nb_texts)
          ContentManager.sound_ship_evolving.Play(false);
        else
        {
          ContentManager.sound_crash.Stop();
          ContentManager.sound_crash.Play(false);
        }
      }
      else
      {
        timer_text_waited++;
      }
    }
  }
  
  public static void Draw()
  {
    StdDraw.picture(0.5, 0.5, ContentManager.background.path);
    if(current_text_index == nb_texts)
    {
      if(evolving)
      {
        if(has_evolved)
        {
          if(stanley_enabled)
          {
            StdDraw.picture(0.2,0.3,ContentManager.door_stanley.path);
            StdDraw.picture(0.8,0.3,ContentManager.door_stanley.path);
          }
          else
          {
            StdDraw.picture(0.5, 0.5, ContentManager.background_all_achievements.path);
            StdDraw.picture(0.5, 0.55, ContentManager.star_all_achievements.path,star_angle);
            ContentManager.SetFontWhite();
            StdDraw.picture(0.5, 0.55, ContentManager.goldenship.path);
            if(press_flash<50)
              StdDraw.text(0.5, 0.2, "Press enter to continue");
            for(Iterator<Rose> i = roses.iterator(); i.hasNext();)
              i.next().Draw();
          }
        }
        else
          ship_evolution.Draw(true);
      }
      else
        StdDraw.picture(0.5, 0.5, ContentManager.ship_move.get(0).path, angle);
    }
    else
    {
      StdDraw.text(0.5,0.5,texts.get(current_text_index));
    }
  }
}