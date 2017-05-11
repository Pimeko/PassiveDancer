import java.awt.event.KeyEvent;
import java.util.*;

public class Menu
{
  private static Animation menu_background;
  private static boolean hasPressedLeft, hasPressedRight;
  
  public static void Initialize()
  {
    menu_background = new Animation(ContentManager.menu_background, 15, false, 0.5, 0.5);
    hasPressedLeft = false;
    hasPressedRight = false;
  }
  
  public static void Update()
  {
    /* Change Keyboard */
    if(hasPressedLeft && !StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
    {
      ChangeKeyboard();
      hasPressedLeft = false;
    }
    if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
      hasPressedLeft = true;
    
    if(hasPressedRight && !StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
    {
      ChangeKeyboard();
      hasPressedRight = false;
    }
    if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
      hasPressedRight = true;
    
    /* Change Keyboard */
    
    if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
    {
      PassiveDancer.activateFeatures = true;
      MusicManager.ChangeMusic(MusicManager.Musics.InGame);
      GameLoop.Initialize();
      PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
    }
    else if(StdDraw.isKeyPressed(KeyEvent.VK_ENTER))
    {
      PassiveDancer.activateFeatures = false;
      MusicManager.ChangeMusic(MusicManager.Musics.InGame);
      GameLoop.Initialize();
      PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
    }
  }
  
  public static void Draw()
  {
    if(menu_background.IsFinished())
    {
      StdDraw.picture(0.5, 0.5, ContentManager.title_background.path);
      
      if(PassiveDancer.englishMode)
        StdDraw.text(0.9, 0.03, "< Keyboard : EN >");
      else
        StdDraw.text(0.9, 0.03, "< Keyboard : FR >");
      
      MusicManager.Draw();
    }
    else
      menu_background.Draw(true);
    
  }
  
  private static void ChangeKeyboard()
  {
    PassiveDancer.englishMode = !PassiveDancer.englishMode;
  }
}