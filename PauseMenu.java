import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu
{
  private static boolean hasPressedEscape, hasPressedR, hasPressedSpace;
  private static int timer_flash;
  
  public static void Reset()
  {
    hasPressedEscape = false;
    hasPressedR = false;
    hasPressedSpace = false;
    timer_flash = 0;
  }
  
  public static void Update()
  {
    if(StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE))
      hasPressedEscape = true;
    if(!StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE) && hasPressedEscape)
    {
      PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
      hasPressedEscape = false;
    }
    
    if(StdDraw.isKeyPressed(KeyEvent.VK_R))
      hasPressedR = true;
    if(!StdDraw.isKeyPressed(KeyEvent.VK_R) && hasPressedR)
    {
      if(PassiveDancer.activateFeatures)
        ExternalManager.SaveAchievements();
      GameLoop.ResetGame(false);
      PassiveDancer.gameState = PassiveDancer.GameState.GameLoop;
      hasPressedR = false;
    }
    
    if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
      hasPressedSpace = true;
    if(!StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && hasPressedSpace)
    {
      PassiveDancer.gameState = PassiveDancer.GameState.Menu;
      hasPressedSpace = false;
    }
    
    timer_flash++;
    timer_flash%=50;
  }
  
  public static void Draw()
  {
    StdDraw.setPenRadius(0.005);
    StdDraw.picture(0.5, 0.5, ContentManager.fog.path, 1, 1);
    StdDraw.picture(0.5, 0.5, ContentManager.background_pause.path, 1, 1);
    if(timer_flash<25)
      StdDraw.text(0.5, 0.8, "PAUSE");
    StdDraw.picture(0.35,0.5, ContentManager.menu_pause_1.path);
    StdDraw.picture(0.65,0.5, ContentManager.menu_pause_2.path);
    StdDraw.picture(0.5,0.15, ContentManager.menu_pause.path);
    if(PassiveDancer.activateFeatures)
    {
      StdDraw.text(0.5, 0.19, "Best Score : " + ExternalManager.currentBestScore);
      StdDraw.text(0.5, 0.12, "Achievements : " + Achievement.nb_achievements_done + "/11"); 
    }
    else
    {
      StdDraw.text(0.5, 0.15, "Best Score : " + ExternalManager.currentBestScore);
    }
  }
}